package ai.agendi.agendador.domain.usuario.repository;

import ai.agendi.agendador.domain.usuario.model.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
    Prestador findByIdAndAtivoTrue(Long id);

    Prestador findByIdAndAtivoTrueAndIsOwnerTrue(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Prestador p SET p.ativo = false WHERE p.id = :id")
    void logicallyDeleteById(Long id);
}
