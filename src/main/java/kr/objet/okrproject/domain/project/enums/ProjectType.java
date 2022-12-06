package kr.objet.okrproject.domain.project.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectType {
    TEAM("TEAM","Team"),
    SINGLE("SINGLE,", "Single");

    private final String code;
    private final String displayName;
}
