package kr.objet.okrproject.infrastructure.team;

import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.ProjectTeamMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTeamMemberRepository extends JpaRepository<ProjectTeamMember, ProjectTeamMemberId> {
}
