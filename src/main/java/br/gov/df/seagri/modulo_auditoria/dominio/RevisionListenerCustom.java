package br.gov.df.seagri.modulo_auditoria.dominio;

import org.hibernate.envers.RevisionListener;

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

    private Long getUsuarioLogado() {
        // TODO: integrar com Spring Security
        return null;
    }
}
