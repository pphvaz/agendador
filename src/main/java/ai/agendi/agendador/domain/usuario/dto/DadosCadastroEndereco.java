package ai.agendi.agendador.domain.usuario.dto;

import ai.agendi.agendador.domain.estabelecimento.model.Estabelecimento;
import ai.agendi.agendador.domain.usuario.model.Cliente;

public record DadosCadastroEndereco (
        String cep,
        String logradouro,
        String numero,
        String bairro,
        String cidade,
        String uf,
        String complemento,
        Estabelecimento estabelecimento,
        Cliente cliente
){}