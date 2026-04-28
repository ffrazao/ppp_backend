package br.gov.df.seagri.modulo_pessoa.dominio;

import java.time.OffsetDateTime;

import org.hibernate.envers.Audited;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "pessoa_relacionamento",
    schema = "pessoa",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_relacao_pessoa_funcao",
            columnNames = {"id_relacao", "id_pessoa", "id_relacao_tipo_funcao"}
        )
    }
)
@Audited
@Getter
@Setter
@NoArgsConstructor
public class PessoaRelacionamento  extends EntidadeBase<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    // =========================
    // RELAÇÃO
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_relacao", nullable = false)
    private Relacao relacao;

    // =========================
    // PESSOA
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa", nullable = false)
    private Pessoa pessoa;

    // =========================
    // FUNÇÃO NA RELAÇÃO
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_relacao_tipo_funcao", nullable = false)
    private RelacaoTipoFuncao relacaoTipoFuncao;

    // =========================
    // TEMPORALIDADE
    // =========================
    @Column(name = "data_inicio")
    private OffsetDateTime dataInicio;

    @Column(name = "data_termino")
    private OffsetDateTime dataTermino;

    // =========================
    // OBSERVAÇÃO
    // =========================
    @Column(name = "observacao")
    private String observacao;

    @PrePersist
    @PreUpdate
    private void validarCoerencia() {
        if (relacaoTipoFuncao.getSexo() != null &&
            pessoa instanceof PessoaFisica pf &&
            pf.getSexo() != relacaoTipoFuncao.getSexo()) {

            throw new IllegalStateException("Sexo incompatível com função");
        }
    }
}
