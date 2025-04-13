package ru.practicum.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {
    private String app;
    private String uri;
    private long hits;
}
