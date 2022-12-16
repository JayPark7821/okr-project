package kr.objet.okrproject.infrastructure.feedback;

import static kr.objet.okrproject.domain.feedback.QFeedback.*;
import static kr.objet.okrproject.domain.initiative.QInitiative.*;
import static kr.objet.okrproject.domain.project.QProjectMaster.*;
import static kr.objet.okrproject.domain.team.QTeamMember.*;
import static kr.objet.okrproject.domain.user.QUser.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.feedback.SearchRange;
import kr.objet.okrproject.domain.team.QTeamMember;
import kr.objet.okrproject.domain.user.User;

@Repository
public class FeedbackQueryRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public FeedbackQueryRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<Feedback> getAllFeedbackList(SearchRange range, User requester, Pageable pageable) {
		QTeamMember writerTeamMember = new QTeamMember("writerTeamMember");

		List<Feedback> results = queryFactory
			.selectFrom(feedback)
			.innerJoin(feedback.initiative, initiative).fetchJoin()
			.innerJoin(initiative.teamMember, teamMember).fetchJoin()
			.innerJoin(teamMember.projectMaster, projectMaster).fetchJoin()
			.innerJoin(feedback.teamMember, writerTeamMember).fetchJoin()
			.innerJoin(writerTeamMember.user, user).fetchJoin()
			.where(teamMember.user.eq(requester).and(initiative.done.isTrue())
				, searchRangeCondition(range))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(feedback.isChecked.asc(), feedback.createdDate.desc())
			.fetch();

		JPAQuery<Feedback> countQuery = queryFactory
			.selectFrom(feedback)
			.innerJoin(feedback.initiative, initiative).fetchJoin()
			.innerJoin(initiative.teamMember, teamMember).fetchJoin()
			.innerJoin(teamMember.projectMaster, projectMaster).fetchJoin()
			.innerJoin(feedback.teamMember, writerTeamMember).fetchJoin()
			.innerJoin(writerTeamMember.user, user).fetchJoin()
			.where(teamMember.user.eq(user).and(initiative.done.isTrue())
				, searchRangeCondition(range))
			.orderBy(feedback.isChecked.asc(), feedback.createdDate.desc());

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
	}

	private BooleanExpression searchRangeCondition(SearchRange searchRange) {
		Map<String, LocalDate> range = searchRange.getRange();
		if (range != null) {
			return feedback.createdDate.between(range.get("startDt").atStartOfDay(), range.get("endDt").atStartOfDay());
		} else {
			return null;
		}
	}
}
