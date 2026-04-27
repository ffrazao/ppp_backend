package br.gov.df.seagri.modulo_pessoa.dominio;

import java.time.OffsetDateTime;

import org.hibernate.envers.Audited;

import br.gov.df.seagri.dominio_central.dominio.AuditoriaCompleta;
import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import br.gov.df.seagri.modulo_pessoa.enumeracao.StatusPessoaEnum;
import br.gov.df.seagri.modulo_pessoa.enumeracao.TipoPessoaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pessoa", schema = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
@Audited
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Pessoa extends EntidadeBase<Long> implements AuditoriaCompleta {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return super.getId();
    }

    // =========================
    // CAMPOS PRINCIPAIS
    // =========================
    @Column(name = "nome", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoPessoaEnum tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusPessoaEnum status;

    @Column(name = "data_inicio")
    private OffsetDateTime dataInicio;

    @Column(name = "data_termino")
    private OffsetDateTime dataTermino;

    @Column(name = "observacao")
    private String observacao;

    // =========================
    // AUDITORIA (estado atual)
    // =========================
    @Column(name = "criado_por", nullable = false)
    private String criadoPor;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "atualizado_por", nullable = false)
    private String atualizadoPor;

    @Column(name = "atualizado_em", nullable = false)
    private OffsetDateTime atualizadoEm;

    // =========================
    // CALLBACKS (boa prática)
    // =========================
    @PrePersist
    public void prePersist() {
        if (criadoEm == null) {
            criadoEm = OffsetDateTime.now();
        }
        if (criadoPor == null) {
            criadoPor = "sistema"; // ideal: pegar do contexto
        }

        // clone na criação (sua regra)
        atualizadoEm = criadoEm;
        atualizadoPor = criadoPor;
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = OffsetDateTime.now();
        if (atualizadoPor == null) {
            atualizadoPor = "sistema"; // ideal: contexto
        }
    }
}
