package de.networkchallenge.lambda.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class VersionResponse {
    private String timestamp;
    private String gitHash;
    private String timestampISO;

    public VersionResponse(String timestamp, String gitHash) {
        this.timestamp = timestamp;
        this.timestampISO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(timestamp)), ZoneId.of("Europe/Berlin")).toString();
        this.gitHash = gitHash;
    }
}
