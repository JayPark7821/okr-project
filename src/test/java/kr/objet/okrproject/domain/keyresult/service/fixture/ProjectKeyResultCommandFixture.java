package kr.objet.okrproject.domain.keyresult.service.fixture;

import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultCommand;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;

public class ProjectKeyResultCommandFixture {
    public static ProjectKeyResultCommand.RegisterProjectKeyResult create() {
        EasyRandomParameters param = new EasyRandomParameters()
                .dateRange(LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 12, 1));
        return new EasyRandom(param).nextObject(ProjectKeyResultCommand.RegisterProjectKeyResult.class);
    }
}
