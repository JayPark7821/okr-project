package kr.objet.okrproject.application.user.fixture;

import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.domain.user.enums.RoleType;
import kr.objet.okrproject.domain.user.enums.jobtype.JobFieldDetail;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class UserFixture {
    public static User create() {
        EasyRandomParameters param = new EasyRandomParameters();
        return new EasyRandom(param).nextObject(User.class);
    }
}
