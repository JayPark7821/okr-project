package kr.objet.okrproject.infrastructure.project;

import static kr.objet.okrproject.domain.project.QProjectMaster.*;
import static kr.objet.okrproject.domain.team.QTeamMember.*;
import static kr.objet.okrproject.domain.user.QUser.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.QUser;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.project.SortType;

@Repository
public class ProjectMasterQueryRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public ProjectMasterQueryRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<ProjectMaster> retrieveProject(
		SortType sortType,
		String includeFinishedProjectYN,
		User requestUser,
		Pageable pageable
	) {

		QUser teamUser = new QUser("teamUser");
		List<ProjectMaster> results = queryFactory
			.select(projectMaster)
			.from(projectMaster)
			.innerJoin(projectMaster.teamMember, teamMember)
			.innerJoin(teamMember.user, user)
			.where(user.eq(requestUser),
				includeFinishedProject(includeFinishedProjectYN))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(getSortType(sortType))
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(projectMaster.id)
			.from(projectMaster)
			.innerJoin(projectMaster.teamMember, teamMember)
			.innerJoin(teamMember.user, user)
			.where(user.eq(requestUser),
				includeFinishedProject(includeFinishedProjectYN));

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
	}

	private OrderSpecifier<?> getSortType(SortType sortType) {

		switch (sortType) {
			case DEADLINE_CLOSE:
				return projectMaster.endDate.asc();
			case PROGRESS_HIGH:
				return projectMaster.progress.desc();
			case PROGRESS_LOW:
				return projectMaster.progress.asc();
			default:
				return projectMaster.createdDate.desc();
		}
	}

	private BooleanExpression includeFinishedProject(String YN) {
		return Objects.equals(YN, "Y") ? null : projectMaster.progress.lt(100);
	}

	public List<ProjectMaster> searchProjectsForCalendar(YearMonth yearMonth, User requester) {
		return queryFactory
			.selectDistinct(projectMaster)
			.from(projectMaster)
			.innerJoin(projectMaster.teamMember, teamMember)
			.innerJoin(teamMember.user, user)
			.where(user.eq(requester),
				getProjectWithinMonth(yearMonth))
			.orderBy(projectMaster.id.desc())
			.fetch();
	}

	private BooleanExpression getProjectWithinMonth(YearMonth yearMonth) {
		LocalDate monthEndDt = yearMonth.atEndOfMonth();
		LocalDate monthStDt = monthEndDt.minusDays(monthEndDt.lengthOfMonth() - 1);

		return projectMaster.endDate.after(monthStDt).and(projectMaster.startDate.before(monthEndDt));
	}
}
