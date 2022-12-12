package kr.objet.okrproject.infrastructure.keyresult;

import java.util.Optional;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.keyresult.service.KeyResultReader;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyResultReaderImpl implements KeyResultReader {

	private final KeyResultRepository keyResultRepository;

	@Override
	public Optional<KeyResult> findByKeyResultTokenAndEmail(String token, User user) {
		return keyResultRepository.findByKeyResultTokenAndUser(token, user);
	}
}
