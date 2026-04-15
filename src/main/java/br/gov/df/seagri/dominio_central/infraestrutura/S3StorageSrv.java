package br.gov.df.seagri.dominio_central.infraestrutura;

import java.net.URI;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Slf4j
public class S3StorageSrv {

    @Value("${storage.s3.endpoint}")
    private String endpoint;

    @Value("${storage.s3.bucket-biometria}")
    private String bucket;

    @Value("${storage.s3.access-key}")
    private String accessKey;

    @Value("${storage.s3.secret-key}")
    private String secretKey;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        // Inicializa o cliente S3 apontando para o seu SeaweedFS local
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.US_EAST_1)
                .credentialsProvider(AnonymousCredentialsProvider.create())
                .forcePathStyle(true)
                .build();

        garantirBucketExiste();
    }

    private void garantirBucketExiste() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        } catch (NoSuchBucketException e) {
            // Se o bucket "seagri-fotos" não existir (como o seu XML mostrou vazio), ele cria agora!
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
        }
    }

    // Método para salvar a imagem física e retornar um UUID de referência
    public UUID salvarFotoBase64(String fotoBase64) {
        if (fotoBase64 == null || fotoBase64.isBlank()) {
            log.debug("fotoBase64 vazio ou não informado");
            return null;
        }

        try {
            // 1. Limpa o prefixo do Base64 caso o React envie
            String base64Limpo = fotoBase64;
            if (fotoBase64.contains(",")) {
                base64Limpo = fotoBase64.substring(fotoBase64.indexOf(",") + 1);
            }

            // 2. Converte para bytes
            byte[] bytesImagem = Base64.getDecoder().decode(base64Limpo);

            // 3. Gera a Referência Única (UUID exigido pela RFC-0005)
            UUID referenciaBiometrica = UUID.randomUUID();
            String nomeArquivo = referenciaBiometrica.toString() + ".jpg";

            // 4. Salva a foto no SeaweedFS (S3)
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(nomeArquivo)
                    .contentType("image/jpeg")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytesImagem));

            log.debug("💾 FOTO SALVA COM SUCESSO NO S3: {}", nomeArquivo);

            return referenciaBiometrica;

        } catch (Exception e) {
            log.error("Erro ao processsar imagem: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar e salvar a imagem biométrica no S3", e);
        }
    }

    // Método para ler a imagem física e devolvê-la para a web
    public byte[] recuperarFoto(UUID referenciaImagem) {
        if (referenciaImagem == null) {
            log.debug("ID de Referência da imagem não informado");
            return null;
        }

        try {
            String nomeArquivo = referenciaImagem.toString() + ".jpg";

            // Monta o pedido de busca apontando para o bucket e o nome do arquivo
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(nomeArquivo)
                    .build();

            // Baixa o arquivo do SeaweedFS e converte diretamente para array de bytes
            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
            byte[] result = objectBytes.asByteArray();

            log.debug("ID de Referência da imagem {} encontrado no S3, tamanho {}.", referenciaImagem.toString(), result.length);
            return result;

        } catch (NoSuchKeyException e) {
            // Equivalente ao antigo !Files.exists(caminhoArquivo)
            log.debug("ID de Referência ({}) não encontrado no bucket S3", referenciaImagem.toString());
            return null;
        } catch (Exception e) {
            log.error("❌ Erro ao ler imagem do S3: " + e.getMessage());
            throw new RuntimeException("Falha ao ler evidência biométrica do S3", e);
        }
    }

}
