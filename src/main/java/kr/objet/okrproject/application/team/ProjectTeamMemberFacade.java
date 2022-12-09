package kr.objet.okrproject.application.team;

import kr.objet.okrproject.interfaces.team.ProjectTeamMemberDto;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectTeamMemberFacade {

	public ProjectTeamMemberDto.saveResponse inviteTeamMembers(ProjectTeamMemberCommand.InviteProjectTeamMember command, User user) {
		return new ProjectTeamMemberDto.saveResponse();
	}
}
