package kr.objet.okrproject.domain.initiative.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.user.User;

public interface InitiativeReader {
	Page<Initiative> searchInitiatives(String keyResultToken, User user, Pageable page);

	List<Initiative> searchInitiativesByDate(LocalDate searchDate, User user);

	List<Initiative> searchActiveInitiativesByDate(LocalDate monthEndDt, LocalDate monthStDt, User user);

	Optional<Initiative> findByInitiativeToken(String initiativeToken);
}
