package br.gov.df.seagri.modulo_seguranca.infraestrutura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.seagri.modulo_seguranca.dominio.PerfilBiometrico;

@Repository
public interface PerfilBiometricoDAO extends JpaRepository<PerfilBiometrico, Long> {

}
