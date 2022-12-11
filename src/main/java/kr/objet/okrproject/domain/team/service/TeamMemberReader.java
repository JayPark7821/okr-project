package kr.objet.okrproject.domain.team.service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.user.User;

import java.util.List;

public interface TeamMemberReader {
    List<TeamMember> findTeamMembersByProjectMasterAndUsers(ProjectMaster projectMaster, List<User> users);

    List<TeamMember> findTeamMembersByProjectId(Long projectId);
}
