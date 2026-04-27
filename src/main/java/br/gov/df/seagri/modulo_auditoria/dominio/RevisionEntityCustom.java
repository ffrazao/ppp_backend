package br.gov.df.seagri.modulo_auditoria.dominio;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@Table(name = "revinfo", schema = "auditoria")
@RevisionEntity(RevisionListenerCustom.class)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RevisionEntityCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @EqualsAndHashCode.Include
    private Long id;

    @RevisionTimestamp
    @Column(nullable = false)
    private Long timestamp;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "origem")
    private String origem;

    @Column(name = "ip")
    private String ip;
}
