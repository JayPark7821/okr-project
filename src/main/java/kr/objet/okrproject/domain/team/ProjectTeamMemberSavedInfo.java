package kr.objet.okrproject.domain.team;

import kr.objet.okrproject.domain.notification.Notifications;
import kr.objet.okrproject.interfaces.team.ProjectTeamMemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter

public class ProjectTeamMemberSavedInfo {


    private final String message;
    private final List<String> addedEmailList;

    public ProjectTeamMemberSavedInfo(List<String> addedEmailList, boolean doesProjectTypeChanged) {
        this.message = doesProjectTypeChanged ? Notifications.PROJECT_TYPE_CHANGE.getMsg() : null;
        this.addedEmailList = addedEmailList;
    }

    public ProjectTeamMemberDto.saveResponse toDto() {
        return new ProjectTeamMemberDto.saveResponse(this.message, this.addedEmailList);
    }
}
