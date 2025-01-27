package ai.agendi.agendador.domain.usuario.controller;

import ai.agendi.agendador.domain.usuario.dto.*;
import ai.agendi.agendador.domain.usuario.enums.Perfil;
import ai.agendi.agendador.domain.usuario.model.Cliente;
import ai.agendi.agendador.domain.usuario.model.Usuario;
import ai.agendi.agendador.domain.usuario.repository.ClienteRepository;
import ai.agendi.agendador.domain.usuario.repository.UsuarioRepository;
import ai.agendi.agendador.utils.JwtTestUtil;
import ai.agendi.agendador.utils.TestUtilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
class ClienteControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final JwtTestUtil jwtUtil = new JwtTestUtil();

    private final TestUtilities utils = new TestUtilities();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void populateData() {

        Set<Perfil> perfis = new HashSet<>();
        perfis.add(Perfil.ROLE_CLIENTE);
        if (clienteRepository.count() == 0) {
            DadosCadastroCliente dados1 = new DadosCadastroCliente(
                    "cliente1@mail.com",
                    "cliente1",
                    "1234567890",
                    "Carlos Silva",
                    LocalDate.of(1990, 5, 15),
                    "11111111111"
            );

            DadosCadastroUsuario usuario1 = new DadosCadastroUsuario(
                    dados1.email(), dados1.senha(), dados1.celular(), dados1.nome(), dados1.dataNascimento(), perfis);

            DadosCadastroCliente dados2 = new DadosCadastroCliente(
                    "cliente2@mail.com",
                    "cliente2",
                    "1234567891",
                    "Ana Oliveira",
                    LocalDate.of(1994, 2, 14),
                    "22222222222"
            );

            DadosCadastroUsuario usuario2 = new DadosCadastroUsuario(
                    dados2.email(), dados2.senha(), dados2.celular(), dados2.nome(), dados2.dataNascimento(), perfis);

            DadosCadastroCliente dados3 = new DadosCadastroCliente(
                    "cliente3@mail.com",
                    "cliente3",
                    "1234567892",
                    "Roberto Souza",
                    LocalDate.of(2000, 12, 1),
                    "47450456893"
            );

            DadosCadastroUsuario usuario3 = new DadosCadastroUsuario(
                    dados3.email(), dados3.senha(), dados3.celular(), dados3.nome(), dados3.dataNascimento(), perfis);


            Cliente cliente1 = new Cliente(usuario1, dados1);
            Cliente cliente2 = new Cliente(usuario2, dados2);
            Cliente cliente3 = new Cliente(usuario3, dados3);

            // Save them directly in your repository
            clienteRepository.save(cliente1);
            clienteRepository.save(cliente2);
            clienteRepository.save(cliente3);

            Usuario admin = new Usuario();
            String encryptedPassword = passwordEncoder.encode("password");

            admin.setEmail("admin@admin.com");
            admin.setNome("Admin");
            admin.setCelular("12991232910");
            admin.setDataNascimento(LocalDate.of(1999,02,01));
            admin.setSenha(encryptedPassword);
            admin.setAtivo(true);
            admin.setDiaDeCadastro(LocalDateTime.now());

            Set<Perfil> perfisAdmin = new HashSet<>();
            perfisAdmin.add(Perfil.ROLE_ADMIN);
            admin.setPerfis(perfisAdmin);

            usuarioRepository.save(admin);
        }

    }

    @Test
    void shouldReturnADadosRespostaClienteWhenDataIsSaved() {
        String testToken = jwtUtil.gerarToken("cliente3@mail.com", Map.of("nome", "Roberto Souza"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/clientes/3", HttpMethod.GET, entity, String.class
        );
        // Assert the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String nome = documentContext.read("$.nome");
        String email = documentContext.read("$.email");
        assertThat(nome).isEqualTo("Roberto Souza");
        assertThat(email).isEqualTo("cliente3@mail.com");
    }

    @Test
    void shouldNotReturnADadosRespostaClienteWithUnkownId() {
        String testToken = jwtUtil.gerarToken("cliente2@mail.com", Map.of("nome", "Ana Oliveira"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/clientes/999", HttpMethod.GET, entity, String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldDenyAccessForUnauthorizedCliente() {
        String testToken = jwtUtil.gerarToken("cliente2@mail.com", Map.of("nome", "Ana Oliveira"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/clientes/3", HttpMethod.GET, entity, String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @Transactional
    void shouldCreateANewClienteWithValidInformation() {
        DadosCadastroCliente newCliente = new DadosCadastroCliente("pedro@mail.com","pedro999","12991261390","Pedro", LocalDate.of(1999,02,06),"48344088097");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes/register", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewCliente = response.getHeaders().getLocation();
        assertThat(locationOfNewCliente).isNotNull();

        String testToken = jwtUtil.gerarToken("pedro@mail.com", Map.of("nome", "Pedro"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);
        HttpEntity<DadosGeraisRespostaCliente> entity = new HttpEntity<>(headers);
        ResponseEntity<DadosGeraisRespostaCliente> getResponse = restTemplate.exchange(
                locationOfNewCliente, HttpMethod.GET, entity, DadosGeraisRespostaCliente.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DadosGeraisRespostaCliente cliente = getResponse.getBody();
        assertThat(cliente).isNotNull();
        assertThat(cliente.nome()).isEqualTo("Pedro");
        assertThat(cliente.email()).isEqualTo("pedro@mail.com");
    }

    @Test
    void shouldNotCreateANewClienteWithExistingEmail() {
        DadosCadastroCliente newCliente = new DadosCadastroCliente("cliente1@mail.com","pedro999","12912344390","Pedro Alves", LocalDate.of(1999,02,06),"60072949007");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes/register", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotCreateANewClienteWithExistingCpf() {
        DadosCadastroCliente newCliente = new DadosCadastroCliente("pedro929@mail.com","pedro999","12991261390","Pedro", LocalDate.of(1999,02,06),"47450456893");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes/register", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotCreateANewClienteWithExistingCelular() {
        DadosCadastroCliente newCliente = new DadosCadastroCliente("p239@mail.com","pedro999","1234567890","Teste", LocalDate.of(1999,02,06),"47450456893");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes/register", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotCreateANewClienteWithInvalidTooShortPassword() {
        DadosCadastroCliente newCliente = new DadosCadastroCliente("pe321239@mail.com","o999","123322890","Pedro", LocalDate.of(1999,02,06),"11113211111");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes/register", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotDeleteUnauthenticatedClient(){
        ResponseEntity<Void> response = restTemplate
                .exchange("/clientes/delete/99999", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldNotDeleteClienteThatIsNotTheUserAuthenticated(){

        String testToken = jwtUtil.gerarToken("cliente2@mail.com", Map.of("nome", "Ana Oliveira"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "/clientes/delete/3", HttpMethod.DELETE, entity, Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @Transactional
    void shouldDeleteById(){
        String testToken = jwtUtil.gerarToken("cliente2@mail.com", Map.of("nome", "Ana Oliveira"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "/clientes/delete/2", HttpMethod.DELETE, entity, Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void shouldNotUpdateAClienteThatDoesntExists(){
        String testToken = jwtUtil.gerarToken("cliente2@mail.com", Map.of("nome", "Ana Oliveira"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Create the DadosAtualizacaoCliente with a non-existent id
        DadosAtualizacaoCliente dadosAtualizacao = new DadosAtualizacaoCliente();
        dadosAtualizacao.setId(99999L);
        dadosAtualizacao.setNovoNome("Novo Nome");

        // Perform the PUT request with the dadosAtualizacao object in the body
        ResponseEntity<Void> response = restTemplate.exchange(
                "/clientes/update",
                HttpMethod.PUT,
                new HttpEntity<>(dadosAtualizacao),
                Void.class
        );

        // Assert that the status code is NOT_FOUND
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldUpdateAnExistingClienteName() throws JsonProcessingException {

        // Create the DadosAtualizacaoCliente with a non-existent id
        String randomName = utils.generateRandomName();
        DadosAtualizacaoCliente dadosAtualizacao = new DadosAtualizacaoCliente();
        dadosAtualizacao.setId(1L);
        dadosAtualizacao.setNovoNome(randomName);

        String testToken = jwtUtil.gerarToken("cliente1@mail.com", Map.of("nome", "Ana Oliveira"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);

        HttpEntity<DadosAtualizacaoCliente> entity = new HttpEntity<>(dadosAtualizacao, headers);

        // Perform the PUT request with the dadosAtualizacao object in the body
        ResponseEntity<DadosPessoaisRespostaCliente> response = restTemplate.exchange(
                "/clientes/update",
                HttpMethod.PUT,
                entity,
                DadosPessoaisRespostaCliente.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().nome()).isEqualTo(randomName);
    }

    @Test
    void shouldNotReturnAllDadosRespostaClientesForANonAdminUser() {
        String testToken = jwtUtil.gerarToken("cliente2@mail.com", Map.of("nome", "Ana Oliveira"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/clientes", HttpMethod.GET, entity, String.class
        );
        // Assert the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}