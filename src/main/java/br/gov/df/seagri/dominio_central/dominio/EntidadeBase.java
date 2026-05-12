package br.gov.df.seagri.dominio_central.dominio;

import java.io.Serializable;

import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public abstract class EntidadeBase<ID extends Serializable> implements Serializable {

    public abstract ID getId();

    public abstract void setId(ID id);

}
