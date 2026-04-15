package br.gov.df.seagri.modulo_organizacao.dominio;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import br.gov.df.seagri.dominio_central.dominio.AuditoriaCompleta;
import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "alocacao_unidade")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlocacaoUnidade extends EntidadeBase implements AuditoriaCompleta {

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vinculo_usuario_id", nullable = false, updatable = false)
    private VinculoUsuario vinculoUsuario;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unidade_id", nullable = false, updatable = false)
    private Unidade unidade;

    @Setter
    @Column(name = "papel_operacional", nullable = false, length = 32)
    private String papelOperacional;

    @Setter
    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Setter
    @Column(name = "data_inicio", nullable = false)
    private OffsetDateTime dataInicio;

    @Setter
    @Column(name = "data_fim")
    private OffsetDateTime dataFim;

    @Setter
    @Column(name = "criado_por", nullable = false, length = 64, updatable = false)
    private String criadoPor;

    @Setter
    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @Setter
    @Column(name = "atualizado_por", length = 64)
    private String atualizadoPor;

    @Setter
    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    public AlocacaoUnidade(VinculoUsuario vinculoUsuario, Unidade unidade, String papelOperacional, String criadoPor) {
        this.vinculoUsuario = vinculoUsuario;
        this.unidade = unidade;
        this.papelOperacional = papelOperacional;
        this.status = "ATIVO";
        this.dataInicio = OffsetDateTime.now(ZoneOffset.UTC);
        this.criadoPor = criadoPor;
        this.criadoEm = OffsetDateTime.now(ZoneOffset.UTC);
    }

    // Método auxiliar para verificar se a alocação está vigente hoje
    public boolean isVigente() {
        if (!"ATIVO".equals(this.status)) return false;
        OffsetDateTime agora = OffsetDateTime.now(ZoneOffset.UTC);
        if (agora.isBefore(this.dataInicio)) return false;
        return this.dataFim == null || agora.isBefore(this.dataFim);
    }

}
