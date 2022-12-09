package kr.objet.okrproject.infrastructure.keyresult;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;

@Repository
public interface ProjectKeyResultRepository extends JpaRepository<ProjectKeyResult, Long> {
	List<ProjectKeyResult> findProjectKeyResultsByProjectMaster(ProjectMaster projectMaster);
}
