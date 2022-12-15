package kr.objet.okrproject.domain.team.service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.TeamMemberSavedInfo;
import kr.objet.okrproject.domain.user.User;

import java.util.List;

public interface TeamMemberService {

	TeamMember registerProjectTeamMember(TeamMemberCommand.RegisterProjectLeader command);

	void validateEmailWithProject(String email, Long projectId);

	TeamMemberSavedInfo inviteTeamMembers(ProjectMaster projectMaster, User user, List<User> users);

	List<TeamMember> findTeamMembersByEmailsNotIn(List<String> addedEmailList, ProjectMaster projectMaster);
}
