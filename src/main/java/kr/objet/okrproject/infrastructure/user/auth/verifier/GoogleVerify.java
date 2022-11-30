package kr.objet.okrproject.infrastructure.user.auth.verifier;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.infrastructure.user.auth.info.GoogleOAuth2UserInfo;

@Component
public class GoogleVerify implements TokenVerifier {
	@Value("${google.clientId}")
	private String clientId;
	private final NetHttpTransport transport = new NetHttpTransport();
	private final JsonFactory jsonFactory = new GsonFactory();

	@Override
	public boolean support(ProviderType providerType) {
		return ProviderType.GOOGLE == providerType;
	}

	@Override
	public OAuth2UserInfo varifyIdToken(String token) {
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
			.setIssuers(Arrays.asList("https://accounts.google.com", "accounts.google.com"))
			.setAudience(Collections.singletonList(clientId))
			.build();

		GoogleIdToken idToken = null;
		try {
			idToken = verifier.verify(token);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (idToken != null) {
			GoogleIdToken.Payload payload = idToken.getPayload();
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put("id", payload.getSubject());
			attributes.put("name", payload.get("name"));
			attributes.put("email", payload.get("email"));
			attributes.put("picture", payload.get("picture"));
			return new GoogleOAuth2UserInfo(attributes);

		} else {
			throw new IllegalArgumentException("Invalid ID token");
		}
	}
}
