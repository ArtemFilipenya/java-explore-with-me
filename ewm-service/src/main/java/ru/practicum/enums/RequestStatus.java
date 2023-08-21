package ru.practicum.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.practicum.mapper.EnumMapper;

public enum RequestStatus {
    PENDING, CONFIRMED, REJECTED, CANCELED;

    @JsonCreator
    public static RequestStatus from(String name) {
        return EnumMapper.getEnumFromString(RequestStatus.class, name, "Unknown request status");
    }
}
