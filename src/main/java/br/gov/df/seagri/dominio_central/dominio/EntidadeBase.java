package br.gov.df.seagri.dominio_central.dominio;

import java.io.Serializable;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter(AccessLevel.PROTECTED) // id não deve ser alterado livremente
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public abstract class EntidadeBase<ID extends Serializable> implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    private ID id;

}
