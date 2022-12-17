package kr.objet.okrproject.domain.project.service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.project.SortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;

public interface ProjectMasterService {

	ProjectMaster registerProjectMaster(ProjectMasterCommand.RegisterProjectMaster command);

	ProjectMaster validateUserWithProjectMasterToken(String projectToken, User user);

	Page<ProjectMaster> retrieveProject(SortType sortType, String includeFinishedProjectYN, User user, Pageable page);

	ProjectMaster retrieveProjectDetail(String projectToken, User user);

	ProjectMaster retrieveProjectProgress(String projectToken, User user);

	List<ProjectMaster> searchProjectsForCalendar(YearMonth yearMonth, User user);


	Double updateProgress(Long projectId);

	Double calcProjectProgress(Long projectId);
}
