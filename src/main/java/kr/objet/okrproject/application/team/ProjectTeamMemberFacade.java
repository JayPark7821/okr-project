package kr.objet.okrproject.application.team;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectTeamMemberFacade {

	public void inviteTeamMembers(ProjectTeamMemberCommand.RegisterProjectTeamMember command, User user) {

	}
}
