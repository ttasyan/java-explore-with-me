package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCategoryId(long catId);

    @Query("SELECT * FROM Event e WHERE e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "AND (:users IS NULL OR e.initiator IN :users) AND (:states IS NULL OR e.state IN :states)" +
            "AND (:categories IS NULL OR e.category IN :categories)")
    Page<Event> findWithParams(@Param("users") List<Long> users, @Param("states") List<String> states,
                               @Param("categories") List<Long> categories, @Param("rangeStart") LocalDateTime rangeStart,
                               @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);

    Event findByIdAndInitiatorId(long eventId, long userId);
}
