package br.gov.df.seagri.modulo_pessoa.dominio;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;

@Entity
@Table(
    name = "profissao",
    schema = "pessoa",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_profissao_nome", columnNames = "nome")
    }
)
@Audited
@Getter
@Setter
@NoArgsConstructor
public class Profissao extends EntidadeBase<Long> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return super.getId();
    }

    // =========================
    // NOME
    // =========================
    @Column(name = "nome", nullable = false)
    private String nome;

    /*
     * Ex:
     * Engenheiro Agrônomo
     * Produtor Rural
     * Técnico
     */

    // =========================
    // DESCRIÇÃO
    // =========================
    @Column(name = "descricao")
    private String descricao;
}
