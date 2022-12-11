package kr.objet.okrproject.domain.keyresult.service;

import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;

public interface ProjectKeyResultService {

    ProjectKeyResult registerProjectKeyResult(ProjectKeyResultCommand.RegisterProjectKeyResultWithProject command);
}
