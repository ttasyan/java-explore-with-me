package ru.practicum.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.Category;
import ru.practicum.user.User;

import java.time.LocalDateTime;

@Data
@Table(name = "events")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String annotation;

    @NotNull
    @Embedded
    private Location location;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(length = 7000)
    @NotBlank
    private String description;

    @NotNull
    private Boolean paid;


    @Enumerated(EnumType.STRING)
    private EventStatus state;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    private int confirmedRequests;

    @PositiveOrZero
    private int participantLimit;

    private long views;

}
