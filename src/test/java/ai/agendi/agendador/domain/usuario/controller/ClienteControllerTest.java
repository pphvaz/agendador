package ai.agendi.agendador.domain.usuario.controller;

import ai.agendi.agendador.domain.usuario.dto.DadosAtualizacaoCliente;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

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
            Cliente cliente1 = new Cliente(
                    "cliente1@mail.com",
                    "doasbodibsaiudboasbdoasbndnasp",
                    "1234567890",
                    "Carlos Silva",
                    "11111111111",
                    LocalDate.of(1990, 5, 15)
            );

            Cliente cliente2 = new Cliente(
                    "cliente2@mail.com",
                    "12b3io1bebowbnsaodboasads12",
                    "0987654321",
                    "Ana Oliveira",
                    "22222222222",
                    LocalDate.of(1985, 8, 22)
            );

            Cliente cliente3 = new Cliente(
                    "cliente3@mail.com",
                    "1b2oi12oinoienwp12inepn1p2nep",
                    "9876543210",
                    "Roberto Souza",
                    "33333333333",
                    LocalDate.of(2000, 12, 1)
            );

            // Save them directly in your repository
            clienteRepository.save(cliente1);
            clienteRepository.save(cliente2);
            clienteRepository.save(cliente3);
        }
    }

    @Test
    void shouldReturnADadosRespostaClienteWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/clientes/2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        String nome = documentContext.read("$.nome");
        String email = documentContext.read("$.email");
        assertThat(id).isEqualTo(2);
        assertThat(nome).isEqualTo("João Silva");
        assertThat(email).isEqualTo("pedro100@mail.com");
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
        DadosCadastroCliente newCliente = new DadosCadastroCliente("pedro@mail.com","pedro999","12991261390","Pedro", LocalDate.of(1999,02,06),"48344088097");
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
    void shouldNotCreateANewClienteWithExistingEmail() {
        DadosCadastroCliente newCliente = new DadosCadastroCliente("pedro99@mail.com","pedro999","12991261390","Pedro", LocalDate.of(1999,02,06),"48344088097");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotCreateANewClienteWithExistingCpf() {
        DadosCadastroCliente newCliente = new DadosCadastroCliente("pedro929@mail.com","pedro999","12991261390","Pedro", LocalDate.of(1999,02,06),"11111111111");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotCreateANewClienteWithExistingCelular() {
        DadosCadastroCliente newCliente = new DadosCadastroCliente("ped239@mail.com","pedro999","1234567890","Pedro", LocalDate.of(1999,02,06),"11113211111");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotCreateANewClienteWithInvalidTooShortPassword() {
        DadosCadastroCliente newCliente = new DadosCadastroCliente("pe321239@mail.com","o999","1234532890","Pedro", LocalDate.of(1999,02,06),"11113211111");
        ResponseEntity<Void> response = restTemplate
                .postForEntity("/clientes", newCliente, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotDeleteByUnknownId(){
        ResponseEntity<Void> response = restTemplate
                .exchange("/clientes/delete/99999", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Transactional
    void shouldDeleteById(){
        ResponseEntity<Void> response = restTemplate
                .exchange("/clientes/delete/1", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Retrieve the entity by ID
        ResponseEntity<DadosRespostaCliente> getResponse = restTemplate
                .getForEntity("/clientes/1", DadosRespostaCliente.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotUpdateAClienteThatDoesntExists(){
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
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldUpdateAnExistingClienteName() {
        // Create the DadosAtualizacaoCliente with a non-existent id
        String randomName = generateRandomName();
        DadosAtualizacaoCliente dadosAtualizacao = new DadosAtualizacaoCliente();
        dadosAtualizacao.setId(3L);
        dadosAtualizacao.setNovoNome(randomName);

        // Perform the PUT request with the dadosAtualizacao object in the body
        ResponseEntity<DadosRespostaCliente> response = restTemplate.exchange(
                "/clientes/update",
                HttpMethod.PUT,
                new HttpEntity<>(dadosAtualizacao),
                DadosRespostaCliente.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().nome()).isEqualTo(randomName);
    }

    private String generateRandomName() {
        String[] firstNames = {"Pedro", "Maria", "João", "Ana", "Carlos", "Julia", "Lucas", "Fernanda", "Gabriel", "Beatriz"};
        String[] lastNames = {"Silva", "Santos", "Oliveira", "Souza", "Pereira", "Costa", "Almeida", "Ferreira", "Ribeiro", "Lima"};

        Random random = new Random();

        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];

        return firstName + " " + lastName;
    }

    void shouldReturnAllDadosRespostaClientesWhenListIsRequested() {}

    void shouldNotReturnAClienteWhenUsingBadCredentials() {

        // assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    void shouldRejectUsersWhoAreNotOwnersOfTheIdSearched() {}

    void shouldNotUpdateAClienteThatDoesNotExist() {}

    void shouldNotUpdateAClienteThatDoesntOwnTheProfile() {}

    void shouldNotAllowDeletionOfClienteThatDoesNotOwnTheProfile() {}

}