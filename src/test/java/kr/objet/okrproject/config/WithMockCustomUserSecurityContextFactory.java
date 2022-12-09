package kr.objet.okrproject.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.domain.user.User;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		User principal = UserFixture.create(customUser.seq(), customUser.email());
		Authentication auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(),
			principal.getAuthorities());
		context.setAuthentication(auth);
		return context;
	}
}