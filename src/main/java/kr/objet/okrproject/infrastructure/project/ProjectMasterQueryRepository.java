package kr.objet.okrproject.infrastructure.project;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.project.SortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

import static kr.objet.okrproject.domain.initiative.QInitiative.initiative;
import static kr.objet.okrproject.domain.project.QProjectMaster.projectMaster;
import static kr.objet.okrproject.domain.team.QTeamMember.teamMember;
import static kr.objet.okrproject.domain.user.QUser.user;

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
			.where(user.eq(requester)
					.and(getProjectWithinMonth(yearMonth)))
			.orderBy(projectMaster.id.desc())
			.fetch();
	}

	private BooleanExpression getProjectWithinMonth(YearMonth yearMonth) {
		LocalDate monthEndDt = yearMonth.atEndOfMonth();
		LocalDate monthStDt = monthEndDt.minusDays(monthEndDt.lengthOfMonth() - 1);

		return projectMaster.endDate.after(monthStDt).and(projectMaster.startDate.before(monthEndDt));
	}

	public double calcProjectProgress(Long projectId) {
		List<Double> progress = queryFactory
				.select((new CaseBuilder()
						.when(initiative.done.isTrue()).then(1D)
						.otherwise(0D)
						.sum()).divide(initiative.count()).multiply(100)
				)
				.from(initiative)
				.where(initiative.keyResult.projectMaster.id.eq(projectId))
				.fetch();

		if (progress.get(0) == null) {
			throw new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_INFO);
		}
		return progress.get(0);
	}
}
