package br.gov.df.seagri.modulo_pessoa.dominio;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;

@Entity
@Table(name = "contato", schema = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
@Audited
@Getter
@Setter
@NoArgsConstructor
public abstract class Contato extends EntidadeBase<Long> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return super.getId();
    }

    // =========================
    // OBSERVAÇÃO
    // =========================
    @Column(name = "observacao")
    private String observacao;

    /*
     * Campo livre para:
     * - contexto do contato
     * - observações operacionais
     */
}
