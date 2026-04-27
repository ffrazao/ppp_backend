package br.gov.df.seagri.modulo_pessoa.dominio;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;

import java.util.List;

@Entity
@Table(
    name = "relacao_tipo",
    schema = "pessoa",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_relacao_tipo_nome", columnNames = "nome")
    }
)
@Audited
@Getter
@Setter
@NoArgsConstructor
public class RelacaoTipo extends EntidadeBase<Long> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return super.getId();
    }

    // =========================
    // IDENTIFICAÇÃO
    // =========================
    @Column(name = "nome", nullable = false)
    private String nome;

    /*
     * Ex:
     * FAMILIAR
     * SOCIETARIA
     * CONTRATUAL
     * INSTITUCIONAL
     */

    // =========================
    // RELACIONAMENTOS
    // =========================
    @OneToMany(mappedBy = "relacaoTipo", fetch = FetchType.LAZY)
    private List<RelacaoTipoFuncao> funcoes;
}
