package kr.objet.okrproject.domain.project.service.impl;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectMasterServiceImpl implements ProjectMasterService {

	@Override
	public ProjectMaster registerProjectMaster(ProjectMasterCommand.RegisterProjectMaster command) {

		return null;
	}
}
