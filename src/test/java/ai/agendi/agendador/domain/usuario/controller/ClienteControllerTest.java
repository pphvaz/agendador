package ai.agendi.agendador.domain.usuario.controller;

import ai.agendi.agendador.domain.usuario.dto.DadosCadastroCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroUsuario;
import ai.agendi.agendador.domain.usuario.dto.DadosRespostaCliente;
import ai.agendi.agendador.domain.usuario.model.Cliente;
import ai.agendi.agendador.domain.usuario.repository.ClienteRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.annotation.PostConstruct;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
class ClienteControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private ClienteRepository clienteRepository;

    @PostConstruct
    public void populateData() {
        if (clienteRepository.count() == 0) {
            DadosCadastroUsuario user1 = new DadosCadastroUsuario("pedro99", "password99", "pedro99@mail.com", "1234567890");
            DadosCadastroCliente cliente1 = new DadosCadastroCliente(user1, "Pedro Vaz", LocalDate.of(1999, 2, 6), "12345678901");

            DadosCadastroUsuario user2 = new DadosCadastroUsuario("pedro100", "password100", "pedro100@mail.com", "1234567891");
            DadosCadastroCliente cliente2 = new DadosCadastroCliente(user2, "João Silva", LocalDate.of(1985, 6, 15), "98765432100");

            DadosCadastroUsuario user3 = new DadosCadastroUsuario("pedro101", "password101", "pedro101@mail.com", "1234567892");
            DadosCadastroCliente cliente3 = new DadosCadastroCliente(user3, "Maria Souza", LocalDate.of(1990, 10, 20), "45678912300");

            // Save them directly in your repository
            clienteRepository.save(new Cliente(cliente1));
            clienteRepository.save(new Cliente(cliente2));
            clienteRepository.save(new Cliente(cliente3));
        }
    }

    @Test
    void shouldReturnADadosRespostaClienteWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/clientes/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        String nome = documentContext.read("$.nome");
        String email = documentContext.read("$.email");
        assertThat(id).isEqualTo(1);
        assertThat(nome).isEqualTo("Pedro Vaz");
        assertThat(email).isEqualTo("pedro99@mail.com");

    }

    @Test
    void shouldNotReturnADadosRespostaClienteWithAnUnkownId() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/clientes/999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Transactional
    void shouldCreateANewClienteWithValidInformation() {
        DadosCadastroUsuario newUsuario = new DadosCadastroUsuario("pedro999","12345678","pedro@mail.com","12991261390");
        DadosCadastroCliente newCliente = new DadosCadastroCliente(newUsuario,"Pedro", LocalDate.of(1999,02,06),"48344088097");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewCliente = response.getHeaders().getLocation();
        assertThat(locationOfNewCliente).isNotNull();

        ResponseEntity<DadosRespostaCliente> getResponse = restTemplate
                .getForEntity(locationOfNewCliente, DadosRespostaCliente.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DadosRespostaCliente cliente = getResponse.getBody();
        assertThat(cliente).isNotNull();
        assertThat(cliente.nome()).isEqualTo("Pedro");
        assertThat(cliente.email()).isEqualTo("pedro@mail.com");
    }

    @Test
    void shouldNotCreateANewClienteWithExistingLogin() {
        DadosCadastroUsuario newUsuario = new DadosCadastroUsuario("pedro99","12345678","pedro@mail.com","12991261390");
        DadosCadastroCliente newCliente = new DadosCadastroCliente(newUsuario,"Pedro", LocalDate.of(1999,02,06),"48344088097");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotCreateANewClienteWithExistingEmail() {
        DadosCadastroUsuario newUsuario = new DadosCadastroUsuario("pedro99","12345678","pedro99@mail.com","12991261390");
        DadosCadastroCliente newCliente = new DadosCadastroCliente(newUsuario,"Pedro", LocalDate.of(1999,02,06),"48344088097");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotCreateANewClienteWithExistingCpf() {
        DadosCadastroUsuario newUsuario = new DadosCadastroUsuario("pedro99","12345678","pedro99@mail.com","12991261390");
        DadosCadastroCliente newCliente = new DadosCadastroCliente(newUsuario,"Pedro", LocalDate.of(1999,02,06),"47450456893");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotCreateANewClienteWithExistingCelular() {
        DadosCadastroUsuario newUsuario = new DadosCadastroUsuario("pedro","12349nk79","pedro@mail.com","1234567890");
        DadosCadastroCliente newCliente = new DadosCadastroCliente(newUsuario,"Pedro", LocalDate.of(1999,02,06),"48344088097");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotCreateANewClienteWithInvalidTooShortPassword() {
        DadosCadastroUsuario newUsuario = new DadosCadastroUsuario("pedro","1234","pedro@mail.com","12991261390");
        DadosCadastroCliente newCliente = new DadosCadastroCliente(newUsuario,"Pedro", LocalDate.of(1999,02,06),"98765432110");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    void shouldReturnAllDadosRespostaClientesWhenListIsRequested() {}

    void shouldNotReturnAClienteWhenUsingBadCredentials() {

        // assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    void shouldRejectUsersWhoAreNotOwnersOfTheIdSearched() {}

    void shouldUpdateAnExistingCliente() {}

    void shouldNotUpdateAClienteThatDoesNotExist() {}

    void shouldNotUpdateAClienteThatDoesntOwnTheProfile() {}

    void shouldLogicDeleteAnExistingCliente() {}

    void shouldNotDeleteAClienteThatDoesNotExist() {}

    void shouldNotAllowDeletionOfClienteThatDoesNotOwnTheProfile() {}

}