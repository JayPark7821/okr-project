package kr.objet.okrproject.domain.keyresult.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.KeyResultReader;
import kr.objet.okrproject.domain.keyresult.service.KeyResultService;
import kr.objet.okrproject.domain.keyresult.service.KeyResultStore;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyResultServiceImpl implements KeyResultService {

	private final KeyResultStore keyResultStore;
	private final KeyResultReader keyResultReader;

	@Override
	public KeyResult registerKeyResult(KeyResultCommand.RegisterKeyResultWithProject command) {
		KeyResult keyResult = command.toEntity();
		return keyResultStore.store(keyResult);
	}

	@Override
	public KeyResult validateKeyResultWithUser(String keyResultToken, User user) {
		KeyResult keyResult = keyResultReader.findByKeyResultTokenAndEmail(keyResultToken, user)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_KEYRESULT_TOKEN));
		List<TeamMember> teamMembers = keyResult.getProjectMaster().getTeamMember();

		if (teamMembers.size() > 1) {
			throw new OkrApplicationException(ErrorCode.INVALID_USER_INFO);
		}
		if (!teamMembers.get(0).getUser().getEmail().equals(user.getEmail())) {
			throw new OkrApplicationException(ErrorCode.INVALID_USER_INFO);
		}
		return keyResult;
	}
}
