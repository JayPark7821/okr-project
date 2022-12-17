package kr.objet.okrproject.infrastructure.initiative;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.initiative.service.InitiativeReader;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

	@Override
	public List<Initiative> searchInitiativesByDate(LocalDate searchDate, User user) {
		return initiativeRepository.findInitiativesByDateAndUser(searchDate, user);
	}

	@Override
	public List<Initiative> searchActiveInitiativesByDate(LocalDate monthEndDt, LocalDate monthStDt, User user) {
		return initiativeQueryRepository.searchActiveInitiativesByDate(monthEndDt, monthStDt, user);
	}

	@Override
	public Optional<Initiative> findByInitiativeToken(String initiativeToken) {
		return initiativeRepository.findByInitiativeToken(initiativeToken);
	}

	@Override
	public Optional<Initiative> findByInitiativeTokenAndUser(String token, User user) {
		return initiativeRepository.findByInitiativeTokenAndUser(token, user);
	}

	@Override
	public Integer getCountForFeedbackToGive(User user) {
		return initiativeRepository.getCountForFeedbackToGive(user);
	}

	@Override
	public Optional<Initiative> validateInitiativeOwnerWithToken(String token, User user) {
		return initiativeRepository.findByInitiativeTokenAndOwner(token, user);
	}

}
