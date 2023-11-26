package com.everett.services;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import com.everett.models.User;
import com.everett.utils.CustomJacksonProvider;

@Stateless
public class KeycloakAdminService {
    private static final Logger logger = LogManager.getLogger(CommentServiceImp.class);

    private String kcServerUrl = System.getenv("KAC_SERVER_URL");
    private String kcRealm = System.getenv("KAC_REALM");
    private String kcClientId = System.getenv("KAC_CLIENT_ID");
    private String kcClientSecret = System.getenv("KAC_CLIENT_SECRET");
    private String kcAdUserName = System.getenv("KAC_ADMIN");
    private String kcAdPassword = System.getenv("KAC_ADMIN_PASSWORD");

    private Keycloak keycloak = KeycloakBuilder.builder()
            .serverUrl(kcServerUrl)
            .realm(kcRealm)
            .grantType(OAuth2Constants.PASSWORD)
            .clientId(kcClientId)
            .clientSecret(kcClientSecret)
            .username(kcAdUserName)
            .password(kcAdPassword)
            .resteasyClient(
                    new ResteasyClientBuilder().connectionPoolSize(10).register(new CustomJacksonProvider()).build())
            .build();

    private RealmResource realmResource = keycloak.realm(kcRealm);
    private UsersResource usersRessource = realmResource.users();

    public void createKeycloakUser(User dbUser, String tempPassword) {
        logger.info("START TO CREAT USER IN KEYCLOAK WITH EMAIL: " + dbUser.getEmail());
        UserRepresentation kcUser = createKcUser(dbUser);

        // Create user in Keycloak
        Response response = usersRessource.create(kcUser);
        String userId = CreatedResponseUtil.getCreatedId(response);

        // Set password credential
        CredentialRepresentation passwordRep = createPasswordRep(tempPassword);
        UserResource userResource = usersRessource.get(userId);
        userResource.resetPassword(passwordRep);

        // Asign user role
        RoleRepresentation realmRole = getRealmRoleRep(dbUser.getRole().name());
        userResource.roles().realmLevel().add(Arrays.asList(realmRole));

        // Join default groups
        List<GroupRepresentation> existedGroup = realmResource.groups().groups();
        for (GroupRepresentation group : existedGroup) {
            userResource.joinGroup(group.getId());
        }
    }

    private UserRepresentation createKcUser(User dbUser) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(dbUser.getLoginName());
        user.setFirstName(dbUser.getGivenName());
        user.setLastName(dbUser.getFamilyName());
        user.setEmail(dbUser.getEmail());

        return user;

    }

    private CredentialRepresentation createPasswordRep(String tempPassword) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(true);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(tempPassword);

        return passwordCred;
    }

    private RoleRepresentation getRealmRoleRep(String role) {
        return realmResource.roles().get(role).toRepresentation();
    }
}
