package kr.objet.okrproject.infrastructure.initiative;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.initiative.service.InitiativeReader;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitiativeReaderImpl implements InitiativeReader {

	private final InitiativeRepository initiativeRepository;
	private final InitiativeQueryRepository initiativeQueryRepository;

	@Override
	public Page<Initiative> searchInitiatives(String keyResultToken, User user, Pageable page) {
		return initiativeQueryRepository.searchInitiatives(keyResultToken, user, page);
	}
}
