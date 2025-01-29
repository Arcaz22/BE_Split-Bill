package com.portofolio.splitbill.service.friendship;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.portofolio.splitbill.dto.friendship.FriendshipRequestDto;
import com.portofolio.splitbill.dto.friendship.PendingResponseDto;
import com.portofolio.splitbill.dto.user.UserProfileResponseDto;
import com.portofolio.splitbill.enums.FriendshipStatus;
import com.portofolio.splitbill.exception.BadRequestException;
import com.portofolio.splitbill.exception.DataNotFoundException;
import com.portofolio.splitbill.exception.ResourceConflictException;
import com.portofolio.splitbill.model.Friendship;
import com.portofolio.splitbill.model.User;
import com.portofolio.splitbill.repository.FriendshipRepository;
import com.portofolio.splitbill.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public FriendshipServiceImpl(
        ModelMapper modelMapper,
        UserRepository userRepository,
        FriendshipRepository friendshipRepository
    ) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    @Value("${avatar.base-url}")
    private String avatarBaseUrl;

    public void sendFriendRequest(Principal principal, FriendshipRequestDto friendRequestDto) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not registered"));

        List<UUID> friendIds = friendRequestDto.getFriendIds();

        for (UUID friendshipId : friendIds) {
            User friend = userRepository.findById(friendshipId)
                    .orElseThrow(() -> {
                        log.error("Friend not found for friend ID: {}", friendshipId);
                        return new DataNotFoundException("Friend not found");
                    });

                    Optional<Friendship> existingRequest = friendshipRepository.findByUserIdAndFriendIdAndStatus(user.getId(), friendshipId, FriendshipStatus.PENDING);


            if (existingRequest.isPresent()) {
                Map<String, String> errors = Map.of("friendId", "Friend request already sent for friend ID: " + friendshipId);
                throw new ResourceConflictException("Friend request already sent", errors);
            }

            Friendship friendship = new Friendship();
            friendship.setUser(user);
            friendship.setFriend(friend);
            friendship.setStatus(FriendshipStatus.PENDING);

            friendshipRepository.save(friendship);
        }
    }

    @Override
    public void statusFriendRequest(Principal principal, UUID friendshipId, boolean accept) {
        String currentUsername = principal.getName();
        log.debug("Current logged-in user: {}", currentUsername);

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new DataNotFoundException("Friendship not found"));

        if (friendship.getUser().getUsername().equals(friendship.getFriend().getUsername())) {
            throw new BadRequestException("You cannot accept or reject your own friend request.");
        }

        if (!friendship.getUser().getUsername().equals(currentUsername) && !friendship.getFriend().getUsername().equals(currentUsername)) {
            throw new BadRequestException("You are not authorized to perform this action on this friendship request.");
        }

        if (accept) {
            friendship.setStatus(FriendshipStatus.ACCEPTED);
        } else {
            friendship.setStatus(FriendshipStatus.REJECTED);
        }

        friendshipRepository.save(friendship);
        log.debug("Friendship status updated to: {}", friendship.getStatus());
    }

    @Override
    public List<PendingResponseDto> getAllPendingFriendRequests(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Friendship> pendingFriendships = friendshipRepository.findAllByStatusAndUserIdOrStatusAndFriendId(
            FriendshipStatus.PENDING, user.getId(), FriendshipStatus.PENDING, user.getId());

        if (pendingFriendships.isEmpty()) {
            throw new DataNotFoundException("No pending friend requests found.");
        }

        return pendingFriendships.stream().map(friendship -> {
            PendingResponseDto dto = new PendingResponseDto();
            dto.setId(friendship.getId());
            dto.setStatus(friendship.getStatus());

            dto.setUserId(friendship.getUser().getId());
            dto.setUserUsername(friendship.getUser().getUsername());

            dto.setFriendId(friendship.getFriend().getId());
            dto.setFriendUsername(friendship.getFriend().getUsername());

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAllAcceptedFriends(Principal principal, int page, int size) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size);

        Page<Friendship> friendships = friendshipRepository.findByUserIdAndStatus(user.getId(), FriendshipStatus.ACCEPTED, pageable);
        Page<Friendship> friendOfFriendships = friendshipRepository.findByFriendIdAndStatus(user.getId(), FriendshipStatus.ACCEPTED, pageable);

        Set<Friendship> allAcceptedFriendsSet = new HashSet<>();
        allAcceptedFriendsSet.addAll(friendships.getContent());
        allAcceptedFriendsSet.addAll(friendOfFriendships.getContent());

        List<UserProfileResponseDto> users = allAcceptedFriendsSet.stream()
                .map(friendship -> {
                    // Tentukan siapa teman dan siapa pengguna berdasarkan ID
                    User friend = friendship.getUser().getId().equals(user.getId()) ? friendship.getFriend() : friendship.getUser();
                    UserProfileResponseDto dto = modelMapper.map(friend, UserProfileResponseDto.class);

                    // Menambahkan URL avatar jika tersedia
                    if (friend.getAvatar() != null) {
                        String avatarUrl = avatarBaseUrl + friend.getAvatar();  // Gabungkan base URL dan path avatar
                        dto.setAvatar(avatarUrl);  // Set URL avatar lengkap
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", allAcceptedFriendsSet.size());
        response.put("totalPages", (int) Math.ceil((double) allAcceptedFriendsSet.size() / size));
        response.put("currentPage", page + 1);
        response.put("items", users);

        return response;
    }

    @Override
    @Transactional
    public void removeFriend(Principal principal, UUID friendId) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        friendshipRepository.deleteByUserIdAndFriendId(user.getId(), friendId);
        friendshipRepository.deleteByUserIdAndFriendId(friendId, user.getId());
    }
}
