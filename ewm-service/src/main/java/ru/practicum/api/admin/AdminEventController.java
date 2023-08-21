package ru.practicum.api.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.service.event.EventSearchParameters;
import ru.practicum.service.event.EventService;
import ru.practicum.utils.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.YYYY_MM_DD_HH_MM_SS;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {
    private final EventService eventService;

    /**
     * Получение списка событий администратором с заданными фильтрами.
     *
     * @param users      Список ID пользователей, чьи события нужно найти.
     * @param states     Список состояний, в которых находятся искомые события.
     * @param categories Список ID категорий, в которых будет вестись поиск.
     * @param rangeStart Дата и время, не ранее которых должно произойти событие.
     * @param rangeEnd   Дата и время, не позже которых должно произойти событие.
     * @param from       Количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size       Количество событий в наборе.
     * @return Список событий с заданными фильтрами.
     */
    @GetMapping
    public List<EventFullDto> getEventsByAdmin(
            @RequestParam(value = "users", required = false) List<Long> users,
            @RequestParam(value = "states", required = false) List<String> states,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "rangeStart", required = false)
            @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS) LocalDateTime rangeStart,
            @RequestParam(value = "rangeEnd", required = false)
            @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS) LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = Constants.FROM) Integer from,
            @Positive @RequestParam(value = "size", defaultValue = Constants.PAGE_SIZE) Integer size
    ) {
        log.debug("Request received GET /admin/events");
        log.debug("RequestParams: users={},states={},categories={},rangeStart={}, rangeEnd={}, from={}, size={} ",
                users, states, categories, rangeStart, rangeEnd, from, size);

        EventSearchParameters searchParameters = new EventSearchParameters();
        searchParameters.setUsers(users);
        searchParameters.setStates(states);
        searchParameters.setCategories(categories);
        searchParameters.setRangeStart(rangeStart);
        searchParameters.setRangeEnd(rangeEnd);
        searchParameters.setFrom(from);
        searchParameters.setSize(size);

        return eventService.getEventsByAdmin(searchParameters);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@Valid @RequestBody(required = false) UpdateEventAdminRequest body,
                                           @PathVariable long eventId) {
        log.debug("Request received PATCH /admin/events/{}:{}", eventId, body);
        return eventService.updateEventByAdmin(body, eventId);
    }
}
