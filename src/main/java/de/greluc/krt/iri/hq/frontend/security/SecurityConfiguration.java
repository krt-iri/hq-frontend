package de.greluc.krt.iri.hq.frontend.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

  private final KeycloakLogoutHandler keycloakLogoutHandler;

  public SecurityConfiguration(KeycloakLogoutHandler keycloakLogoutHandler) {
    this.keycloakLogoutHandler = keycloakLogoutHandler;
  }

  @Bean
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }

  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
    return authorities -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      var authority = authorities.iterator().next();

      if (authority instanceof OidcUserAuthority oidcUserAuthority) {
        var userInfo = oidcUserAuthority.getUserInfo();

        if (userInfo.hasClaim("realm_access")) {
          var realmAccess = userInfo.getClaimAsMap("realm_access");
          Object rolesObj = realmAccess.get("roles");
          if (rolesObj instanceof Collection<?> rolesCollection) {
            Collection<String> roles = rolesCollection.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList();
            mappedAuthorities.addAll(roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .toList());
          }
        }
      }
      return mappedAuthorities;
    };
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // Configure OAuth2 login and logout
    http.authorizeHttpRequests(authorize -> authorize
        // Allow access to static resources
        .requestMatchers("/images/*.png").permitAll()
        // All other requests require authentication
        //.anyRequest().authenticated()
        ).oauth2Login(oauth2 -> oauth2
            .defaultSuccessUrl("/")
        )
        .logout(logout -> logout
            .addLogoutHandler(keycloakLogoutHandler)
            .logoutSuccessUrl("/")
        );

    super.configure(http);
  }

}
