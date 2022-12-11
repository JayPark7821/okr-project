package kr.objet.okrproject.domain.keyresult.service;

import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;
import lombok.Builder;
import lombok.Getter;


public class KeyResultCommand {

    public static class RegisterKeyResultWithProject {

        private final ProjectMaster projectMaster;

        private final String name;

        @Builder
        public RegisterKeyResultWithProject(String name, ProjectMaster projectMaster) {
            this.name = name;
            this.projectMaster = projectMaster;
        }

        public KeyResult toEntity() {
            return KeyResult.builder()
                    .projectMaster(projectMaster)
                    .name(this.name)
                    .build();
        }
    }

    @Getter
    public static class RegisterKeyResult {

        private final String projectToken;

        private final String name;

        @Builder
        public RegisterKeyResult(String name, String projectToken) {
            this.name = name;
            this.projectToken = projectToken;
        }
    }
}
