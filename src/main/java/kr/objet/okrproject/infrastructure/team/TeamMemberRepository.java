package kr.objet.okrproject.infrastructure.team;

import java.util.List;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.objet.okrproject.domain.team.TeamMemberId;
import kr.objet.okrproject.domain.user.User;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMemberId> {

	List<TeamMember> findProjectTeamMembersByUser(User user);

	@Query("select t " +
			"from TeamMember  t " +
			"join fetch t.user u " +
			"where t.projectMaster =:projectMaster " +
			"and t.user in :users")
	List<TeamMember> findTeamMembersByProjectMasterAndUsers(
			@Param("projectMaster") ProjectMaster projectMaster,
			@Param("users") List<User> users
	);

	@Query("select t " +
			"from TeamMember t " +
			"join t.projectMaster m " +
			"join fetch t.user u " +
			"where m.id = :projectId ")
	List<TeamMember> findTeamMembersByProjectId(@Param("projectId") Long projectId);
}