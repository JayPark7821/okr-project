package kr.objet.okrproject.domain.project.service;

import java.util.Optional;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.User;

public interface ProjectMasterReader {

	Optional<ProjectMaster> findByProjectTokenAndUser(String token, User user);
}
