package ru.practicum.api.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.enums.SortType;
import ru.practicum.service.event.EventQuery;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.YYYY_MM_DD_HH_MM_SS;
import static ru.practicum.utils.Constants.FROM;
import static ru.practicum.utils.Constants.PAGE_SIZE;

@RestController
@RequestMapping(path = "/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicEventController {
    private final EventService eventService;

    @GetMapping("/{id}")
    public EventFullDto getPublishedEvent(@PathVariable long id, HttpServletRequest request) {
        log.debug("Request received GET {}", request.getRequestURI());
        return eventService.getPublishedEvent(id, request);
    }

    @GetMapping
    public List<EventShortDto> getPublishedEvents(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "rangeStart", required = false)
            @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS) LocalDateTime rangeStart,
            @RequestParam(value = "rangeEnd", required = false)
            @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS) LocalDateTime rangeEnd,
            @RequestParam(value = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) SortType sort,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = FROM) Integer from,
            @Positive @RequestParam(value = "size", defaultValue = PAGE_SIZE) Integer size,
            HttpServletRequest request
    ) {
        log.debug("Request received GET /events");
        log.debug("RequestParams: text='{}',categories={},paid={},rangeStart={},rangeEnd={},onlyAvailable={},sort='{}',from={},size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        EventQuery query = new EventQuery();
        query.setText(text);
        query.setCategories(categories);
        query.setPaid(paid);
        query.setRangeStart(rangeStart);
        query.setRangeEnd(rangeEnd);
        query.setOnlyAvailable(onlyAvailable);
        query.setSort(sort);
        query.setFrom(from);
        query.setSize(size);

        return eventService.getPublishedEvents(query, request);
    }
}
