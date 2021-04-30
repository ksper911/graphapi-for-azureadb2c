package com.ksper.graphapi.azureadb2c;

import com.microsoft.graph.auth.confidentialClient.ClientCredentialProvider;
import com.microsoft.graph.auth.enums.NationalCloud;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.ObjectIdentity;
import com.microsoft.graph.models.extensions.PasswordProfile;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Slf4j
class TestGraphApi {

    Logger logger = LoggerFactory.getLogger(TestGraphApi.class);

    final static String CLIENT_ID = "";
    final static String DOMAIN_NAME = "";
    final static String CLIENT_SECRET = "";
    final static String CLIENT_CREDENTIALS = "";
    final static String SCOPE = "";
    final static String PASSWORD = "";
    final static String DISPLAY_NAME = "";
    final static String NICKNAME = "";
    final static String USER_PRINCIPAL_NAME = "";
    final static String ISSUER = "";
    final static String ISSUER_ASSIGNED_ID = "";
    final static String DISPLAY_NAME_FOR_UPDATE = "";
    final static String ISSUER_ASSIGNED_ID_FOR_UPDATE = "";
    final static String OBJECT_ID_FOR_UPDATE = "";

    @Test
    void createUser() {
        logger.info(">>>createUser");

        /**
         * ユーザーインスタンスの作成
         */
        User user = new User();

        /**
         * パスワード属性の設定
         */
        PasswordProfile passwordProfile = new PasswordProfile();
        passwordProfile.password = PASSWORD;
        passwordProfile.forceChangePasswordNextSignIn = false;  //(*1)
        user.passwordProfile = passwordProfile;

        /**
         * ユーザー属性の設定（必須項目のみ）
         */
        user.displayName = DISPLAY_NAME;
        user.accountEnabled = true;
        user.mailNickname = NICKNAME;
        user.userPrincipalName = USER_PRINCIPAL_NAME; //(*2)
        ObjectIdentity objectIdentity = new ObjectIdentity();
        objectIdentity.signInType = "userName";  //(*3)
        objectIdentity.issuer = ISSUER;  //(*4)
        objectIdentity.issuerAssignedId = ISSUER_ASSIGNED_ID;
        List<ObjectIdentity> identities = new ArrayList();
        identities.add(objectIdentity);
        user.identities = identities;

        /**
         * Azure AD B2C 上にユーザーを作成
         */
        User response =
                createIGraphServiceClient(
                        CLIENT_ID,
                        CLIENT_SECRET,
                        DOMAIN_NAME)
                        .users()
                        .buildRequest()
                        .post(user); //(*5)

    }

    @Test
    void updateUser() {

        /**
         * ユーザーインスタンスの作成
         */
        User user = new User();


        /**
         * ユーザー属性の設定
         */
        user.displayName = DISPLAY_NAME_FOR_UPDATE;
        ObjectIdentity objectIdentity = new ObjectIdentity();
        objectIdentity.signInType = "username";
        objectIdentity.issuer = ISSUER;
        objectIdentity.issuerAssignedId = ISSUER_ASSIGNED_ID_FOR_UPDATE;
        List<ObjectIdentity> identities = new ArrayList();
        identities.add(objectIdentity);
        user.identities = identities;

        /**
         * Azure AD B2C 上にユーザーを更新
         */
        createIGraphServiceClient(
                CLIENT_ID,
                CLIENT_SECRET,
                DOMAIN_NAME).users(OBJECT_ID_FOR_UPDATE)
                .buildRequest().patch(user);  //  (*1)

    }

    private IGraphServiceClient createIGraphServiceClient(
            String clientId, String clientSecret, String tenantName) {

        List<String> scopes = new ArrayList();
        scopes.add(SCOPE); //(*6)
        ClientCredentialProvider authProvider = new ClientCredentialProvider(clientId,
                scopes, clientSecret, tenantName, NationalCloud.Global);
        IGraphServiceClient graphClient = GraphServiceClient
                .builder()
                .authenticationProvider(authProvider)
                .buildClient();
        return graphClient;
    }

}
