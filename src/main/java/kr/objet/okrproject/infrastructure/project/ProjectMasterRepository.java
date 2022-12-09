package kr.objet.okrproject.infrastructure.project;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.objet.okrproject.domain.project.ProjectMaster;

@Repository
public interface ProjectMasterRepository extends JpaRepository<ProjectMaster, Long> {
	Optional<ProjectMaster> findByProjectMasterToken(String token);
}
