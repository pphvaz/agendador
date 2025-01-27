package ai.agendi.agendador.domain.usuario.controller;

import ai.agendi.agendador.domain.usuario.dto.DadosCadastroPrestador;
import ai.agendi.agendador.domain.usuario.dto.DadosGeraisRespostaCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosRespostaPrestador;
import ai.agendi.agendador.domain.usuario.service.PrestadorService;
import jakarta.annotation.security.RolesAllowed;
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
@RequestMapping("prestadores")
public class PrestadorController {

    @Autowired
    private PrestadorService prestadorService;

    @GetMapping("/buscar-cliente")
    @RolesAllowed({"ROLE_PRESTADOR", "ROLE_MANAGER","ROLE_OWNER", "ROLE_ADMIN"})
    public ResponseEntity<DadosGeraisRespostaCliente> searchClienteByCpfOrCelularOrEmail(@RequestParam String query, Authentication authentication) {
        DadosGeraisRespostaCliente cliente = prestadorService.searchClienteByCpfOrCelularOrEmail(query, authentication);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping("/register")
    public ResponseEntity<DadosRespostaPrestador> register(@RequestBody @Valid DadosCadastroPrestador dados, UriComponentsBuilder ucb) {
        DadosRespostaPrestador resposta = prestadorService.register(dados);
        URI localizacao = ucb
                .path("prestadores/{id}")
                .buildAndExpand(resposta.id())
                .toUri();
        return ResponseEntity.created(localizacao).body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosRespostaPrestador> searchPrestadorById(@PathVariable Long id, Authentication authentication) {
        DadosRespostaPrestador prestador = prestadorService.findById(id, authentication);
        return ResponseEntity.ok(prestador);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({"ROLE_OWNER", "ROLE_ADMIN"})
    public ResponseEntity<Void> deletePrestadorById(@PathVariable Long id, Authentication authentication) {
        prestadorService.logicallyDeleteByAtivo(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<DadosRespostaPrestador>> findAll(Pageable pageable, Authentication authentication) {
        Page<DadosRespostaPrestador> page = prestadorService.findAll(pageable);
        if (page.getTotalElements() > 0) {
            return ResponseEntity.ok(page);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}