package kr.objet.okrproject.infrastructure.team;

import java.util.List;

import kr.objet.okrproject.domain.project.ProjectMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.ProjectTeamMemberId;
import kr.objet.okrproject.domain.user.User;

@Repository
public interface ProjectTeamMemberRepository extends JpaRepository<ProjectTeamMember, ProjectTeamMemberId> {

	List<ProjectTeamMember> findProjectTeamMembersByUser(User user);

	@Query("select t " +
			"from ProjectTeamMember  t " +
			"join fetch t.user u " +
			"where t.projectMaster =:projectMaster " +
			"and t.user in :users")
	List<ProjectTeamMember> findTeamMembersByProjectMasterAndUsers(
			@Param("projectMaster") ProjectMaster projectMaster,
			@Param("users") List<User> users
	);
}
