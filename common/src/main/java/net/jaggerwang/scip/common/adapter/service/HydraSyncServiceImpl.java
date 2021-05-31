package net.jaggerwang.scip.common.adapter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.auth.*;
import net.jaggerwang.scip.common.usecase.port.service.HydraSyncService;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class HydraSyncServiceImpl extends SyncService implements HydraSyncService {
    protected ObjectMapper objectMapper;

    public HydraSyncServiceImpl(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
                                ObjectMapper objectMapper) {
        super(restTemplate, cbFactory);
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("slow");
    }

    @Override
    public LoginRequestDto getLoginRequest(String challenge) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/login")
                .queryParam("login_challenge", challenge)
                .build();
        return get(uri, null, LoginRequestDto.class, null).getBody();
    }

    @Override
    public String directlyAcceptLoginRequest(String challenge, LoginAcceptDto accept) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/login/accept")
                .queryParam("login_challenge", challenge)
                .build();
        var requestEntity = new HttpEntity<>(Map.of("subject", accept.getSubject()));
        return (String) put(uri, requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}, null)
                .getBody()
                .get("redirect_to");
    }

    @Override
    public String acceptLoginRequest(String challenge, LoginAcceptDto accept) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/login/accept")
                .queryParam("login_challenge", challenge)
                .build();
        var requestEntity = new HttpEntity<>(Map.of("subject", accept.getSubject(),
                "remember", accept.getRemember(), "remember_for", accept.getRememberFor()));
        return (String) put(uri, requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}, null)
                .getBody()
                .get("redirect_to");
    }

    @Override
    public String rejectLoginRequest(String challenge, LoginRejectDto reject) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/login/reject")
                .queryParam("login_challenge", challenge)
                .build();
        var requestEntity = new HttpEntity<>(Map.of("error", reject.getError(),
                "error_description", reject.getErrorDescription()));
        return (String) put(uri, requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}, null)
                .getBody()
                .get("redirect_to");
    }

    @Override
    public ConsentRequestDto getConsentRequest(String challenge) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/consent")
                .queryParam("consent_challenge", challenge)
                .build();
        return get(uri, null, ConsentRequestDto.class, null)
                .getBody();
    }

    @Override
    public String directlyAcceptConsentRequest(String challenge, ConsentAcceptDto accept) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/consent/accept")
                .queryParam("consent_challenge", challenge)
                .build();
        var requestEntity = new HttpEntity<>(Map.of("grant_scope", accept.getGrantScope(),
                "grant_access_token_audience", accept.getGrantAccessTokenAudience(),
                "session", accept.getSession()));
        return (String) put(uri, requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}, null)
                .getBody()
                .get("redirect_to");
    }

    @Override
    public String acceptConsentRequest(String challenge, ConsentAcceptDto accept) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/consent/accept")
                .queryParam("consent_challenge", challenge)
                .build();
        var requestEntity = new HttpEntity<>(Map.of("grant_scope", accept.getGrantScope(),
                "grant_access_token_audience", accept.getGrantAccessTokenAudience(),
                "session", accept.getSession(), "remember", accept.getRemember(),
                "remember_for", accept.getRememberFor()));
        return (String) put(uri, requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}, null)
                .getBody()
                .get("redirect_to");
    }

    @Override
    public String rejectConsentRequest(String challenge, ConsentRejectDto reject) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/consent/reject")
                .queryParam("consent_challenge", challenge)
                .build();
        var requestEntity = new HttpEntity<>(Map.of("error", reject.getError(),
                "error_description", reject.getErrorDescription()));
        return (String) put(uri, requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}, null)
                .getBody()
                .get("redirect_to");
    }

    @Override
    public LogoutRequestDto getLogoutRequest(String challenge) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/logout")
                .queryParam("logout_challenge", challenge)
                .build();
        return get(uri, null, LogoutRequestDto.class, null)
                .getBody();
    }

    @Override
    public String acceptLogoutRequest(String challenge) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/logout/accept")
                .queryParam("logout_challenge", challenge)
                .build();
        return (String) put(uri, null, new ParameterizedTypeReference<Map<String, Object>>() {},
                null)
                .getBody()
                .get("redirect_to");
    }

    @Override
    public Void rejectLogoutRequest(String challenge) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/logout/reject")
                .queryParam("logout_challenge", challenge)
                .build();
        return put(uri, null, Void.class, null)
                .getBody();
    }
}
