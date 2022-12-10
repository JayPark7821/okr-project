package kr.objet.okrproject.domain.team.service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.ProjectTeamMemberSavedInfo;
import kr.objet.okrproject.domain.user.User;

import java.util.List;

public interface ProjectTeamMemberService {

	ProjectTeamMember registerProjectTeamMember(ProjectTeamMemberCommand.RegisterProjectLeader command);

	void checkIsUserProjectLeader(List<ProjectTeamMember> teamMemberList, User user);

    List<ProjectTeamMember> findTeamMembersByProjectMasterAndUsers(ProjectMaster projectMaster, List<User> users);

	ProjectTeamMemberSavedInfo checkUsersAndRegisterTeamMember(List<User> users, List<ProjectTeamMember> teamMembers, ProjectMaster projectMaster);
}
