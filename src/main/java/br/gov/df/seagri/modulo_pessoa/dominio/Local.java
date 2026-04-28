package br.gov.df.seagri.modulo_pessoa.dominio;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import br.gov.df.seagri.modulo_pessoa.enumeracao.TipoLocalEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Entity
@Table(name = "local", schema = "pessoa")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class Local  extends EntidadeBase<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    // =========================
    // IDENTIFICAÇÃO
    // =========================
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "sigla")
    private String sigla;

    // =========================
    // TIPO (ENUM DO BANCO)
    // =========================
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoLocalEnum tipo;

    // =========================
    // HIERARQUIA
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_local_pai")
    private Local pai;

    @OneToMany(mappedBy = "pai")
    private List<Local> filhos;

    // =========================
    // GEOMETRIA (PostGIS)
    // =========================

    // Ponto central (centroide)
    @Column(name = "centroide", columnDefinition = "public.geometry(Point, 4326)")
    private Point centroide;

    // Área geográfica
    @Column(name = "area", columnDefinition = "public.geometry(MultiPolygon, 4326)")
    private MultiPolygon area;
}
