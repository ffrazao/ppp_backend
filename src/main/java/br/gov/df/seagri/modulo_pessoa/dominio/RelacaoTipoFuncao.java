package br.gov.df.seagri.modulo_pessoa.dominio;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import br.gov.df.seagri.modulo_pessoa.enumeracao.SexoEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(
    name = "relacao_tipo_funcao",
    schema = "pessoa",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_relacao_tipo_funcao_nome",
            columnNames = {"id_relacao_tipo", "nome"}
        )
    }
)
@Audited
@Getter
@Setter
@NoArgsConstructor
public class RelacaoTipoFuncao extends EntidadeBase<Long> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return super.getId();
    }

    // =========================
    // RELAÇÃO COM TIPO
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_relacao_tipo", nullable = false)
    private RelacaoTipo relacaoTipo;

    // =========================
    // NOME DA FUNÇÃO
    // =========================
    @Column(name = "nome", nullable = false)
    private String nome;

    /*
     * Ex:
     * pai, mãe, filho, tio
     * sócio, administrador
     */

    // =========================
    // RESTRIÇÃO DE SEXO
    // =========================
    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private SexoEnum sexo;

    /*
     * Quando NULL → qualquer sexo
     * Quando preenchido → validação obrigatória
     */
}
