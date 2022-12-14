package kr.objet.okrproject.infrastructure.keyresult;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.User;

public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {
	List<KeyResult> findProjectKeyResultsByProjectMaster(ProjectMaster projectMaster);

	Optional<KeyResult> findByKeyResultToken(String keyResultToken);

	@Query("select k " +
		"from KeyResult k " +
		"join fetch k.projectMaster p " +
		"join fetch p.teamMember t " +
		"join fetch t.user u " +
		"where k.keyResultToken =:token " +
		"and t.user =:user ")
	Optional<KeyResult> findByKeyResultTokenAndUser(@Param("token") String token,
		@Param("user") User user);
}
