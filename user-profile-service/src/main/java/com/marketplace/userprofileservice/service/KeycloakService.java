package com.marketplace.userprofileservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jakarta.ws.rs.core.Response; // CORREGIDO: Jakarta en lugar de javax
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.marketplace.userprofileservice.dto.CreateUserRequestDTO;

@Service
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakService {
    
    private String serverUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
    
    public String createKeycloakUser(CreateUserRequestDTO request, String roleName) {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm("master")
                    .clientId("admin-cli")
                    .username(username)
                    .password(password)
                    .build();
            
            // SIMPLIFICADO: Extraer nombre del email
            String emailPart = request.getEmail().split("@")[0];
            
            UserRepresentation user = new UserRepresentation();
            user.setUsername(request.getEmail());
            user.setEmail(request.getEmail());
            user.setFirstName(emailPart);        
            user.setLastName("-");               
            user.setEnabled(true);
            user.setEmailVerified(true);
            user.setRequiredActions(new ArrayList<>());
            
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(request.getPassword());
            credential.setTemporary(false);
            user.setCredentials(Arrays.asList(credential));
            
            Response response = keycloak.realm(realm).users().create(user);
            
            if (response.getStatus() == 201) {
                String userId = extractUserIdFromResponse(response);
                assignRoleToUser(keycloak, userId, roleName);
                keycloak.close();
                return userId;
            } else {
                throw new RuntimeException("Error creating user in Keycloak: " + response.getStatus());
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user in Keycloak", e);
        }
    }
    
    private String extractUserIdFromResponse(Response response) {
        String location = response.getHeaderString("Location");
        return location.substring(location.lastIndexOf('/') + 1);
    }
    
    private void assignRoleToUser(Keycloak keycloak, String userId, String roleName) {
        RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(List.of(role));
    }
    
    public String getServerUrl() { return serverUrl; }
    public void setServerUrl(String serverUrl) { this.serverUrl = serverUrl; }
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}