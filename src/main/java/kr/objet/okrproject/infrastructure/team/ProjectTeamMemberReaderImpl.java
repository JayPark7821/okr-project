package kr.objet.okrproject.infrastructure.team;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberReader;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectTeamMemberReaderImpl implements ProjectTeamMemberReader {

    private final ProjectTeamMemberRepository projectTeamMemberRepository;

    @Override
    public List<ProjectTeamMember> findTeamMembersByProjectMasterAndUsers(ProjectMaster projectMaster, List<User> users) {
        return projectTeamMemberRepository.findTeamMembersByProjectMasterAndUsers(projectMaster, users);
    }

    @Override
    public List<ProjectTeamMember> findTeamMembersByProjectId(Long projectId) {
        return projectTeamMemberRepository.findTeamMembersByProjectId(projectId);
    }
}
