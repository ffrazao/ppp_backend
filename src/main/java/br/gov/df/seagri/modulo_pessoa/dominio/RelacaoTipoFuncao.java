package br.gov.df.seagri.modulo_pessoa.dominio;

import org.hibernate.envers.Audited;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import br.gov.df.seagri.modulo_pessoa.enumeracao.SexoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
