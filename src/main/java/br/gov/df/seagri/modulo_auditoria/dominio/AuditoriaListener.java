package br.gov.df.seagri.modulo_auditoria.dominio;

import java.time.OffsetDateTime;

import org.springframework.security.core.context.SecurityContextHolder;

import br.gov.df.seagri.dominio_central.dominio.AuditoriaCompleta;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class AuditoriaListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof AuditoriaCompleta aud) {

            OffsetDateTime agora = OffsetDateTime.now();

            if (aud.getCriadoEm() == null) {
                aud.setCriadoEm(agora);
            }

            if (aud.getCriadoPor() == null) {
                aud.setCriadoPor(getUsuario());
            }

            // clone na criação
            aud.setAtualizadoEm(aud.getCriadoEm());
            aud.setAtualizadoPor(aud.getCriadoPor());
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof AuditoriaCompleta aud) {

            aud.setAtualizadoEm(OffsetDateTime.now());

            if (aud.getAtualizadoPor() == null) {
                aud.setAtualizadoPor(getUsuario());
            }
        }
    }

    private String getUsuario() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "sistema";
    }

}
