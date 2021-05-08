package com.codesoom.assignment.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Task {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Task(@JsonProperty("id") Long id, @JsonProperty("title") String title) {
        this.id = id;
        this.title = title;

        final var serverTimeZone = "Asia/Seoul";
        var currentLocalTimeInSeoul = LocalDateTime.now(ZoneId.of(serverTimeZone));
        this.createdAt = currentLocalTimeInSeoul;
        this.lastUpdatedAt = currentLocalTimeInSeoul;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", createdAt=" + createdAt +
            ", lastUpdatedAt=" + lastUpdatedAt +
            '}';
    }

    public Task update(Task updatedTask) {
        this.title = updatedTask.title;

        final var serverTimeZone = "Asia/Seoul";
        var currentLocalTimeInSeoul = LocalDateTime.now(ZoneId.of(serverTimeZone));
        this.lastUpdatedAt = currentLocalTimeInSeoul;
        return this;
    }
}
