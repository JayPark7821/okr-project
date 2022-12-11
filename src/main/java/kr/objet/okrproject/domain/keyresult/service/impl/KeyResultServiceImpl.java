package kr.objet.okrproject.domain.keyresult.service.impl;

import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.KeyResultService;
import kr.objet.okrproject.domain.keyresult.service.KeyResultStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyResultServiceImpl implements KeyResultService {

    private final KeyResultStore keyResultStore;

    @Override
    public KeyResult registerKeyResult(KeyResultCommand.RegisterKeyResultWithProject command) {
        KeyResult keyResult = command.toEntity();
        return keyResultStore.store(keyResult);
    }
}
