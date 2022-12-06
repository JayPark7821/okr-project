package kr.objet.okrproject.domain.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectRoleType {
    LEADER("LEADER","Leader"),
    MEMBER("MEMBER,", "Member");

    private final String code;
    private final String displayName;
}
