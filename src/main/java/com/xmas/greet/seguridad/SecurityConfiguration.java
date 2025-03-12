package com.xmas.greet.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.xmas.greet.servicio.UsuarioServicio;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomAuthenticationSuccessHandler successHandler;
    private final UsuarioServicio usuarioServicio;

    public SecurityConfiguration(CustomAuthenticationSuccessHandler successHandler, UsuarioServicio usuarioServicio) {
        this.successHandler = successHandler;
        this.usuarioServicio = usuarioServicio;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(usuarioServicio);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeRequests(authz -> authz
                .requestMatchers(
                    "/css/**", "/js/**", "/img/**", 
                    "/index**", "/registro**", "/registro/usuario**", 
                    "/resultado**", "/recover-password**", 
                    "/reset-password/**", "/recuperar/**", "/catalogo/**","/carrito-pedido/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin()
                .loginPage("/login")
                .successHandler(successHandler)  // Usar el handler inyectado
                .permitAll()
            .and()
            .logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        return http.build();
    }
}
