package ai.agendi.agendador.domain.usuario.controller;

import ai.agendi.agendador.domain.usuario.dto.DadosAtualizacaoCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosGeraisRespostaCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosPessoaisRespostaCliente;
import ai.agendi.agendador.domain.usuario.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/register")
    public ResponseEntity<DadosPessoaisRespostaCliente> register(@RequestBody @Valid DadosCadastroCliente dados, UriComponentsBuilder ucb) {
        DadosPessoaisRespostaCliente resposta = clienteService.register(dados);
        URI locationOfNewCliente = ucb
                .path("clientes/{id}")
                .buildAndExpand(resposta.id())
                .toUri();
        return ResponseEntity.created(locationOfNewCliente).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosGeraisRespostaCliente> findById(@PathVariable("id") Long id, Authentication authentication){
            DadosGeraisRespostaCliente dadosResposta = clienteService.buscarClientePorId(id, authentication);
            return ResponseEntity.ok(dadosResposta);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<DadosGeraisRespostaCliente>> findAll(Pageable pageable, Authentication authentication) {
        Page<DadosGeraisRespostaCliente> page = clienteService.findAll(pageable);
        if (page.getTotalElements() > 0) {
            return ResponseEntity.ok(page);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Apenas admin ou o proprio cliente podem deletar o usuario Cliente.
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, Authentication authentication) {
        clienteService.deleteById(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("update")
    public ResponseEntity<DadosPessoaisRespostaCliente> update(@RequestBody @Valid DadosAtualizacaoCliente dadosAtualizacao, Authentication authentication) {
        return ResponseEntity.ok(clienteService.update(dadosAtualizacao, authentication));
    }
}