package br.gov.df.seagri.dominio_central.dominio;

import java.time.OffsetDateTime;

public interface RastreavelCriacao {
    String getCriadoPor();
    OffsetDateTime getCriadoEm();
    void setCriadoPor(String criadoPor);
    void setCriadoEm(OffsetDateTime criadoEm);
}
