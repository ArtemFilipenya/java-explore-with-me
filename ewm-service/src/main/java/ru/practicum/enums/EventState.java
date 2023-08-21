package ru.practicum.enums;

import lombok.Getter;

import ru.practicum.mapper.EnumMapper;

@Getter
public enum EventState {
    CANCELED,
    PENDING,
    PUBLISHED;

    public static EventState from(String name) {
        return EnumMapper.getEnumFromString(EventState.class, name, "Unknown event state");
    }
}
