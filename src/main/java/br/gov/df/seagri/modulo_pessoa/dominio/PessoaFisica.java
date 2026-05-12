package br.gov.df.seagri.modulo_pessoa.dominio;

import br.gov.df.seagri.modulo_pessoa.enumeracao.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Table(name = "pessoa_fisica", schema = "pessoa")
@PrimaryKeyJoinColumn(name = "id")
@Audited
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PessoaFisica extends Pessoa {

    @Column(name = "cpf", length = 11)
    private String cpf;

    @Column(name = "rg")
    private String rg;

    // dataNascimento é o mesmo que pessoa.dataInicio
    // dataFalecimento é o mesmo que pessoa.dataTermino

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private SexoEnum sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoCivilEnum estadoCivil;

    @Enumerated(EnumType.STRING)
    @Column(name = "nacionalidade")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private NacionalidadeEnum nacionalidade;

    @Column(name = "apelido")
    private String apelido;

    // =========================
    // FILIAÇÃO (AJUSTADA)
    // =========================
    @Column(name = "nome_mae")
    private String nomeMae;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa_mae")
    private PessoaFisica mae;

    @Column(name = "nome_pai")
    private String nomePai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa_pai")
    private PessoaFisica pai;

    // =========================
    // LOCAL
    // =========================
    @Column(name = "local_nascimento_nome")
    private String localNascimentoNome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_local_nascimento")
    private Local localNascimento;

    @Column(name = "local_falecimento_nome")
    private String localFalecimentoNome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_local_falecimento")
    private Local localFalecimento;

    // =========================
    // PROFISSÃO
    // =========================
    @Column(name = "profissao_nome")
    private String profissaoNome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profissao")
    private Profissao profissao;

    public boolean estaFalecida() {
        return getDataTermino() != null;
    }

}
