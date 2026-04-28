package br.gov.df.seagri.modulo_organizacao.dominio;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;

import br.gov.df.seagri.dominio_central.dominio.AuditoriaCompleta;
import br.gov.df.seagri.dominio_central.dominio.EntidadeBase;
import br.gov.df.seagri.modulo_auditoria.dominio.AuditoriaListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(schema = "folha_ponto", name = "organizacao")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Audited
@EntityListeners(AuditoriaListener.class)
public class Organizacao extends EntidadeBase<UUID> implements AuditoriaCompleta {

    @Id
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @PrePersist
    public void gerarIdSeNecessario() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    @Setter
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Setter
    @Column(name = "status", nullable = false, length = 32)
    private String status;

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

    public Organizacao(String nome, String criadoPor) {
        this.nome = nome;
        this.status = "ATIVO";
        this.criadoPor = criadoPor;
        this.criadoEm = OffsetDateTime.now(ZoneOffset.UTC);
    }

}
