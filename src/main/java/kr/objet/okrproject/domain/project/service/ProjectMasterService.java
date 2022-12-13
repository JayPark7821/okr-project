package kr.objet.okrproject.domain.project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.project.SortType;

public interface ProjectMasterService {

	ProjectMaster registerProjectMaster(ProjectMasterCommand.RegisterProjectMaster command);

	ProjectMaster validateProjectMasterWithUser(String projectToken, User user);

	void validateProjectDueDate(ProjectMaster projectMaster);

	Page<ProjectMaster> retrieveProject(SortType sortType, String includeFinishedProjectYN, User user, Pageable page);
}
