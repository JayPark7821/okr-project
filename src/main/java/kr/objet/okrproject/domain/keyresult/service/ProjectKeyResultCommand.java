package kr.objet.okrproject.domain.keyresult.service;

import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;
import lombok.Builder;


public class ProjectKeyResultCommand {

    public static class RegisterProjectKeyResult {

        private ProjectMaster projectMaster;

        private String name;

        @Builder
        public RegisterProjectKeyResult(String name, ProjectMaster projectMaster) {
            this.name = name;
            this.projectMaster = projectMaster;
        }

        public ProjectKeyResult toEntity() {
            return ProjectKeyResult.builder()
                    .projectMaster(projectMaster)
                    .name(this.name)
                    .build();
        }
    }

}
