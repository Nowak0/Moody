package app.moody.repository;

import app.moody.entity.Mood;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MoodRepository extends JpaRepository<Mood, UUID> {
    @Query("""
    SELECT AVG(m.value)
    FROM Mood m
    WHERE m.date >= :fromDate
    """)
    Float getAverageMood(@Param("fromDate") LocalDateTime fromDate);

    List<Mood> findAllByOrderByDateDesc();
    List<Mood> findTop2ByOrderByDateDesc();
}
