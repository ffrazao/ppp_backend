package br.gov.df.seagri.modulo_pessoa.dominio;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import br.gov.df.seagri.modulo_pessoa.enumeracao.PapelGrupoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "pessoa_grupo_membros", schema = "pessoa")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class PessoaGrupoMembro  extends EntidadeBase<Long> {

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
    @JoinColumn(name = "id_grupo", nullable = false)
    private PessoaGrupo grupo;

    // =========================
    // PAPEL NO GRUPO
    // =========================
    @Enumerated(EnumType.STRING)
    @Column(name = "papel")
    private PapelGrupoEnum papel;

    /*
     * Ex:
     * - GESTOR
     * - MEMBRO
     * - ASSOCIADO
     * - INTERESSADO
     */

    // =========================
    // DESCRIÇÃO LIVRE
    // =========================
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_entrada")
    private LocalDate dataEntrada;

    @Column(name = "data_saida")
    private LocalDate dataSaida;

}
