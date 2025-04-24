package ru.practicum.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findByPinned(boolean pinned, Pageable pageable);
}
