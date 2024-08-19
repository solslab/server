package kr.sols.domain.suggestion.repository;

import kr.sols.domain.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SuggestionRepository extends JpaRepository<Suggestion, UUID> {
}
