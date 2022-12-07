package kr.objet.okrproject.infrastructure.keyresult;

import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectKeyResultStoreImpl implements ProjectKeyResultStore {

    private final ProjectKeyResultRepository projectKeyResultRepository;
    @Override
    public ProjectKeyResult store(ProjectKeyResult projectKeyResult) {
        return projectKeyResultRepository.save(projectKeyResult);
    }

}
