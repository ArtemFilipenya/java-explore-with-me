package ru.practicum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Пользователь (краткая информация)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {
    private Long id;
    private String name;
}
