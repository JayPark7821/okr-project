package kr.objet.okrproject.infrastructure.user;

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
	public User getUserByUsername(String username) {
		return userRepository.findUserByUsername(username)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_INFO));
	}

	@Override
	public User getUserByUserId(String userId) {
		return userRepository.findUserByUserId(userId)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_INFO));
	}

}
