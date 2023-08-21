package ru.practicum.service.event;

import ru.practicum.dto.event.*;
import ru.practicum.enums.SortType;
import ru.practicum.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    /**
     * @param userId
     * @param body
     * @return
     */
    EventFullDto saveEvent(long userId, NewEventDto body);

    /**
     * @param userId
     * @param eventId
     * @return
     */

    EventFullDto getEvent(long userId, long eventId);

    EventFullDto updateEventByUser(UpdateEventUserRequest body, long userId, long eventId);

    List<EventShortDto> getEvents(long userId, int from, int size);

    List<EventFullDto> getEventsByAdmin(EventSearchParameters searchParameters);

    /**
     * @param body
     * @param eventId
     * @return
     */
    EventFullDto updateEventByAdmin(UpdateEventAdminRequest body, long eventId);

    List<EventShortDto> getPublishedEvents(EventQuery query, HttpServletRequest request);

    EventFullDto getPublishedEvent(long id, HttpServletRequest request);

    Event findEventById(long eventId);

    List<Event> findEventsByIds(List<Long> eventIdList);
}
