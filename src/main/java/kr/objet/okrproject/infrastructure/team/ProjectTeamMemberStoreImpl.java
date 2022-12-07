package kr.objet.okrproject.infrastructure.team;

import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectTeamMemberStoreImpl implements ProjectTeamMemberStore {

    private final ProjectTeamMemberRepository projectTeamMemberRepository;
    @Override
    public ProjectTeamMember store(ProjectTeamMember projectTeamMember) {
        return projectTeamMemberRepository.save(projectTeamMember);
    }

}
