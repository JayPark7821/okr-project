package kr.objet.okrproject.infrastructure.project;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.User;

@Repository
public interface ProjectMasterRepository extends JpaRepository<ProjectMaster, Long> {
	Optional<ProjectMaster> findByProjectMasterToken(String token);

	@Query("select p " +
		"from ProjectMaster p " +
		"join fetch p.teamMember t " +
		"join fetch t.user u " +
		"where p.projectMasterToken =:token " +
		"and t.user =:user ")
	Optional<ProjectMaster> findByProjectMasterTokenAndUser(@Param("token") String token,
		@Param("user") User user);

}
