package br.gov.df.seagri.modulo_organizacao.dominio;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.gov.df.seagri.dominio_central.dominio.AuditoriaCompleta;
import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import br.gov.df.seagri.dominio_central.dominio.PertenceOrganizacao;
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
@Table(schema = "folha_ponto", name = "unidade")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Unidade extends EntidadeBase<UUID> implements AuditoriaCompleta, PertenceOrganizacao {

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false, updatable = false)
    private Organizacao organizacao;

    @Setter
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Setter
    @Column(name = "tipo_geometria", nullable = false, length = 16)
    private String tipoGeometria;

    @Setter
    @Column(name = "centro_geo_lat")
    private Double centroGeoLat;

    @Setter
    @Column(name = "centro_geo_lng")
    private Double centroGeoLng;

    @Setter
    @Column(name = "raio_geo_metros")
    private Integer raioGeoMetros;

    @JdbcTypeCode(SqlTypes.JSON)
    @Setter
    @Column(name = "poligono_geo", columnDefinition = "jsonb")
    private String poligonoGeo;

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

    public Unidade(Organizacao organizacao, String nome, String tipoGeometria, String criadoPor) {
        this.organizacao = organizacao;
        this.nome = nome;
        this.tipoGeometria = tipoGeometria;
        this.criadoPor = criadoPor;
        this.criadoEm = OffsetDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public UUID obterOrganizacaoId() {
        return (UUID) this.organizacao.getId();
    }
}
