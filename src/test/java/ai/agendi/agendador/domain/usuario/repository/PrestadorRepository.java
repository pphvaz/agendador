package ai.agendi.agendador.domain.usuario.repository;

import ai.agendi.agendador.domain.usuario.model.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
}
