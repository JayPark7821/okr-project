package kr.objet.okrproject.infrastructure.team;

import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.service.TeamMemberStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamMemberStoreImpl implements TeamMemberStore {

    private final TeamMemberRepository teamMemberRepository;
    @Override
    public TeamMember store(TeamMember teamMember) {
        return teamMemberRepository.save(teamMember);
    }

}
