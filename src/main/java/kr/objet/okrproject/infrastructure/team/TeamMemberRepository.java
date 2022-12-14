package kr.objet.okrproject.infrastructure.team;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.TeamMemberId;
import kr.objet.okrproject.domain.user.User;

public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMemberId> {

	@Query("select t " +
		"from TeamMember  t " +
		"join fetch t.user u " +
		"where t.projectMaster =:projectMaster " +
		"and u.email =:email")
	Optional<TeamMember> findByProjectMasterAndUser(
		@Param("projectMaster") ProjectMaster projectMaster,
		@Param("email") String email
	);

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
