package kr.objet.okrproject.domain.keyresult.service;

import kr.objet.okrproject.domain.keyresult.KeyResult;

public interface KeyResultService {

    KeyResult registerKeyResult(KeyResultCommand.RegisterKeyResultWithProject command);
}
