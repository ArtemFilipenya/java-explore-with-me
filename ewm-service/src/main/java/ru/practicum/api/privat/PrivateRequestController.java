package ru.practicum.api.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.request.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final RequestService requestService;

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable(value = "userId") long userId,
                                                           @RequestParam(value = "eventId") long eventId) {
        log.debug("Request received GET /users/{}/requests?eventId={}", userId, eventId);
        return requestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestStatus(@Valid @RequestBody EventRequestStatusUpdateRequest body,
                                                              @PathVariable(value = "userId") long userId,
                                                              @PathVariable(value = "eventId") long eventId) {
        log.debug("Request received PATCH /users/{}/events/{}/requests : {}", userId, eventId, body);
        return requestService.changeRequestStatus(body, userId, eventId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipants(
            @PathVariable(value = "userId") long userId,
            @PathVariable(value = "eventId") long eventId) {
        log.debug("Request received GET /users/{}/events/{}/requests", userId, eventId);
        return requestService.getEventParticipants(userId, eventId);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable(value = "userId") long userId) {
        log.debug("Request received GET /users/{}/requests", userId);
        return requestService.getUserRequests(userId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable(value = "userId") long userId,
                                                 @PathVariable(value = "requestId") long requestId) {
        log.debug("Request received GET /users/{}/requests/{}/cancel", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }

    /**
     * Скрыть события от друзей<br>
     * - Изменить видимость можно только у подтвержденных запросов на участие<br>
     * - Изменять можно только свои запросы
     */
    @PatchMapping("/requests/hide")
    public List<ParticipationRequestDto> hideParticipation(
            @PathVariable(value = "userId") long userId,
            @RequestParam(value = "ids") List<Long> ids) {
        log.debug("Request received PATCH /users/{}/requests/hide?ids={}", userId, ids);
        return requestService.changeVisibilityEventParticipation(userId, ids, true);
    }

    /**
     * Показать события друзьям<br>
     * - Изменить видимость можно только у подтвержденных запросов на участие<br>
     * - Изменять можно только свои запросы
     */
    @PatchMapping("/requests/show")
    public List<ParticipationRequestDto> showParticipation(
            @PathVariable(value = "userId") long userId,
            @RequestParam(value = "ids") List<Long> ids) {
        log.debug("Request received PATCH /users/{}/requests/show?ids={}", userId, ids);
        return requestService.changeVisibilityEventParticipation(userId, ids, false);
    }
}