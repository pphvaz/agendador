package ai.agendi.agendador.domain.usuario.controller;

import ai.agendi.agendador.domain.estabelecimento.model.Estabelecimento;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroEndereco;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroEstabelecimento;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroPrestador;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroUsuario;
import ai.agendi.agendador.domain.usuario.enums.Perfil;
import ai.agendi.agendador.domain.usuario.model.Endereco;
import ai.agendi.agendador.domain.usuario.model.Prestador;
import ai.agendi.agendador.domain.usuario.repository.EnderecoRepository;
import ai.agendi.agendador.domain.usuario.repository.EstabelecimentoRepository;
import ai.agendi.agendador.domain.usuario.repository.PrestadorRepository;
import ai.agendi.agendador.utils.JwtTestUtil;
import ai.agendi.agendador.utils.TestUtilities;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
class PrestadorControllerTest {

    @Autowired
    private PrestadorController prestadorController;

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Autowired
    TestRestTemplate restTemplate;
    private final JwtTestUtil jwtUtil = new JwtTestUtil();
    private final TestUtilities utils = new TestUtilities();

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;

    @PostConstruct
    void populateData() {
        if (prestadorRepository.count() == 0) {
            Estabelecimento estabelecimento = new Estabelecimento(new DadosCadastroEstabelecimento(
                    "Teste",
                    null,
                    null));
            var estabelecimentoCriado = estabelecimentoRepository.save(estabelecimento);
            enderecoRepository.save(new Endereco(new DadosCadastroEndereco(
                    "12312-231",
                    "Rua das Oliveiras",
                    "123",
                    "Floradas",
                    "Floripa",
                    "PR",
                    null,
                    estabelecimentoCriado,
                    null)));
            var perfilPrestador = Set.of(Perfil.ROLE_PRESTADOR);
            var perfilManager = Set.of(Perfil.ROLE_MANAGER);

            var dados1 = new DadosCadastroPrestador(estabelecimentoCriado.getId(), "prestador1@gmail.com",passwordEncoder.encode("12345678"),"12991261390","Jão Lucas",LocalDate.of(1990,5,14),5,false,false,true,null);
            var dados2 = new DadosCadastroPrestador(estabelecimentoCriado.getId(), "manager1@gmail.com",passwordEncoder.encode("12345678"),"12891162391","Antonio Marcos", LocalDate.of(1994,2,1),10,true,false,true,null);
            var usuario1 = new DadosCadastroUsuario(dados1.getEmail(), dados1.getSenha(), dados1.getCelular(), dados1.getNome(),dados1.getDataNascimento(), perfilPrestador);
            var usuario2 = new DadosCadastroUsuario(dados2.getEmail(), dados2.getSenha(), dados2.getCelular(), dados2.getNome(),dados2.getDataNascimento(), perfilManager);

            Prestador prestador1 = new Prestador(dados1,usuario1,estabelecimentoCriado);
            Prestador manager1 = new Prestador(dados2,usuario2,estabelecimentoCriado);
            prestadorRepository.saveAll(Arrays.asList(prestador1,manager1));
        }
    }

    @Test
    void shouldNotReturnAClienteIfNotPrestador(){
        String testToken = jwtUtil.gerarToken("cliente1@mail.com", Map.of("nome", "Qualquer Souza"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/prestadores/buscar-cliente", HttpMethod.GET, entity, String.class
        );
        // Assert the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}