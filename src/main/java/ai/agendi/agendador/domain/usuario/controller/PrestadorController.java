package ai.agendi.agendador.domain.usuario.controller;

import ai.agendi.agendador.domain.usuario.dto.DadosGeraisRespostaCliente;
import ai.agendi.agendador.domain.usuario.service.PrestadorService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("prestadores")
public class PrestadorController {

    @Autowired
    private PrestadorService prestadorService;


    @GetMapping("/buscar-cliente")
    @RolesAllowed("ROLE_PRESTADOR")
    public ResponseEntity<DadosGeraisRespostaCliente> searchClienteByCpfOrCelularOrEmail(@RequestParam String query, Authentication authentication) {
        DadosGeraisRespostaCliente cliente = prestadorService.searchClienteByCpfOrCelularOrEmail(query, authentication);
        return ResponseEntity.ok(cliente);
    }
}