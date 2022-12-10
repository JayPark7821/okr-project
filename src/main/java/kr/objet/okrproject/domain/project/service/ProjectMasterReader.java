package kr.objet.okrproject.domain.project.service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.User;

import java.util.Optional;

public interface ProjectMasterReader {

    Optional<ProjectMaster> findByProjectTokenAndEmail(String token, User user);
}
