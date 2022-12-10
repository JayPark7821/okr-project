package kr.objet.okrproject.application.team;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberService;
import kr.objet.okrproject.domain.user.service.UserService;
import kr.objet.okrproject.interfaces.team.ProjectTeamMemberDto;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectTeamMemberFacade {

	private final ProjectMasterService projectMasterService;
	private final ProjectTeamMemberService projectTeamMemberService;
	private final UserService userService;

	public ProjectTeamMemberDto.saveResponse inviteTeamMembers(ProjectTeamMemberCommand.InviteProjectTeamMember command, User user) {
		ProjectMaster projectMaster = projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user);
		projectTeamMemberService.checkIsUserProjectLeader(projectMaster.getProjectTeamMember(), user);
		List<User> users = userService.findUsersByEmails(command.getUserEmails());
		projectTeamMemberService.findTeamMembersByProjectMasterAndUsers(projectMaster, users);


		return new ProjectTeamMemberDto.saveResponse();
	}
}