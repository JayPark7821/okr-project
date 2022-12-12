package kr.objet.okrproject.infrastructure.initiative;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.initiative.service.InitiativeStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitiativeStoreImpl implements InitiativeStore {

	private final InitiativeRepository initiativeRepository;

	@Override
	public Initiative store(Initiative initiative) {
		return initiativeRepository.save(initiative);
	}

}
