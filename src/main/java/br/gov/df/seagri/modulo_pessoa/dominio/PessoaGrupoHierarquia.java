package br.gov.df.seagri.modulo_pessoa.dominio;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;

@Entity
@Table(name = "pessoa_grupo_hierarquia", schema = "pessoa")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class PessoaGrupoHierarquia  extends EntidadeBase<Long> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return super.getId();
    }

    // =========================
    // GRUPO PAI
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo_pai", nullable = false)
    private PessoaGrupo grupoPai;

    // =========================
    // GRUPO FILHO
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo_filho", nullable = false)
    private PessoaGrupo grupoFilho;

    // =========================
    // NOMES DA RELAÇÃO
    // =========================
    @Column(name = "nome_pai_para_filho")
    private String nomePaiParaFilho;

    /*
     * Ex:
     * "gerencia"
     * "coordena"
     */

    @Column(name = "nome_filho_para_pai")
    private String nomeFilhoParaPai;

    /*
     * Ex:
     * "é gerido por"
     * "subordinado a"
     */
}
