package kr.objet.okrproject.application.team;

import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.notification.Notification;
import kr.objet.okrproject.domain.team.service.TeamMemberCommand;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.infrastructure.notification.NotificationRepository;
import kr.objet.okrproject.infrastructure.user.UserRepository;
import kr.objet.okrproject.interfaces.team.TeamMemberDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TeamMemberFacadeIntegrationTest {

	@Autowired
	private TeamMemberFacade sut;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NotificationRepository notificationRepository;

	private static final String PROJECT_TOKEN = "mst_Kiwqnp1Nq6lbTNn0";

	@Test
	void 신규_팀원_등록_성공() throws Exception {
		//given
		TeamMemberCommand.InviteTeamMember command =
			createInviteCommand(
				"user3@naver.com",
				"user4@naver.com"
			);

		User user = UserFixture.create(2L, "teamMemberTest@naver.com");
		//when
		TeamMemberDto.saveResponse response = sut.inviteTeamMembers(command, user);
		//then
		assertThat(response.getAddedEmailList()).contains("user3@naver.com", "user4@naver.com");

		assertNotification("teamMemberTest@naver.com",response.getAddedEmailList() );
	}



	@Test
	void 신규_팀원_등록_실패_리더가아님() throws Exception {
		//given
		TeamMemberCommand.InviteTeamMember command = createInviteCommand();
		User user = UserFixture.create(4L, "user2@naver.com");
		//when
		assertThatThrownBy(() -> sut.inviteTeamMembers(command, user))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_IS_NOT_LEADER);
	}

	@Test
	void 신규_팀원_등록_실패_추가된_팀원이_없음() throws Exception {
		//given
		TeamMemberCommand.InviteTeamMember command = createInviteCommand();
		User user = UserFixture.create(2L, "teamMemberTest@naver.com");
		//when
		assertThatThrownBy(() -> sut.inviteTeamMembers(command, user))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.NO_USERS_ADDED);

	}

	@Test
	void 신규_팀원_등록_실패_empty_list() throws Exception {
		//given
		TeamMemberCommand.InviteTeamMember command =
			new TeamMemberCommand.InviteTeamMember(
				PROJECT_TOKEN,
				null
			);
		
		User user = UserFixture.create(2L, "teamMemberTest@naver.com");
		//when
		assertThatThrownBy(() -> sut.inviteTeamMembers(command, user))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.NO_USERS_ADDED);

	}

	private TeamMemberCommand.InviteTeamMember createInviteCommand(String... emails) {
		List<String> memberEmailList =
			List.of("teamMemberTest@naver.com",
				"user1@naver.com",
				"user2@naver.com"
			);

		List<String> finalEmailList = Stream
			.concat(memberEmailList.stream(), Arrays.stream(emails))
			.collect(Collectors.toList());

		return new TeamMemberCommand.InviteTeamMember(
			PROJECT_TOKEN,
			finalEmailList
		);
	}

	private void assertNotification(String teamMember, List<String> invitedMembers) {
		List<Notification> teamMemberNoti = notificationRepository.findAllByEmail(teamMember);
		List<Notification> invitedMemberNoti = notificationRepository.findAllByEmail(invitedMembers.get(0));
		List<String> teamMemberNotiMsg = teamMemberNoti.stream().map(Notification::getMsg).collect(Collectors.toList());
		assertThat(teamMemberNoti.size()).isEqualTo(invitedMembers.size());
		assertThat(teamMemberNotiMsg.toString()).contains(invitedMembers);
		assertThat(invitedMemberNoti.size()).isEqualTo(0);
	}

}
