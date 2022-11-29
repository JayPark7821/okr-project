package kr.objet.okrproject.domain.user.service;

import kr.objet.okrproject.domain.user.User;

public interface UserReader {

	User getUserBy(String username);
}
