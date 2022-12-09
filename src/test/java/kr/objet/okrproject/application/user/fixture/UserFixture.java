package kr.objet.okrproject.application.user.fixture;

import static org.jeasy.random.FieldPredicates.*;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.user.User;

public class UserFixture {
	public static User create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(User.class);
	}

	public static User create(Long id, String email) {
		Predicate<Field> emailPredicate = named("email").and(ofType(String.class))
			.and(inClass(User.class));
		Predicate<Field> userSeqPredicate = named("userSeq").and(ofType(Long.class))
			.and(inClass(User.class));

		EasyRandomParameters param = new EasyRandomParameters()
			.randomize(emailPredicate, () -> email)
			.randomize(userSeqPredicate, () -> id);
		return new EasyRandom(param).nextObject(User.class);
	}

	public static EasyRandom get() {
		Predicate<Field> seqPredicate = named("userSeq").and(ofType(Long.class))
			.and(inClass(User.class));

		EasyRandomParameters param = new EasyRandomParameters()
			.excludeField(seqPredicate);
		return new EasyRandom(param);
	}
}
