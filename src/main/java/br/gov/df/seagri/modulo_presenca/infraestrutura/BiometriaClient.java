package br.gov.df.seagri.modulo_presenca.infraestrutura;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BiometriaClient {

    // Instanciamos o cliente HTTP do Spring
    private final RestClient restClient;

    // Inicializa o RestClient do Spring 3.5 lendo a URL do application.yml
    public BiometriaClient(@Value("${biometria.url}") String biometriaUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(biometriaUrl)
                .build();
    }

    public Map<String, Object> verificarFace(String fotoCadastroBase64, String fotoCapturadaBase64) {
        try {
            // Monta o payload exatamente como o nosso Pydantic do FastAPI espera
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("image_base64_1", fotoCadastroBase64);
            requestBody.put("image_base64_2", fotoCapturadaBase64);

            // Dispara a requisição POST para a IA em Python com tipagem segura
            return restClient.post()
                    .body(requestBody)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });

        } catch (Exception e) {
            // Em caso de falha no motor Python (timeout, container offline, etc),
            // garantimos que o sistema não quebre, mas force um score 0.0 (suspeito)
            log.error("[BIOMETRIA] Falha ao comunicar com o motor Python: {}", e.getMessage());

            Map<String, Object> fallback = new HashMap<>();
            fallback.put("is_match", false);
            fallback.put("biometric_score", 0.0);
            fallback.put("tecnico_falha", true);
            return fallback;
        }
    }

    public Map<String, Object> verificarFace(byte[] fotoCadastro, byte[] fotoCapturada) {
        try {
            Objects.requireNonNull(fotoCadastro, "fotoCadastro não pode ser nulo");
            Objects.requireNonNull(fotoCapturada, "fotoCapturada não pode ser nulo");

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            body.add("image_bytes_1", new ByteArrayResource(fotoCadastro) {
                @Override
                public String getFilename() {
                    return "ref.jpg";
                }
            });

            body.add("image_bytes_2", new ByteArrayResource(fotoCapturada) {
                @Override
                public String getFilename() {
                    return "cap.jpg";
                }
            });

            return restClient.post()
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (Exception e) {
            log.error("[BIOMETRIA] Falha ao comunicar com o motor Python: {}", e.getMessage());
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("is_match", false);
            fallback.put("biometric_score", 0.0);
            fallback.put("tecnico_falha", true);
            return fallback;
        }
    }
}
