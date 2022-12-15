package kr.objet.okrproject.domain.team.service;

import java.util.List;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.TeamMemberSavedInfo;
import kr.objet.okrproject.domain.user.User;

public interface TeamMemberService {

	TeamMember registerProjectTeamMember(TeamMemberCommand.RegisterProjectLeader command);

	void validateEmailWithProject(String email, Long projectId);

	TeamMemberSavedInfo inviteTeamMembers(ProjectMaster projectMaster, User user, List<User> users);
}
