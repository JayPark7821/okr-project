package kr.objet.okrproject.application.user.fixture;

import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;

import java.util.HashMap;
import java.util.Map;

public class OAuth2UserInfoFixture {

    public static OAuth2UserInfo get(String id, String name, String email, String url) {

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("id", id);
        attributes.put("name", name);
        attributes.put("email", email);
        attributes.put("picture", url);
        return new OAuth2UserInfo(attributes) {
            @Override
            public String getId() {
                return (String) attributes.get("id");
            }

            @Override
            public String getName() {
                return (String) attributes.get("name");
            }

            @Override
            public String getEmail() {
                return (String) attributes.get("email");
            }

            @Override
            public String getImageUrl() {
                return (String) attributes.get("picture");
            }
        };
    }
}
