package ai.agendi.agendador.domain.usuario.controller;

import ai.agendi.agendador.domain.usuario.dto.DadosCadastroAdmin;
import ai.agendi.agendador.domain.usuario.dto.DadosRespostaAdmin;
import ai.agendi.agendador.domain.usuario.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<DadosRespostaAdmin> register(@RequestBody @Valid DadosCadastroAdmin dto) {
        DadosRespostaAdmin admin = adminService.register(dto);
        return ResponseEntity.ok(admin);
    }
}