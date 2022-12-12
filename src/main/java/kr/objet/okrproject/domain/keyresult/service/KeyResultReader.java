package kr.objet.okrproject.domain.keyresult.service;

import java.util.Optional;

import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.user.User;

public interface KeyResultReader {

	Optional<KeyResult> findByKeyResultTokenAndEmail(String token, User user);
}
