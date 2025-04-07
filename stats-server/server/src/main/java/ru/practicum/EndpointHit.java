package ru.practicum;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "hits")
@Entity
@Data
@NoArgsConstructor
public class EndpointHit {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "app", nullable = false)
    String app;

    @Column(name = "ip", nullable = false, length = 15)
    String ip;

    @Column(name = "uri", nullable = false)
    String uri;

    @Column(name = "timestamp", nullable = false, columnDefinition = "timestamp")
    LocalDateTime timestamp;
}
