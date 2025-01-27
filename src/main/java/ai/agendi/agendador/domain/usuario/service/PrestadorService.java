package ai.agendi.agendador.domain.usuario.service;

import ai.agendi.agendador.domain.estabelecimento.model.Estabelecimento;
import ai.agendi.agendador.domain.usuario.dto.*;
import ai.agendi.agendador.domain.usuario.enums.Perfil;
import ai.agendi.agendador.domain.usuario.model.Cliente;
import ai.agendi.agendador.domain.usuario.model.Endereco;
import ai.agendi.agendador.domain.usuario.model.Prestador;
import ai.agendi.agendador.domain.usuario.model.Usuario;
import ai.agendi.agendador.domain.usuario.repository.*;
import ai.agendi.agendador.domain.usuario.usecase.CpfValidation;
import ai.agendi.agendador.infra.exceptions.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
public class PrestadorService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PrestadorRepository prestadorRepository;
    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    public DadosGeraisRespostaCliente searchClienteByCpfOrCelularOrEmail(String query, Authentication authentication) {
        Cliente authenticated = (Cliente) authentication.getPrincipal();
        Cliente cliente;
        if (isEmail(query)) {
            cliente = clienteRepository.findByEmailAndAtivoTrue(query)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
        } else if (isCpf(query)) {
            cliente = clienteRepository.findByCpfAndAtivoTrue(query)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
        } else {
            cliente = clienteRepository.findByCelularAndAtivoTrue(query)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
        }
        return new DadosGeraisRespostaCliente(cliente);
    }

    private boolean isEmail(String query) {
        return query.contains("@");
    }

    private boolean isCpf(String query) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validateValue(CpfValidation.class, "cpf", query).isEmpty();
    }

    @Transactional
    public DadosRespostaPrestador register(DadosCadastroPrestador dadosPrestador) {
        Prestador prestador;
        Estabelecimento estabelecimento;
        Endereco endereco;
        var dadosUsuarioParaCadastro = criarObjetoDadosCadastroUsuario(dadosPrestador);

        if (dadosPrestador.getEstabelecimentoId() == null && dadosPrestador.getIsOwner()) {
            estabelecimento = registrarNovoEstabelecimento(dadosPrestador.getDadosEstabelecimento());
            endereco = registrarNovoEndereco(dadosPrestador.getDadosEstabelecimento().getEndereco(), estabelecimento);
        } else if (dadosPrestador.getEstabelecimentoId() != null) {
            estabelecimento = estabelecimentoRepository.getReferenceById(dadosPrestador.getEstabelecimentoId());
        } else {
            throw new ValidacaoException("Não foi possível registrar ou encontrar o estabelecimento, verifique os dados.");
        }
        prestador = registrarNovoPrestador(dadosPrestador, dadosUsuarioParaCadastro, estabelecimento);
        if (estabelecimento.getProprietario() == null) {
            estabelecimento.setProprietario(prestador);
        }
        estabelecimento.getPrestadores().add(prestador);
        estabelecimentoRepository.save(estabelecimento);
        return new DadosRespostaPrestador(prestador);
    }

    private Endereco registrarNovoEndereco(DadosCadastroEndereco dadosEndereco, Estabelecimento estabelecimento) {
        return enderecoRepository.save(new Endereco(dadosEndereco));
    }

    private Estabelecimento registrarNovoEstabelecimento(DadosCadastroEstabelecimento dadosEstabelecimento) {
        return estabelecimentoRepository.save(new Estabelecimento(dadosEstabelecimento));
    }

    private DadosCadastroUsuario criarObjetoDadosCadastroUsuario(DadosCadastroPrestador dadosPrestador) {
        String senhaCriptografada = passwordEncoder.encode(dadosPrestador.getSenha());

        var perfis = new HashSet<Perfil>();
        perfis.add(Perfil.ROLE_PRESTADOR);

        if (dadosPrestador.getIsManager()) {
            perfis.add(Perfil.ROLE_MANAGER);
        }
        if (dadosPrestador.getIsOwner()) {
            perfis.add(Perfil.ROLE_OWNER);
        }

        DadosCadastroUsuario dadosUsuario = new DadosCadastroUsuario(
                dadosPrestador.getEmail(),
                senhaCriptografada,
                dadosPrestador.getCelular(),
                dadosPrestador.getNome(),
                dadosPrestador.getDataNascimento(),
                perfis
        );
        return dadosUsuario;
    }

    private Prestador registrarNovoPrestador(DadosCadastroPrestador dadosPrestador,DadosCadastroUsuario dadosUsuario, Estabelecimento estabelecimento) {
        var prestador = new Prestador(dadosPrestador, dadosUsuario, estabelecimento);
        return prestadorRepository.save(prestador);
    }

    public DadosRespostaPrestador findById(Long id, Authentication authentication) {
        var prestador = prestadorRepository.findByIdAndAtivoTrue(id);
        if (prestador == null) {
            throw new EntityNotFoundException(String.format("Não foi possível encontrar prestador com o id: %s", id));
        }
        return new DadosRespostaPrestador(prestador);
    }

    public void logicallyDeleteByAtivo(Long id, Authentication authentication) {
        // If the authenticated user is not the owner of current Estabelecimento, not allow deleting.
        Prestador authenticated = (Prestador) authentication.getPrincipal();
        Prestador toBeDeleted = prestadorRepository.findByIdAndAtivoTrue(id);

        if (!authenticated.getEstabelecimento().getId().equals(toBeDeleted.getEstabelecimento().getId())) {
            throw new ValidacaoException("You are not allowed to delete");
        }
        prestadorRepository.logicallyDeleteById(toBeDeleted.getId());
    }

    public Page<DadosRespostaPrestador> findAll(Pageable pageable) {
        return prestadorRepository.findAll(pageable)
                .map(DadosRespostaPrestador::new);
    }
}