package kr.objet.okrproject.infrastructure.initiative;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.initiative.service.InitiativeReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitiativeReaderImpl implements InitiativeReader {
	private final InitiativeRepository initiativeRepository;
}
