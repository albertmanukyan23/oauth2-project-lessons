package com.appsdeveloperblog.keycloak.myremoteuserstorageprovider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.UserCredentialStore;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteUserStorageProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator {

    private KeycloakSession keycloakSession;
    private ComponentModel componentModel;
    private UsersApiService usersApiService;

    @Override
    public void close() {

    }

    @Override
    public UserModel getUserById(RealmModel realmModel, String s) {
        return null;
    }

    @Override
    public UserModel getUserByUsername(RealmModel realmModel, String s) {

        UserModel returnValue = null;
        User user = usersApiService.getUserDetails(s);
        if (user != null) {
            returnValue = createUserModel(s, realmModel);
        }
        return returnValue;
    }

    private UserModel createUserModel(String s, RealmModel realmModel) {
        return new AbstractUserAdapter(keycloakSession, realmModel, componentModel) {
            @Override
            public String getUsername() {
                return s;
            }
        };
    }

    @Override
    public UserModel getUserByEmail(RealmModel realmModel, String s) {
        return null;
    }

    @Override
    public boolean supportsCredentialType(String s) {
        return PasswordCredentialModel.TYPE.equals(s);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String s) {
        if (!supportsCredentialType(s)){
            return  false;
        }
        return  !getCredentialsStore().getStoredCredentialsByTypeStream(realmModel,userModel,s).toList().isEmpty();
    }

    private UserCredentialStore getCredentialsStore() {
        return keycloakSession.userCredentianalManager();
    }

    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        VerifyPasswordResponse verifyPasswordResponse =
                usersApiService.verifyUserPassword(userModel.getUsername(), credentialInput.getChallengeResponse());
        if (verifyPasswordResponse == null)
            return false;
        return verifyPasswordResponse.getResult();
    }
}
