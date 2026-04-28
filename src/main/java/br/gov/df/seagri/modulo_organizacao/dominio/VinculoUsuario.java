package br.gov.df.seagri.modulo_organizacao.dominio;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;

import br.gov.df.seagri.dominio_central.dominio.AuditoriaCompleta;
import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import br.gov.df.seagri.dominio_central.dominio.PertenceOrganizacao;
import br.gov.df.seagri.modulo_auditoria.dominio.AuditoriaListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(schema = "folha_ponto", name = "vinculo_usuario", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "usuario_id", "organizacao_id" })
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Audited
@EntityListeners(AuditoriaListener.class)
public class VinculoUsuario extends EntidadeBase<UUID> implements AuditoriaCompleta, PertenceOrganizacao {

    @Id
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @PrePersist
    public void gerarIdSeNecessario() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false, updatable = false)
    private Organizacao organizacao;

    @Setter
    @Column(name = "usuario_id", nullable = false, length = 64, updatable = false)
    private String usuarioId;

    @Setter
    @Column(name = "papel", nullable = false, length = 32)
    private String papel;

    @Setter
    @Column(name = "status", nullable = false, length = 32)
    private String status;

    // NOVOS CAMPOS DE TEMPORALIDADE DA RFC-0010
    @Setter
    @Column(name = "data_inicio", nullable = false)
    private OffsetDateTime dataInicio;

    @Setter
    @Column(name = "data_fim")
    private OffsetDateTime dataFim;

    @Setter
    @Column(name = "criado_por", nullable = false, length = 64, updatable = false)
    private String criadoPor;

    @Setter
    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @Setter
    @Column(name = "atualizado_por", length = 64)
    private String atualizadoPor;

    @Setter
    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    public VinculoUsuario(Organizacao organizacao, String usuarioId, String papel, String criadoPor) {
        this.organizacao = organizacao;
        this.usuarioId = usuarioId;
        this.papel = papel;
        this.status = "ATIVO";
        this.dataInicio = OffsetDateTime.now(ZoneOffset.UTC); // Inicializa com a data atual
        this.criadoPor = criadoPor;
        this.criadoEm = OffsetDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public UUID obterOrganizacaoId() {
        return (UUID) this.organizacao.getId();
    }

}
