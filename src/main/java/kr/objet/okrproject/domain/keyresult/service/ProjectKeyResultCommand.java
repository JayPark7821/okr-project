package kr.objet.okrproject.domain.keyresult.service;

import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;
import lombok.Builder;
import lombok.Getter;


public class ProjectKeyResultCommand {

    public static class RegisterProjectKeyResultWithProject {

        private final ProjectMaster projectMaster;

        private final String name;

        @Builder
        public RegisterProjectKeyResultWithProject(String name, ProjectMaster projectMaster) {
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

    @Getter
    public static class RegisterProjectKeyResult {

        private final String projectToken;

        private final String name;

        @Builder
        public RegisterProjectKeyResult(String name, String projectToken) {
            this.name = name;
            this.projectToken = projectToken;
        }
    }
}
