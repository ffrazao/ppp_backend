package br.gov.df.seagri.modulo_seguranca.aplicacao;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.seagri.modulo_pessoa.dominio.PessoaFisica;
import br.gov.df.seagri.modulo_pessoa.enumeracao.TipoPessoaEnum;
import br.gov.df.seagri.modulo_pessoa.infraestrutura.PessoaFisicaDAO;
import br.gov.df.seagri.modulo_presenca.infraestrutura.BiometriaClient;
import br.gov.df.seagri.modulo_seguranca.dominio.IdentidadeAcesso;
import br.gov.df.seagri.modulo_seguranca.dominio.PerfilBiometrico;
import br.gov.df.seagri.modulo_seguranca.infraestrutura.IdentidadeAcessoDAO;
import br.gov.df.seagri.modulo_seguranca.infraestrutura.PerfilBiometricoDAO;
import br.gov.df.seagri.modulo_seguranca.web.dto.OnboardingRequestDTO;

@Service
public class OnboardingSrv {

    private final PessoaFisicaDAO pessoaFisicaDAO;
    private final IdentidadeAcessoDAO identidadeAcessoDAO;
    private final PerfilBiometricoDAO perfilBiometricoDAO;
    private final BiometriaClient biometriaClient;

    public OnboardingSrv(PessoaFisicaDAO pessoaFisicaDAO,
            IdentidadeAcessoDAO identidadeAcessoDAO,
            PerfilBiometricoDAO perfilBiometricoDAO,
            BiometriaClient biometriaClient) {
        this.pessoaFisicaDAO = pessoaFisicaDAO;
        this.identidadeAcessoDAO = identidadeAcessoDAO;
        this.perfilBiometricoDAO = perfilBiometricoDAO;
        this.biometriaClient = biometriaClient;
    }

    @Transactional
    public void processarOnboarding(String keycloakSub, OnboardingRequestDTO request) {

        // 1. Busca ou Cria a Pessoa Física (O dono do CPF)
        PessoaFisica pessoa = pessoaFisicaDAO.findByCpf(request.getCpf()).orElse(null);

        // Se não existir, criamos a Identidade Canônica raiz
        if (pessoa == null) {
            pessoa = new PessoaFisica();
            pessoa.setCpf(request.getCpf());
            pessoa.setTipo(TipoPessoaEnum.FISICA);
            // Definição genérica por enquanto. Aqui você poderá captar request.getNome() no
            // futuro.
            pessoa.setNome("Servidor - CPF " + request.getCpf());

            // Salva a nova Pessoa Física no banco
            pessoa = pessoaFisicaDAO.save(pessoa);
        }

        // 2. Vincula a Identidade (Mundo Keycloak -> Mundo Negócio)
        IdentidadeAcesso novaIdentidade = new IdentidadeAcesso();
        novaIdentidade.setKeycloakSub(keycloakSub);
        novaIdentidade.setPessoa(pessoa);
        novaIdentidade.setProvedor("keycloak");
        identidadeAcessoDAO.save(novaIdentidade);

        // 3. EXTRAÇÃO DO VETOR BIOMÉTRICO (A Mágica da LGPD)
        byte[] vetorBiometrico = biometriaClient.extrairVetorBiometrico(request.getFotoBase64());

        // 4. Salva o perfil biométrico matemático no banco de dados
        PerfilBiometrico perfil = new PerfilBiometrico();
        perfil.setUsuarioId(keycloakSub);
        perfil.setModeloBiometrico(vetorBiometrico);
        perfil.setVersaoModelo("Facenet512-mock"); // Nome hipotético do modelo IA
        perfil.setCriadoPor(keycloakSub);
        perfil.setCriadoEm(OffsetDateTime.now());

        perfilBiometricoDAO.save(perfil);
    }

}
