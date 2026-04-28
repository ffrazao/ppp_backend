package br.gov.df.seagri.modulo_auditoria.dominio;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;

public class RevisionListenerCustom implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        RevisionEntityCustom rev = (RevisionEntityCustom) revisionEntity;

        // 🔹 Aqui você poderá integrar depois com:
        // - Spring Security (usuário logado)
        // - contexto da requisição
        // - headers (IP, origem, etc.)

        rev.setIdUsuario(getUsuarioLogado());
        rev.setOrigem("SISTEMA");
        rev.setIp("0.0.0.0");
    }

    private String getUsuarioLogado() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "sistema";
    }
}
