package kr.objet.okrproject.domain.project.service.impl;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterReader;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.project.service.ProjectMasterStore;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.project.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectMasterServiceImpl implements ProjectMasterService {

	private final ProjectMasterStore projectMasterStore;
	private final ProjectMasterReader projectMasterReader;

	@Override
	public ProjectMaster registerProjectMaster(ProjectMasterCommand.RegisterProjectMaster command) {
		ProjectMaster projectMaster = command.toEntity();
		return projectMasterStore.store(projectMaster);
	}

	@Override
	public ProjectMaster validateProjectMasterWithUser(String projectToken, User user) {
		return projectMasterReader.findByProjectTokenAndUser(projectToken, user)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));
	}

	@Override
	public void validateProjectDueDate(ProjectMaster projectMaster) {
		if (LocalDate.now().isAfter(projectMaster.getEndDate())) {
			throw new OkrApplicationException(ErrorCode.INVALID_PROJECT_END_DATE);
		}
	}

	@Override
	public Page<ProjectMaster> retrieveProject(
		SortType sortType,
		String includeFinishedProjectYN,
		User user,
		Pageable page
	) {
		return projectMasterReader.retrieveProject(sortType, includeFinishedProjectYN, user, page);
	}
}
