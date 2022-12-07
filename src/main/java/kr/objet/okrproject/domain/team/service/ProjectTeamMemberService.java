package kr.objet.okrproject.domain.team.service;

import kr.objet.okrproject.domain.team.ProjectTeamMember;

public interface ProjectTeamMemberService {

	ProjectTeamMember registerProjectTeamMember(ProjectTeamMemberCommand.RegisterProjectTeamMember command);
}
