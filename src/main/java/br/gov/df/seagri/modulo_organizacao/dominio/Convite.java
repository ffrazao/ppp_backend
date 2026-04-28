package br.gov.df.seagri.modulo_organizacao.dominio;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(schema = "folha_ponto", name = "convite")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Convite extends EntidadeBase<UUID> {

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

    // NOVO: Mapeamento da Unidade (RFC-0010)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    @Column(name = "codigo", nullable = false, length = 32, unique = true)
    private String codigo;

    // NOVO: Papel esperado ao aceitar o convite (RFC-0010)
    @Column(name = "papel_esperado", nullable = false, length = 32)
    private String papelEsperado;

    @Column(name = "data_expiracao", nullable = false)
    private OffsetDateTime dataExpiracao;

    @Setter
    @Column(name = "usado", nullable = false)
    private Boolean usado;

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

    // Construtor atualizado
    public Convite(Organizacao organizacao, Unidade unidade, String codigo, String papelEsperado,
            OffsetDateTime dataExpiracao, String criadoPor) {
        this.organizacao = organizacao;
        this.unidade = unidade;
        this.codigo = codigo;
        this.papelEsperado = papelEsperado;
        this.dataExpiracao = dataExpiracao;
        this.criadoPor = criadoPor;
        this.criadoEm = OffsetDateTime.now();
        this.usado = false;
    }

    public boolean isValido() {
        return !this.usado && this.dataExpiracao.isAfter(OffsetDateTime.now());
    }
}
