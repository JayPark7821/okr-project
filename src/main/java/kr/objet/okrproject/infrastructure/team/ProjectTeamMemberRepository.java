package kr.objet.okrproject.infrastructure.team;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.ProjectTeamMemberId;
import kr.objet.okrproject.domain.user.User;

@Repository
public interface ProjectTeamMemberRepository extends JpaRepository<ProjectTeamMember, ProjectTeamMemberId> {

	List<ProjectTeamMember> findProjectTeamMembersByUser(User user);
}
