package kr.objet.okrproject.domain.initiative.service;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface InitiativeReader {
	Page<Initiative> searchInitiatives(String keyResultToken, User user, Pageable page);

    List<Initiative> searchInitiativesByDate(LocalDate searchDate, User user);
}
