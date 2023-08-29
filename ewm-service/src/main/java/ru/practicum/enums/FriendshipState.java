package ru.practicum.enums;

import ru.practicum.mapper.EnumMapper;

public enum FriendshipState {
    PENDING, APPROVED, REJECTED;

    public static FriendshipState from(String name) {
        return EnumMapper.getEnumFromString(FriendshipState.class, name, "Unknown friendship status");
    }
}