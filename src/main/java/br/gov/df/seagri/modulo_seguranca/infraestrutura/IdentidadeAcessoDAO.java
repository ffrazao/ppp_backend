package br.gov.df.seagri.modulo_seguranca.infraestrutura;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.df.seagri.dominio_central.infraestrutura.BaseDAO;
import br.gov.df.seagri.modulo_seguranca.dominio.IdentidadeAcesso;

@Repository
public interface IdentidadeAcessoDAO extends BaseDAO<IdentidadeAcesso, Long> {

    /**
     * Busca a identidade de acesso através do Subject (sub) do Keycloak,
     * já trazendo os dados da Pessoa (Identidade Canônica) na mesma query.
     */
    @Query("SELECT i FROM IdentidadeAcesso i JOIN FETCH i.pessoa WHERE i.keycloakSub = :sub")
    Optional<IdentidadeAcesso> findByKeycloakSubComPessoa(@Param("sub") String sub);

}
