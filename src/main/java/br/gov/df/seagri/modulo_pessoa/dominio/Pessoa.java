package br.gov.df.seagri.modulo_pessoa.dominio;

import java.time.OffsetDateTime;

import org.hibernate.envers.Audited;

import br.gov.df.seagri.dominio_central.dominio.AuditoriaCompleta;
import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import br.gov.df.seagri.modulo_auditoria.dominio.AuditoriaListener;
import br.gov.df.seagri.modulo_pessoa.enumeracao.StatusPessoaEnum;
import br.gov.df.seagri.modulo_pessoa.enumeracao.TipoPessoaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pessoa", schema = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Audited
@EntityListeners(AuditoriaListener.class)
public class Pessoa extends EntidadeBase<Long> implements AuditoriaCompleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

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

}
