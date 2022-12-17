package kr.objet.okrproject.infrastructure.initiative;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static kr.objet.okrproject.domain.initiative.QInitiative.initiative;
import static kr.objet.okrproject.domain.project.QProjectMaster.projectMaster;
import static kr.objet.okrproject.domain.team.QTeamMember.teamMember;
import static kr.objet.okrproject.domain.user.QUser.user;

@Repository
public class InitiativeQueryRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public InitiativeQueryRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<Initiative> searchInitiatives(String keyResultToken, User requester, Pageable page) {

		List<Initiative> results = queryFactory
			.select(initiative)
			.from(initiative)
			.innerJoin(initiative.teamMember, teamMember).fetchJoin()
			.innerJoin(teamMember.projectMaster, projectMaster).fetchJoin()
			.innerJoin(teamMember.user, user).fetchJoin()
			.where(keyResultTokenEq(keyResultToken))
			.offset(page.getOffset())
			.limit(page.getPageSize())
			.orderBy(initiative.createdDate.asc())
			.fetch();

		JPAQuery<Initiative> countQuery = queryFactory
			.select(initiative)
			.from(initiative)
			.where(keyResultTokenEq(keyResultToken));

		return PageableExecutionUtils.getPage(results, page, () -> countQuery.fetch().size());
	}

	private BooleanExpression keyResultTokenEq(String keyResultToken) {
		return initiative.keyResult.keyResultToken.eq(keyResultToken);
	}

	public List<Initiative> searchActiveInitiativesByDate(LocalDate monthEndDt, LocalDate monthStDt, User requester) {
		return queryFactory
				.select(initiative)
				.from(initiative)
				.innerJoin(initiative.teamMember, teamMember)
				.innerJoin(teamMember.projectMaster, projectMaster)
				.innerJoin(teamMember.user, user)
				.where(user.eq(requester)
						.and(initiative.edt.after(monthStDt).and(initiative.sdt.before(monthEndDt)))
						.and(initiative.done.eq(false)))
				.fetch();
	}


}
