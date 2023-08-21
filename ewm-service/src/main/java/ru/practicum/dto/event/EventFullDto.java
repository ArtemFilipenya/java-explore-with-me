package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Constants;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.enums.EventState;

import java.time.LocalDateTime;

/**
 * Класс EventFullDto представляет собой объект передачи данных (DTO), содержащий подробную информацию о событии.
 *
 * @param id                Уникальный идентификатор события.
 * @param title             Заголовок события.
 * @param annotation        Краткое описание или аннотация события.
 * @param category          Категория, к которой относится событие.
 * @param paid              Флаг, указывающий, требуется ли оплата за участие в событии.
 * @param eventDate         Дата и время, запланированные для события.
 *                          Формат: yyyy-MM-dd HH:mm:ss.
 * @param initiator         Информация об инициаторе события.
 * @param confirmedRequests Количество одобренных запросов на участие в событии.
 * @param description       Полное описание события.
 * @param participantLimit  Ограничение на количество участников.
 *                          Значение 0 означает отсутствие ограничения.
 * @param state             Текущее состояние события в его жизненном цикле.
 * @param createdOn         Дата и время создания события.
 *                          Формат: yyyy-MM-dd HH:mm:ss.
 * @param publishedOn       Дата и время публикации события.
 *                          Формат: yyyy-MM-dd HH:mm:ss.
 * @param location          Информация о местоположении события.
 * @param requestModeration Флаг, указывающий, требуется ли предварительная модерация запросов на участие.
 * @param views             Количество просмотров события.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Boolean paid;
    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Integer confirmedRequests;
    private String description;
    private Integer participantLimit;
    private EventState state;
    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createdOn;
    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime publishedOn;
    private LocationDto location;
    private Boolean requestModeration;
    private Long views = 0L;
}
