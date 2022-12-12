package kr.objet.okrproject.infrastructure.initiative;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.objet.okrproject.domain.initiative.Initiative;

@Repository
public interface InitiativeRepository extends JpaRepository<Initiative, Long> {

	Optional<Initiative> findByInitiativeToken(String initiativeToken);
}
