package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(long userId);

    boolean existsByRequesterIdAndEventId(long userId, long eventId);

    List<Request> findAllByEventId(long eventId);

}
