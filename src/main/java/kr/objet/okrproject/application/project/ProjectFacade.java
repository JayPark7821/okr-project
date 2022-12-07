package kr.objet.okrproject.application.project;

import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultService;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberService;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFacade {

	private final ProjectMasterService projectMasterService;
	private final ProjectTeamMemberService projectTeamMemberService;
	private final ProjectKeyResultService projectKeyResultService;

	public Long registerProject(ProjectMasterCommand.RegisterProjectMaster command, User user) {

		ProjectMaster projectMaster = projectMasterService.registerProjectMaster(command);

		 projectTeamMemberService.registerProjectTeamMember(
				 new ProjectTeamMemberCommand.RegisterProjectTeamMember(projectMaster,
						 ProjectRoleType.LEADER,
						 true,
						 user
		 ));

		command.getKeyResults().forEach(keyResult ->{
			projectKeyResultService.registerProjectKeyResult(
					new ProjectKeyResultCommand.RegisterProjectKeyResult(keyResult, projectMaster)
			);
		});

		return projectMaster.getProjectId();
	}
}
