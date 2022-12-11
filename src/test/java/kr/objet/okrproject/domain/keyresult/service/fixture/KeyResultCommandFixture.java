package kr.objet.okrproject.domain.keyresult.service.fixture;

import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;

public class KeyResultCommandFixture {
    public static KeyResultCommand.RegisterKeyResultWithProject create() {
        EasyRandomParameters param = new EasyRandomParameters()
                .dateRange(LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 12, 1))
                .collectionSizeRange(0,5);

        return new EasyRandom(param).nextObject(KeyResultCommand.RegisterKeyResultWithProject.class);
    }
}
