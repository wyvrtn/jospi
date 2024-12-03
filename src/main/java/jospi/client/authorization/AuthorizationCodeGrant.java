package jospi.client.authorization;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import jospi.client.core.AbstractApiAuthorization;
import jospi.models.authorization.AuthorizationCodeResponse;
import lombok.Getter;

@Getter
public class AuthorizationCodeGrant extends AbstractApiAuthorization {
	
	private String clientId;
	private String clientSecret;
	private String refreshToken;

    public AuthorizationCodeGrant(int clientId, String clientSecret, String code, String redirectUri) {
        this.clientId = Integer.toString(clientId);
        this.clientSecret = clientSecret;
        authorizationBody.put("client_id", this.clientId);
		authorizationBody.put("client_secret", this.clientSecret);
		authorizationBody.put("code", code);
		authorizationBody.put("grant_type", "authorization_code");
        authorizationBody.put("redirect_uri", redirectUri);
    }

    protected void authorizationFlow(StatefulHttpServiceProvider svc) {
    	String authBody = toFormUrl(authorizationBody);
		processResponse(svc.exchangeCode(authBody));
    }

	protected void refreshAccessToken(StatefulHttpServiceProvider svc) {
		authorizationBody.clear();
		authorizationBody.put("client_id", clientId);
		authorizationBody.put("client_secret", clientSecret);
		authorizationBody.put("grant_type", "refresh_token");
		authorizationBody.put("refresh_token", refreshToken);
		String authBody = toFormUrl(authorizationBody);
		processResponse((AuthorizationCodeResponse) svc.requestNewToken(authBody));
	}

	private void processResponse(AuthorizationCodeResponse response) {
		response.validation();
		setAccessToken(response.getAccessToken());
		setExpirationDate(OffsetDateTime.now(ZoneId.systemDefault())
			.plusSeconds(response.getExpiresIn() - 30L /** Leniency */));
		refreshToken = response.getRefreshToken();
	}
}