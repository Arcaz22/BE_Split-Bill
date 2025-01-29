package com.portofolio.splitbill.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portofolio.splitbill.enums.FriendshipStatus;
import com.portofolio.splitbill.model.Friendship;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {
    Optional<Friendship> findByUserIdAndFriendId(UUID userId, UUID friendId);

    Optional<Friendship> findByUserIdAndFriendIdAndStatus(UUID id, UUID friendId, FriendshipStatus pending);

    List<Friendship> findAllByStatusAndUserIdOrStatusAndFriendId(FriendshipStatus status, UUID userId, FriendshipStatus status2, UUID friendId);

    Page<Friendship> findByUserIdAndStatus(UUID userId, FriendshipStatus status, Pageable pageable);

    Page<Friendship> findByFriendIdAndStatus(UUID friendId, FriendshipStatus status, Pageable pageable);

    public void deleteByUserIdAndFriendId(UUID userId, UUID friendId);

    boolean existsByUserIdAndFriendIdAndStatus(UUID id, UUID friendId, FriendshipStatus accepted);
}
