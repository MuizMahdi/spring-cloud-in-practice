package net.jaggerwang.scip.gateway.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "spring.security.oauth2.enabled", havingValue = "false",
        matchIfMissing = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@EnableWebFluxSecurity
public class SecurityConfig {
    private ObjectMapper objectMapper;

    public SecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private Mono<Void> responseJson(ServerWebExchange exchange, HttpStatus status, RootDto data) {
        var response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        var body = new byte[0];
        try {
            body = objectMapper.writeValueAsBytes(data);
        } catch (IOException e) {
        }
        return response.writeWith(Flux.just(response.bufferFactory().wrap(body)));
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, exception) ->
                                responseJson(exchange, HttpStatus.UNAUTHORIZED,
                                        new RootDto("unauthenticated", "未认证")))
                        .accessDeniedHandler((exchange, accessDeniedException) ->
                                responseJson(exchange, HttpStatus.FORBIDDEN,
                                        new RootDto("unauthorized", "未授权")))
                )
                .authorizeExchange(authorizeExchange -> authorizeExchange
                        .pathMatchers("/favicon.ico", "/*/actuator/**", "/user/register",
                                "/user/login", "/user/logout", "/user/logged").permitAll()
                        .anyExchange().authenticated())
                .build();
    }
}
