package br.gov.df.seagri.modulo_pessoa.dominio;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "relacao", schema = "pessoa")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class Relacao extends EntidadeBase<Long> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return super.getId();
    }

    // =========================
    // TIPO DA RELAÇÃO
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_relacao_tipo", nullable = false)
    private RelacaoTipo relacaoTipo;

    // =========================
    // CONTEXTO OPCIONAL
    // =========================
    @Column(name = "nome")
    private String nome;

    /*
     * Ex:
     * "Família Silva"
     * "Sociedade Empresa XPTO"
     */

    @Column(name = "observacao")
    private String observacao;

    // =========================
    // TEMPORALIDADE
    // =========================
    @Column(name = "data_inicio")
    private OffsetDateTime dataInicio;

    @Column(name = "data_termino")
    private OffsetDateTime dataTermino;

    // =========================
    // PARTICIPANTES
    // =========================
    @OneToMany(mappedBy = "relacao", fetch = FetchType.LAZY)
    private List<PessoaRelacionamento> participantes;
}
