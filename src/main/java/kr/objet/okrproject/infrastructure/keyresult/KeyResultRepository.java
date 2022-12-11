package kr.objet.okrproject.infrastructure.keyresult;

import java.util.List;
import java.util.Optional;

import kr.objet.okrproject.domain.keyresult.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.objet.okrproject.domain.project.ProjectMaster;

@Repository
public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {
	List<KeyResult> findProjectKeyResultsByProjectMaster(ProjectMaster projectMaster);

	Optional<KeyResult> findByKeyResultToken(String keyResultToken);
}
