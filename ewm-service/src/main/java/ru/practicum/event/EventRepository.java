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

    @Query("SELECT e FROM Event e " +
            "JOIN e.initiator u " +
            "JOIN e.category c " +
            "WHERE e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "AND (:users IS NULL OR u.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR c.id IN :categories)")
    Page<Event> findWithParams(@Param("users") List<Long> users, @Param("states") List<String> states,
                               @Param("categories") List<Long> categories, @Param("rangeStart") LocalDateTime rangeStart,
                               @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);

    Event findByIdAndInitiatorId(long eventId, long userId);

    @Query("SELECT e FROM Event e " +
            "JOIN e.category c " +
            "WHERE e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "AND (:text IS NULL OR e.description ILIKE %:text% OR e.annotation ILIKE %:text%) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:categories IS NULL OR c.id IN :categories)")
    Page<Event> findWithParamsPublic(@Param("text") String text, @Param("categories") List<Long> categories,
                                     @Param("paid") boolean paid, @Param("rangeStart") LocalDateTime rangeStart,
                                     @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);
}
