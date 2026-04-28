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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(schema = "folha_ponto", name = "relacionamento")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Audited
@EntityListeners(AuditoriaListener.class)
public class Relacionamento extends EntidadeBase<UUID> implements AuditoriaCompleta, PertenceOrganizacao {

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false, updatable = false)
    private Organizacao organizacao;

    @Column(name = "sujeito_id", nullable = false, length = 64, updatable = false)
    private String sujeitoId;

    @Column(name = "objeto_id", nullable = false, length = 64, updatable = false)
    private String objetoId;

    @Setter
    @Column(name = "tipo_relacionamento", nullable = false, length = 64)
    private String tipoRelacionamento;

    @Setter
    @Column(name = "inicio_validade")
    private OffsetDateTime inicioValidade;

    @Setter
    @Column(name = "fim_validade")
    private OffsetDateTime fimValidade;

    @Column(name = "criado_por", nullable = false, length = 64, updatable = false)
    private String criadoPor;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @Setter
    @Column(name = "atualizado_por", length = 64)
    private String atualizadoPor;

    @Setter
    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    public Relacionamento(Organizacao organizacao, String sujeitoId, String objetoId, String tipoRelacionamento,
            String criadoPor) {
        this.organizacao = organizacao;
        this.sujeitoId = sujeitoId;
        this.objetoId = objetoId;
        this.tipoRelacionamento = tipoRelacionamento;
        this.criadoPor = criadoPor;
        this.criadoEm = OffsetDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public UUID obterOrganizacaoId() {
        return (UUID) this.organizacao.getId();
    }

}
