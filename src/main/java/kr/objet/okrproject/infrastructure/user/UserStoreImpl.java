package kr.objet.okrproject.infrastructure.user;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.service.UserStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStoreImpl implements UserStore {

	private final UserRepository userRepository;

	@Override
	public User store(User user) {
		return userRepository.saveAndFlush(user);
	}
}
