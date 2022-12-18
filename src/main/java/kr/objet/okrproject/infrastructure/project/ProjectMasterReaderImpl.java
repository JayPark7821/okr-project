package kr.objet.okrproject.infrastructure.project;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterReader;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.project.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectMasterReaderImpl implements ProjectMasterReader {

	private final ProjectMasterRepository projectMasterRepository;
	private final ProjectMasterQueryRepository projectMasterQueryRepository;

	@Override
	public Optional<ProjectMaster> findByProjectTokenAndUser(String token, User user) {
		return projectMasterRepository.findByProjectMasterTokenAndUser(token, user);
	}

	@Override
	public Page<ProjectMaster> retrieveProject(
		SortType sortType,
		String includeFinishedProjectYN,
		User user,
		Pageable page
	) {
		return projectMasterQueryRepository.retrieveProject(sortType, includeFinishedProjectYN, user, page);
	}

	@Override
	public Optional<ProjectMaster> retrieveProjectDetail(String projectToken, User user) {
		return projectMasterRepository.findProjectDetailByProjectMasterTokenAndUser(projectToken, user);
	}

	@Override
	public List<ProjectMaster> searchProjectsForCalendar(YearMonth yearMonth, User user) {
		return projectMasterQueryRepository.searchProjectsForCalendar(yearMonth, user);
	}

	@Override
	public double calcProjectProgress(Long projectId) {
		return projectMasterQueryRepository.calcProjectProgress(projectId);
	}

	@Override
	public ProjectMaster getReferenceById(Long projectId) {
		return projectMasterRepository.getReferenceById(projectId);
	}

}
