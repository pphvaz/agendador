package ai.agendi.agendador.utils;

import ai.agendi.agendador.domain.usuario.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import java.util.Map;

public class JwtTestUtil {

    private static final String TEST_ISSUER = "Agendador Application";

    private final String secret;

    public JwtTestUtil() {
        this.secret = "2kjeb12bj1k2123b2bo123h12oabds";
    }

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(TEST_ISSUER)
                    .withSubject(usuario.getEmail())
                    .withClaim("nome", usuario.getNome())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String gerarToken(String email, Map<String, String> claims) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            var builder = JWT.create()
                    .withIssuer(TEST_ISSUER)
                    .withSubject(email);

            if (claims != null) {
                claims.forEach(builder::withClaim);
            }

            return builder.sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }
}