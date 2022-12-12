package kr.objet.okrproject.domain.keyresult.service;

import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.user.User;

public interface KeyResultService {

	KeyResult registerKeyResult(KeyResultCommand.RegisterKeyResultWithProject command);

	KeyResult validateKeyResultWithUser(String keyResultToken, User user);
}
