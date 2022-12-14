package kr.objet.okrproject.domain.initiative.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.user.User;

public interface InitiativeReader {
	Page<Initiative> searchInitiatives(String keyResultToken, User user, Pageable page);
}
