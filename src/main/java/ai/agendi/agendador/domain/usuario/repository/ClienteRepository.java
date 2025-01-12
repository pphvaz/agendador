package ai.agendi.agendador.domain.usuario.repository;

import ai.agendi.agendador.domain.usuario.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Page<Cliente> findAll(Pageable pageable);

    Cliente findByCpfAndAtivoTrue(String cpf);

    Cliente findByEmailAndAtivoTrue(String email);

    Cliente findByCelularAndAtivoTrue(String celular);

    @Modifying
    @Query("UPDATE Cliente c SET c.ativo = false WHERE c.idUsuario = :id")
    int logicalDeletionByClienteId(Long id);

    Optional<Cliente> findByIdUsuarioAndAtivoTrue(Long id);
}