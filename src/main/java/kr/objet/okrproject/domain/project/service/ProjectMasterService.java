package kr.objet.okrproject.domain.project.service;

import kr.objet.okrproject.domain.project.ProjectMaster;

public interface ProjectMasterService {

	ProjectMaster registerProjectMaster(ProjectMasterCommand.RegisterProjectMaster command);
}
