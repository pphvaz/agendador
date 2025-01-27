package ai.agendi.agendador.domain.usuario.repository;

import ai.agendi.agendador.domain.usuario.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
