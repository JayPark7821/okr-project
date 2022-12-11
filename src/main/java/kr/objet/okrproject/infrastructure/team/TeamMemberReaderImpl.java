package kr.objet.okrproject.infrastructure.team;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.service.TeamMemberReader;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamMemberReaderImpl implements TeamMemberReader {

    private final TeamMemberRepository teamMemberRepository;

    @Override
    public List<TeamMember> findTeamMembersByProjectMasterAndUsers(ProjectMaster projectMaster, List<User> users) {
        return teamMemberRepository.findTeamMembersByProjectMasterAndUsers(projectMaster, users);
    }

    @Override
    public List<TeamMember> findTeamMembersByProjectId(Long projectId) {
        return teamMemberRepository.findTeamMembersByProjectId(projectId);
    }
}
