package kr.objet.okrproject.domain.team.service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.TeamMemberSavedInfo;
import kr.objet.okrproject.domain.user.User;

import java.util.List;

public interface TeamMemberService {

	TeamMember registerProjectTeamMember(TeamMemberCommand.RegisterProjectLeader command);

	void checkIsUserProjectLeader(List<TeamMember> teamMemberList, User user);

    List<TeamMember> findTeamMembersByProjectMasterAndUsers(ProjectMaster projectMaster, List<User> users);

	TeamMemberSavedInfo checkUsersAndRegisterTeamMember(List<User> users, List<TeamMember> teamMembers, ProjectMaster projectMaster);

	void validateEmailWithProject(String email, Long projectId);
}
