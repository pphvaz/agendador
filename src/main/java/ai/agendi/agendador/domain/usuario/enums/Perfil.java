package ai.agendi.agendador.domain.usuario.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Perfil implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_MANAGER,
    ROLE_OWNER,
    ROLE_CLIENTE,
    ROLE_PRESTADOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
