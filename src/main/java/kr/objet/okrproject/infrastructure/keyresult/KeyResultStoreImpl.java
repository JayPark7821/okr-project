package kr.objet.okrproject.infrastructure.keyresult;

import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.keyresult.service.KeyResultStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyResultStoreImpl implements KeyResultStore {

    private final KeyResultRepository keyResultRepository;
    @Override
    public KeyResult store(KeyResult keyResult) {
        return keyResultRepository.save(keyResult);
    }

}
