package com.company.govpaysecured.security.oauth2;

import com.company.govpaysecured.security.SecurityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.CheckForNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Claim converter to add custom claims by retrieving the user from the userinfo endpoint.
 */
public class CustomClaimConverter implements Converter<Map<String, Object>, Map<String, Object>> {

    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();

    private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

    private final RestTemplate restTemplate;

    private final ClientRegistration registration;

    private final Map<String, ObjectNode> users = new HashMap<>();

    public CustomClaimConverter(ClientRegistration registration, RestTemplate restTemplate) {
        this.registration = registration;
        this.restTemplate = restTemplate;
    }

    private static final String GIVENNAME = "given_name";
    private static final String FAMILYNAME = "family_name";
    private static final String GROUPS = "groups";
    private static final String PREFFERED_USER = "preferred_username";
    private static final String ROLES = "roles";

    public Map<String, Object> convert(Map<String, Object> claims) {
        Map<String, Object> convertedClaims = this.delegate.convert(claims);
        @CheckForNull
        var reqAttrib = RequestContextHolder.getRequestAttributes();
        if (reqAttrib != null) {
            // Retrieve and set the token
            String token = bearerTokenResolver.resolve(((ServletRequestAttributes) reqAttrib).getRequest());
            var headers = new HttpHeaders();
            headers.set("Authorization", buildBearer(token));

            // Retrieve user infos from OAuth provider if not already loaded
            ObjectNode user = users.computeIfAbsent(
                claims.get("sub").toString(),
                s -> {
                    ResponseEntity<ObjectNode> userInfo = restTemplate.exchange(
                        registration.getProviderDetails().getUserInfoEndpoint().getUri(),
                        HttpMethod.GET,
                        new HttpEntity<String>(headers),
                        ObjectNode.class
                    );
                    return userInfo.getBody();
                }
            );

            // Add custom claims
            if (user != null) {
                convertedClaims.put(PREFFERED_USER, user.get(PREFFERED_USER).asText());
                if (user.has(GIVENNAME)) {
                    convertedClaims.put(GIVENNAME, user.get(GIVENNAME).asText());
                }
                if (user.has(FAMILYNAME)) {
                    convertedClaims.put(FAMILYNAME, user.get(FAMILYNAME).asText());
                }
                if (user.has(GROUPS)) {
                    List<String> groups = StreamSupport
                        .stream(user.get(GROUPS).spliterator(), false)
                        .map(JsonNode::asText)
                        .collect(Collectors.toList());
                    convertedClaims.put(GROUPS, groups);
                }

                if (user.has(SecurityUtils.CLAIMS_NAMESPACE + ROLES)) {
                    List<String> roles = StreamSupport
                        .stream(user.get(SecurityUtils.CLAIMS_NAMESPACE + ROLES).spliterator(), false)
                        .map(JsonNode::asText)
                        .collect(Collectors.toList());
                    convertedClaims.put(ROLES, roles);
                }
            }
        }
        return convertedClaims;
    }

    private String buildBearer(String token) {
        return "Bearer " + token;
    }
}
