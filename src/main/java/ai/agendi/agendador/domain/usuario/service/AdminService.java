package ai.agendi.agendador.domain.usuario.service;

import ai.agendi.agendador.domain.usuario.dto.DadosCadastroAdmin;
import ai.agendi.agendador.domain.usuario.dto.DadosRespostaAdmin;
import ai.agendi.agendador.domain.usuario.enums.Perfil;
import ai.agendi.agendador.domain.usuario.model.Usuario;
import ai.agendi.agendador.domain.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DadosRespostaAdmin register(DadosCadastroAdmin admin) {
        Usuario usuario = new Usuario();
        String encryptedPassword = passwordEncoder.encode(admin.senha());

        usuario.setEmail(admin.email());
        usuario.setNome(admin.nome());
        usuario.setCelular(admin.celular());
        usuario.setDataNascimento(admin.dataNascimento());
        usuario.setSenha(encryptedPassword);
        usuario.setAtivo(true);
        usuario.setDiaDeCadastro(LocalDateTime.now());

        Set<Perfil> perfis = new HashSet<>();
        perfis.add(Perfil.ROLE_ADMIN);
        usuario.setPerfis(perfis);

        usuarioRepository.save(usuario);

        return new DadosRespostaAdmin(usuario);
    }
}
