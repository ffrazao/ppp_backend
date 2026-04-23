package br.gov.df.seagri.modulo_presenca.aplicacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.seagri.dominio_central.aplicacao.GeolocalizacaoSrv;
import br.gov.df.seagri.dominio_central.infraestrutura.S3StorageArmazenamentoFotoSrv;
import br.gov.df.seagri.modulo_organizacao.aplicacao.UnidadeSrv;
import br.gov.df.seagri.modulo_organizacao.dominio.Unidade;
import br.gov.df.seagri.modulo_presenca.dominio.RegistroPresenca;
import br.gov.df.seagri.modulo_presenca.infraestrutura.BiometriaClient;
import br.gov.df.seagri.modulo_presenca.infraestrutura.RegistroPresencaDAO;
import br.gov.df.seagri.modulo_presenca.web.dto.RegistroPresencaRequestDTO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegistroPresencaSrv {

    private final BiometriaClient biometriaClient;
    private final RegistroPresencaDAO registroPresencaDAO;
    private final UnidadeSrv unidadeSrv;
    private final GeolocalizacaoSrv geolocalizacaoSrv;
    //private final LocalArmazenamentoFotoSrv armazenamentoLocalSrv;
    private final S3StorageArmazenamentoFotoSrv s3StorageSrv;

    public RegistroPresencaSrv(RegistroPresencaDAO registroPresencaDAO,
                               UnidadeSrv unidadeSrv,
                               GeolocalizacaoSrv geolocalizacaoSrv,
                               BiometriaClient biometriaClient,
                               //LocalArmazenamentoFotoSrv armazenamentoLocalSrv,
                               S3StorageArmazenamentoFotoSrv s3StorageSrv) {
        this.registroPresencaDAO = registroPresencaDAO;
        this.unidadeSrv = unidadeSrv;
        this.geolocalizacaoSrv = geolocalizacaoSrv;
        this.biometriaClient = biometriaClient;
        //this.armazenamentoLocalSrv = armazenamentoLocalSrv;
        this.s3StorageSrv = s3StorageSrv;
    }

    @Transactional
    public RegistroPresenca registrar(UUID organizacaoId, String userId, RegistroPresencaRequestDTO dto) {

        // Rastreador visual no log para confirmar a chegada da imagem Base64
        if (dto.getFotoBase64() != null) {
            log.debug("📸 SUCESSO! Foto recebida no Java. Tamanho: {} caracter(es).", dto.getFotoBase64().length());
        } else {
            log.debug("❌ AVISO: A foto chegou NULA. O usuário negou a permissão da câmera.");
        }

        // Busca a unidade passando o Tenant (Organização) e o ID da Unidade
        Unidade unidade = unidadeSrv.buscarPorId(organizacaoId, dto.getUnidadeId());

        String statusAdministrativo = "VALIDO";
        Double pontuacaoRisco = 0.0;

        // Usaremos uma Lista em vez de StringBuilder para facilitar a conversão para JSON (exigência do PostgreSQL JSONB)
        List<String> indicadores = new ArrayList<>();

        // 1. Validação de GPS (O usuário recusou a localização?)
        if (dto.getLatitude() == null || dto.getLongitude() == null) {
            statusAdministrativo = "PENDENTE";
            pontuacaoRisco += 50.0;
            indicadores.add("SEM_GPS"); // Adiciona na lista sem os colchetes
        } else {
            // Se tem GPS e a unidade possui centro geográfico configurado, calcula a distância
            if (unidade.getCentroGeoLat() != null && unidade.getCentroGeoLng() != null) {
                double distancia = geolocalizacaoSrv.calcularDistanciaMetros(
                        dto.getLatitude(), dto.getLongitude(),
                        unidade.getCentroGeoLat(), unidade.getCentroGeoLng()
                );

                // Se estiver muito longe da unidade, penaliza
                if (distancia > unidade.getRaioGeoMetros()) {
                    statusAdministrativo = "PENDENTE";
                    pontuacaoRisco += 40.0;
                    indicadores.add("FORA_DO_RAIO");
                }
            } else {
                indicadores.add("UNIDADE_SEM_GEO");
            }
        }

        // 2. Validação de Biometria (O usuário recusou a câmera?)
        UUID referenciaBiometrica = null;
        if (dto.getFotoBase64() != null) {
            // referenciaBiometrica = armazenamentoLocalSrv.salvarFotoBase64(dto.getFotoBase64());
            // log.debug("UUID temporário da foto: {} - armazenamento local", referenciaBiometrica);

            referenciaBiometrica = s3StorageSrv.salvarFotoBase64(dto.getFotoBase64());
            log.debug("UUID da foto no S3: {} - armazenamento em s3", referenciaBiometrica);
        }

        if (referenciaBiometrica == null) {
            statusAdministrativo = "PENDENTE";
            pontuacaoRisco += 50.0; // Soma mais 50 pontos de risco
            indicadores.add("SEM_FOTO");
        } else {
            // comparar a foto biometrica com a foto de referencia do usuário
            try {
                Map<String,Object> verificaFace = biometriaClient.verificarFace(dto.getFotoBase64(), dto.getFotoBase64());

                Boolean isMatch = (Boolean) Optional.ofNullable(verificaFace.get("is_match")).orElse(Boolean.FALSE);

                Boolean falhaTecnica = (Boolean) Optional.ofNullable(verificaFace.get("tecnico_falha")).orElse(Boolean.FALSE);

                // Lógica de Ponto Pendente a todo custo
                if (falhaTecnica) {
                    statusAdministrativo = "PENDENTE";
                    pontuacaoRisco += 100.0;
                    indicadores.add("ERRO_SISTEMA_BIOMETRIA");
                } else if (!isMatch) {
                    statusAdministrativo = "PENDENTE";
                    pontuacaoRisco += 50.0;
                    indicadores.add("FOTO_NAO_RECONHECIDA");
                }
            } catch (Exception e) {
                // Se o Python retornar 400 (Rosto não detectado), o Java cai aqui
                log.error("Erro na integração biométrica: {}", e.getMessage());
                statusAdministrativo = "PENDENTE";
                pontuacaoRisco += 100.0; // Risco máximo pois a validação falhou
                indicadores.add("ERRO_DETECCAO_FACIAL");
            }
        }

        // 3. Converte a Lista do Java para uma String em formato JSON Array válido para o PostgreSQL
        String jsonIndicadores = "[]";
        if (!indicadores.isEmpty()) {
            jsonIndicadores = "[\"" + String.join("\", \"", indicadores) + "\"]";
        }

        // Cria o evento bruto e imutável de presença (Sempre preservado conforme RFC-008)
        RegistroPresenca registro = new RegistroPresenca(
                organizacaoId,
                unidade.getId(),
                userId,
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getPrecisaoGps(),
                dto.getDispositivoId(),
                dto.getModoRegistro(),
                dto.getCapturadoEm(),
                "RECEBIDO"
        );

        // Aplica os resultados da análise antifraude
        registro.setStatusAdministrativo(statusAdministrativo);
        registro.setPontuacaoRisco(pontuacaoRisco);

        // Envia o JSON válido (Ex: '["SEM_GPS", "SEM_FOTO"]') para a coluna JSONB
        registro.setIndicadoresRisco(jsonIndicadores);

        // Vincula o UUID da foto salva ao registro de banco de dados, se existir
        if (referenciaBiometrica != null) {
            registro.registrarEvidenciaBiometrica(referenciaBiometrica, Double.valueOf(0.0), Boolean.FALSE);
        }

        return registroPresencaDAO.save(registro);
    }
    // Método fiel ao nome declarado na sua interface RegistroPresencaDAO
    public List<RegistroPresenca> buscarPorUsuario(String usuarioId) {
        return registroPresencaDAO.buscarPorUsuario(usuarioId);
    }

    // Método que garante a segurança e busca os bytes da foto
    public byte[] buscarFotoBiometrica(UUID organizacaoId, String usuarioId, UUID presencaId) {
        // Busca o evento bruto no banco
        RegistroPresenca presenca = registroPresencaDAO.findById(presencaId)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));

        // Segurança: O usuário só pode ver a sua própria foto dentro da sua organização
        if (!presenca.getUsuarioId().equals(usuarioId) || !presenca.getOrganizacaoId().equals(organizacaoId)) {
            throw new RuntimeException("Acesso negado à evidência biométrica");
        }

        byte[] result = null;
        // result = armazenamentoLocalSrv.recuperarFoto(presenca.getReferenciaBiometrica());

        result = s3StorageSrv.recuperarFoto(presenca.getReferenciaBiometrica());

        return result;
    }

}
