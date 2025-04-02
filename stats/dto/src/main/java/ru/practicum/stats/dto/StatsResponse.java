package ru.practicum.stats.dto;

import lombok.Data;

@Data
public class StatsResponse {
    private String app;
    private String uri;
    private int hits;
}
