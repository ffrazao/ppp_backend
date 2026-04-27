package br.gov.df.seagri.modulo_pessoa.dominio;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;

import java.time.OffsetDateTime;

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

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return super.getId();
    }

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
}
