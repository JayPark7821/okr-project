package kr.objet.okrproject.infrastructure.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.service.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

	private final UserRepository userRepository;

	@Override
	public Optional<User> findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	@Override
	public Optional<User> findUserByUserId(String userId) {
		return userRepository.findUserByUserId(userId);
	}

	@Override
	public List<User> findUsersByEmails(List<String> emails) {
		return userRepository.findUsersByEmails(emails);
	}

}
