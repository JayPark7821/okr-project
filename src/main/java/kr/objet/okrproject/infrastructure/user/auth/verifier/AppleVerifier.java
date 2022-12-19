package kr.objet.okrproject.infrastructure.user.auth.verifier;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.infrastructure.user.auth.AppleClient;
import kr.objet.okrproject.infrastructure.user.auth.ApplePublicKeyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AppleVerifier implements TokenVerifier {

    private final AppleClient appleClient;

    @Override
    public boolean support(ProviderType providerType) {
        return false;
    }

    @Override
    public OAuth2UserInfo varifyIdToken(String token) {
        Map<String, String> header = getHeader(token.substring(0, token.indexOf(".")));

        ApplePublicKeyResponse.Key key = appleClient.getAppleAuthPublicKey()
                .getMatchedKeyBy(header.get("kid"), header.get("alg"))
                .orElseThrow(() -> new OkrApplicationException(ErrorCode.INTERNAL_SERVER_ERROR));

        Claims body = getBodyFromToken(token, key);
        String uuid = UUID.randomUUID().toString();
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("email", body.get("email",String.class));
        attributes.put("name", "tempUser");
        attributes.put("id", uuid);

        return new OAuth2UserInfo(attributes);
    }

    private static Claims getBodyFromToken(String token, ApplePublicKeyResponse.Key key) {
        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, Base64.getUrlDecoder().decode(key.getN())),
                    new BigInteger(1, Base64.getUrlDecoder().decode(key.getE())));

            PublicKey publicKey = KeyFactory.getInstance(key.getKty()).generatePublic(publicKeySpec);

            return Jwts.parserBuilder()
                    .setSigningKey(publicKey).build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new OkrApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private static Map<String, String> getHeader(String headerOfIdentityToken) {
        try {
            return new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken), StandardCharsets.UTF_8), Map.class);
        } catch (Exception e) {
            throw new OkrApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


}
