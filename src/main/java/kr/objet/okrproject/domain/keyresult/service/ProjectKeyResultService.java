package kr.objet.okrproject.domain.keyresult.service;

import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;

public interface ProjectKeyResultService {

    ProjectKeyResult registerProjectKeyResult(ProjectKeyResultCommand.RegisterProjectKeyResult command);
}
