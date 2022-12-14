package kr.objet.okrproject.domain.project.service;

import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.project.SortType;

public interface ProjectMasterService {

	ProjectMaster registerProjectMaster(ProjectMasterCommand.RegisterProjectMaster command);

	ProjectMaster validateProjectMasterWithUser(String projectToken, User user);

	Page<ProjectMaster> retrieveProject(SortType sortType, String includeFinishedProjectYN, User user, Pageable page);

	ProjectMaster retrieveProjectDetail(String projectToken, User user);

	ProjectMaster retrieveProjectProgress(String projectToken, User user);

	List<ProjectMaster> searchProjectsForCalendar(YearMonth yearMonth, User user);
}
