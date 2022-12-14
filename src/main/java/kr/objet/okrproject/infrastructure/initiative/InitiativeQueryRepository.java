package kr.objet.okrproject.infrastructure.initiative;

import static kr.objet.okrproject.domain.initiative.QInitiative.*;
import static kr.objet.okrproject.domain.project.QProjectMaster.*;
import static kr.objet.okrproject.domain.team.QTeamMember.*;
import static kr.objet.okrproject.domain.user.QUser.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.user.User;

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

}
