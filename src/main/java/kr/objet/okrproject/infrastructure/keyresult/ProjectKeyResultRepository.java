package kr.objet.okrproject.infrastructure.keyresult;

import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectKeyResultRepository extends JpaRepository<ProjectKeyResult, Long> {
}
