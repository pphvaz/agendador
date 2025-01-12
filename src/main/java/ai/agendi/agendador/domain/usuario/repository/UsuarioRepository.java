package ai.agendi.agendador.domain.usuario.repository;

import ai.agendi.agendador.domain.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
