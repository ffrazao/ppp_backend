package br.gov.df.seagri.dominio_central.dominio;

import java.time.OffsetDateTime;

public interface RastreavelModificacao {
    String getAtualizadoPor();
    OffsetDateTime getAtualizadoEm();
    void setAtualizadoPor(String atualizadoPor);
    void setAtualizadoEm(OffsetDateTime atualizadoEm);
}
