package ru.practicum.service.subs;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.enums.EventState;
import ru.practicum.enums.FriendshipState;
import ru.practicum.enums.RequestStatus;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.*;
import ru.practicum.service.user.UserService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    private final UserService userService;
    private final JPAQueryFactory queryFactory;

    public FriendServiceImpl(UserService userService,
                             EntityManager em) {
        this.userService = userService;
        this.queryFactory = new JPAQueryFactory(em);
    }

    /** Получение списка друзей */
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getFriends(long userId) {
        userService.checkExistById(userId);
        final List<User> fiends = getUserList(QFriendship.friendship.friend, QFriendship.friendship.follower, userId);
        return UserMapper.toDto(fiends);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getFollowers(long userId) {
        userService.checkExistById(userId);
        final List<User> fiends = getUserList(QFriendship.friendship.follower, QFriendship.friendship.friend, userId);
        return UserMapper.toDto(fiends);
    }

    /** Получить список событий в которых примут участие друзья */
    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getParticipateEvents(long followerId, int from, int size) {
        userService.checkExistById(followerId);
        final List<Event> events =
                queryFactory
                        .selectDistinct(QRequest.request.event)
                        .from(QRequest.request)
                        .innerJoin(QFriendship.friendship).on(QFriendship.friendship.friend.eq(QRequest.request.requester))
                        .where(QFriendship.friendship.follower.id.eq(followerId)
                                .and(QRequest.request.isPrivate.isFalse())
                                .and(QRequest.request.status.eq(RequestStatus.CONFIRMED))
                                .and(QRequest.request.event.eventDate.after(LocalDateTime.now().plusHours(2))))
                        .offset(from)
                        .limit(size)
                        .fetch();


        return EventMapper.toDto(events);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getFriendEvents(long followerId, int from, int size) {
        userService.checkExistById(followerId);
        final List<Event> events =
                queryFactory
                        .selectDistinct(QEvent.event)
                        .from(QEvent.event)
                        .innerJoin(QFriendship.friendship).on(QFriendship.friendship.friend.eq(QEvent.event.initiator))
                        .where(QFriendship.friendship.follower.id.eq(followerId)
                                .and(QEvent.event.state.eq(EventState.PUBLISHED))
                                .and(QEvent.event.eventDate.after(LocalDateTime.now().plusHours(2))))
                        .offset(from)
                        .limit(size)
                        .fetch();

        return EventMapper.toDto(events);
    }

    private List<User> getUserList(QUser friend, QUser user, long userId) {
        return queryFactory
                .select(friend)
                .from(QFriendship.friendship)
                .where(user.id.eq(userId).and(QFriendship.friendship.state.eq(FriendshipState.APPROVED)))
                .fetch();
    }
}
