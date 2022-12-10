package kr.objet.okrproject.domain.project.service.impl;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.project.service.ProjectMasterReader;
import kr.objet.okrproject.domain.user.User;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.project.service.ProjectMasterStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

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
		return projectMasterReader.findByProjectTokenAndEmail(projectToken, user)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));
	}
}
