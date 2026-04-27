package br.gov.df.seagri.modulo_pessoa.dominio;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import br.gov.df.seagri.modulo_pessoa.enumeracao.ClassificacaoContatoEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "pessoa_contato", schema = "pessoa")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class PessoaContato  extends EntidadeBase<Long> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return super.getId();
    }

    // =========================
    // RELACIONAMENTOS
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa", nullable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contato", nullable = false)
    private Contato contato;

    // =========================
    // CLASSIFICAÇÃO
    // =========================
    @Enumerated(EnumType.STRING)
    @Column(name = "classificacao")
    private ClassificacaoContatoEnum classificacao;

    /*
     * Ex:
     * RESIDENCIAL
     * COMERCIAL
     * OUTRO
     */

    // =========================
    // PRIORIDADE
    // =========================
    @Column(name = "prioridade")
    private Integer prioridade;

    /*
     * Quanto menor o número, maior a prioridade:
     * 1 = principal
     * 2 = secundário
     * ...
     */
}
