package br.gov.df.seagri.modulo_seguranca.dominio;

import java.time.OffsetDateTime;

import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import br.gov.df.seagri.modulo_pessoa.dominio.Pessoa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(schema = "seguranca", name = "identidade_acesso")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class IdentidadeAcesso extends EntidadeBase<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    // Relacionamento com a Identidade Canônica (Pessoa)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    // A Identidade Técnica (Efêmera) que vem do Keycloak
    @Column(name = "keycloak_sub", nullable = false, unique = true, length = 64)
    private String keycloakSub;

    @Column(name = "provedor", nullable = false, length = 32)
    private String provedor = "keycloak";

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm = OffsetDateTime.now();

}
