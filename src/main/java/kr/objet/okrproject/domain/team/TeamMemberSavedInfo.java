package kr.objet.okrproject.domain.team;

import kr.objet.okrproject.domain.notification.Notifications;
import kr.objet.okrproject.interfaces.team.TeamMemberDto;
import lombok.Getter;

import java.util.List;

@Getter

public class TeamMemberSavedInfo {


    private final String message;
    private final List<String> addedEmailList;

    public TeamMemberSavedInfo(List<String> addedEmailList, boolean doesProjectTypeChanged) {
        this.message = doesProjectTypeChanged ? Notifications.PROJECT_TYPE_CHANGE.getMsg() : null;
        this.addedEmailList = addedEmailList;
    }

    public TeamMemberDto.saveResponse toDto() {
        return new TeamMemberDto.saveResponse(this.message, this.addedEmailList);
    }
}
