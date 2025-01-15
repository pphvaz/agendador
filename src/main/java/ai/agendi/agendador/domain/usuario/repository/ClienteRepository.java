package ai.agendi.agendador.domain.usuario.repository;

import ai.agendi.agendador.domain.usuario.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByIdAndAtivoTrue(Long id);
    Optional<Cliente> findByEmailAndAtivoTrue(String email);
    Optional<Cliente> findByCelularAndAtivoTrue(String celular);
    Optional<Cliente> findByCpfAndAtivoTrue(String cpf);

    @Modifying
    @Query("UPDATE Cliente c SET c.ativo = false WHERE c.id = :id")
    int logicalDeletionByClienteId(@Param("id") Long id);

}
