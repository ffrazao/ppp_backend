package br.gov.df.seagri.modulo_seguranca.dominio;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "perfil_biometrico", schema = "seguranca")
@Getter
@Setter
@NoArgsConstructor
public class PerfilBiometrico extends EntidadeBase<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    // bytea no PostgreSQL é mapeado como byte[] no Java
    @Column(name = "modelo_biometrico", nullable = false)
    private byte[] modeloBiometrico;

    @Column(name = "versao_modelo", nullable = false)
    private String versaoModelo;

    @Column(name = "criado_por", nullable = false)
    private String criadoPor;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "atualizado_por")
    private String atualizadoPor;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;
}
