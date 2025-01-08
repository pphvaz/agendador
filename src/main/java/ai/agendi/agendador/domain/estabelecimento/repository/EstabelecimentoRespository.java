package ai.agendi.agendador.domain.estabelecimento.repository;

import ai.agendi.agendador.domain.estabelecimento.model.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstabelecimentoRespository extends JpaRepository<Estabelecimento, Long> {
}
