package ai.agendi.agendador.domain.usuario.repository;

import ai.agendi.agendador.domain.estabelecimento.model.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Long> {
}
