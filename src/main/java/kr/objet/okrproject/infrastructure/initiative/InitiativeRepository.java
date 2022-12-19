package kr.objet.okrproject.infrastructure.initiative;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.user.User;

public interface InitiativeRepository extends JpaRepository<Initiative, Long> {

	@Query("select i "
		+ "from Initiative i "
		+ "join fetch i.teamMember it "
		+ "join fetch it.user iu "
		+ "left outer join fetch i.feedback f "
		+ "left outer join fetch f.teamMember t "
		+ "left outer join fetch t.user u "
		+ "where i.initiativeToken = :initiativeToken ")
	Optional<Initiative> findByInitiativeToken(
		@Param("initiativeToken") String initiativeToken
	);

	@Query("SELECT i " +
		"FROM Initiative i " +
		"join fetch i.keyResult k " +
		"join fetch k.projectMaster m " +
		"join fetch i.teamMember t " +
		"join fetch t.user u " +
		"where i.done = false " +
		"and :searchDate between i.sdt and i.edt " +
		"and t.user =:user")
	List<Initiative> findInitiativesByDateAndUser(@Param("searchDate") LocalDate searchDate, @Param("user") User user);

	@Query("SELECT i " +
		"FROM Initiative i " +
		"join fetch i.teamMember it " +
		"join fetch it.user iu " +
		"join i.keyResult k " +
		"join k.projectMaster m " +
		"join m.teamMember t " +
		"where i.initiativeToken =:token " +
		"and t.user =:user")
	Optional<Initiative> findByInitiativeTokenAndUser(@Param("token") String token, @Param("user") User user);

	@Query("select count(i.iniId)  " +
		"from Initiative i " +
		"join i.keyResult k " +
		"join k.projectMaster p " +
		"join p.teamMember t " +
		"where i.done = true " +
		"and t.user =:user " +
		"and i.iniId not in (select f.initiative.iniId " +
		"                    from Feedback f " +
		"                    inner join f.teamMember fm " +
		"                    where fm.user = :user ) ")
	Integer getCountForFeedbackToGive(@Param("user") User user);

	@Query("SELECT i " +
		"FROM Initiative i " +
		"join i.teamMember it " +
		"join fetch i.keyResult k " +
		"join fetch k.projectMaster m " +
		"join fetch m.teamMember t " +
		"join fetch t.user u " +
		"where i.initiativeToken =:token " +
		"and it.user =:user")
	Optional<Initiative> findByInitiativeTokenAndOwner(@Param("token") String token, @Param("user") User user);
}
