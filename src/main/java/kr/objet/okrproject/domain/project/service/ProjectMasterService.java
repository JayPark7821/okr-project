package kr.objet.okrproject.domain.project.service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.User;

public interface ProjectMasterService {

	ProjectMaster registerProjectMaster(ProjectMasterCommand.RegisterProjectMaster command);

    ProjectMaster validateProjectMasterWithUser(String projectToken, User user);


}
