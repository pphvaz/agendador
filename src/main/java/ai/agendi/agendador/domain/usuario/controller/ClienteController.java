package ai.agendi.agendador.domain.usuario.controller;

import ai.agendi.agendador.domain.usuario.dto.DadosCadastroCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosRespostaCliente;
import ai.agendi.agendador.domain.usuario.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<Page<DadosRespostaCliente>> findAll(Pageable pageable) {
        Page<DadosRespostaCliente> page = clienteService.findAll(pageable);
        if (page.getTotalElements() > 0) {
            return ResponseEntity.ok(page);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosRespostaCliente> buscarClientePorId(@PathVariable("id") Long id) {
        try {
            DadosRespostaCliente dadosResposta = clienteService.buscarClientePorId(id);
            return ResponseEntity.ok(dadosResposta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<DadosRespostaCliente> save(@RequestBody @Valid DadosCadastroCliente dados, UriComponentsBuilder ucb) {
        DadosRespostaCliente resposta = clienteService.save(dados);
        URI locationOfNewCliente = ucb
                .path("clientes/{id}")
                .buildAndExpand(resposta.id())
                .toUri();
        return ResponseEntity.created(locationOfNewCliente).build();
    }
}
