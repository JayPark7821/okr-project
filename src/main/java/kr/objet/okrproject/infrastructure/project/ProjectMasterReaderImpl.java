package kr.objet.okrproject.infrastructure.project;

import java.util.Optional;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterReader;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectMasterReaderImpl implements ProjectMasterReader {

	private final ProjectMasterRepository projectMasterRepository;

	@Override
	public Optional<ProjectMaster> findByProjectTokenAndUser(String token, User user) {
		return projectMasterRepository.findByProjectMasterTokenAndUser(token, user);
	}
}
