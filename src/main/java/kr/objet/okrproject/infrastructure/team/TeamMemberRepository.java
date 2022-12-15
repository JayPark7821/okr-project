package kr.objet.okrproject.infrastructure.team;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.TeamMemberId;
import kr.objet.okrproject.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

	@Query("select t " +
			"from TeamMember t " +
			"join fetch t.projectMaster p " +
			"join fetch t.user u " +
			"where u.email not in :emails " +
			"and t.projectMaster =:projectMaster")
    List<TeamMember> findTeamMembersByEmailsNotIn(@Param("emails") List<String> emails, @Param("projectMaster") ProjectMaster projectMaster);


}
