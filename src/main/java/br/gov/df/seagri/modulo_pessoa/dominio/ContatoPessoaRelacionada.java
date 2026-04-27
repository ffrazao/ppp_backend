package br.gov.df.seagri.modulo_pessoa.dominio;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "contato_pessoa_relacionada", schema = "pessoa")
@PrimaryKeyJoinColumn(name = "id")
@Audited
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContatoPessoaRelacionada extends Contato {

    // =========================
    // REFERÊNCIA A PESSOA
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa_relacionada")
    private Pessoa pessoaRelacionada;

    // =========================
    // NOME LIVRE
    // =========================
    @Column(name = "nome")
    private String nome;

    /*
     * usado quando não há vínculo formal
     */

    // =========================
    // TIPO DE RELAÇÃO
    // =========================
    @Column(name = "tipo_relacionamento")
    private String tipoRelacionamento;

    /*
     * Ex:
     * pai, mãe, vizinho, contador, advogado...
     */

    // =========================
    // DESCRIÇÃO COMPLEMENTAR
    // =========================
    @Column(name = "descricao")
    private String descricao;
}
