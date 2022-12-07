package kr.objet.okrproject.infrastructure.project;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectMasterStoreImpl implements ProjectMasterStore {

	private final ProjectMasterRepository projectMasterRepository;


	@Override
	public ProjectMaster store(ProjectMaster projectMaster) {
		return projectMasterRepository.save(projectMaster);
	}

}
