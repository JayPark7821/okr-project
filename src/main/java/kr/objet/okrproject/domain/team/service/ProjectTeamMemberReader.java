package kr.objet.okrproject.domain.team.service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.user.User;

import java.util.List;

public interface ProjectTeamMemberReader {
    List<ProjectTeamMember> findTeamMembersByProjectMasterAndUsers(ProjectMaster projectMaster, List<User> users);

    List<ProjectTeamMember> findTeamMembersByProjectId(Long projectId);
}
