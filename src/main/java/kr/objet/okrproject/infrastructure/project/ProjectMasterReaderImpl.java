package kr.objet.okrproject.infrastructure.project;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterReader;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectMasterReaderImpl implements ProjectMasterReader {

    private final ProjectMasterRepository projectMasterRepository;

    @Override
    public Optional<ProjectMaster> findByProjectTokenAndEmail(String token, User user) {
        return projectMasterRepository.findByProjectMasterTokenAndEmail(token, user);
    }
}
