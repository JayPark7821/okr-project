package kr.objet.okrproject.domain.keyresult.service.impl;

import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectKeyResultServiceImpl implements ProjectKeyResultService {

    @Override
    public ProjectKeyResult registerProjectKeyResult(ProjectKeyResultCommand.RegisterProjectKeyResult command) {
        return null;
    }
}
