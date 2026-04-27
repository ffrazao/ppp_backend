package br.gov.df.seagri.modulo_pessoa.dominio;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pessoa_grupo", schema = "pessoa")
@PrimaryKeyJoinColumn(name = "id")
@Audited
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PessoaGrupo extends Pessoa {

    // =========================
    // IDENTIFICAÇÃO COMPLEMENTAR
    // =========================
    @Column(name = "nome_resumido_sigla")
    private String nomeResumidoSigla;

    /*
     * Convenção:
     * - Pessoa.nome → nome principal
     * - nomeResumidoSigla → abreviação / sigla
     */

    // =========================
    // CICLO DE VIDA
    // =========================
    @Column(name = "data_criacao_grupo")
    private LocalDate dataCriacaoGrupo;

    // data_termino herdado de Pessoa

    // =========================
    // HIERARQUIA ENTRE GRUPOS
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo_pai")
    private PessoaGrupo grupoPai;

    @OneToMany(mappedBy = "grupoPai")
    private List<PessoaGrupo> subgrupos;

    @Column(name = "nome_relacao_pai_para_filho")
    private String nomeRelacaoPaiParaFilho;

    @Column(name = "nome_relacao_filho_para_pai")
    private String nomeRelacaoFilhoParaPai;

    /*
     * Exemplo:
     * - pai → filho: "gerencia"
     * - filho → pai: "é gerido por"
     */

    // =========================
    // MEMBROS DO GRUPO
    // =========================
    @OneToMany(mappedBy = "grupo")
    private List<PessoaGrupoMembro> membros;
}
