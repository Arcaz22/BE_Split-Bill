package com.portofolio.splitbill.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portofolio.splitbill.api.FriendshipApi;
import com.portofolio.splitbill.dto.friendship.FriendshipRequestDto;
import com.portofolio.splitbill.dto.friendship.PendingResponseDto;
import com.portofolio.splitbill.service.friendship.FriendshipService;
import com.portofolio.splitbill.util.ApiResponseUtil;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/friend")
public class FriendshipController implements FriendshipApi{
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping("/send-request")
    public ResponseEntity<Object> sendFriendRequest(Principal principal, @Valid @RequestBody FriendshipRequestDto friendRequestDto) {
        friendshipService.sendFriendRequest(principal, friendRequestDto);
        return ApiResponseUtil.success(HttpStatus.OK, "Friend request sent successfully");
    }

    @PostMapping("/update-status/{friendshipId}")
    public ResponseEntity<Object> statusFriendRequest(@PathVariable UUID friendshipId, @RequestParam boolean accept, Principal principal) {
        friendshipService.statusFriendRequest(principal, friendshipId, accept);

        if (accept) {
            return ApiResponseUtil.success(HttpStatus.OK, "Friend request accepted");
        } else {
            return ApiResponseUtil.success(HttpStatus.OK, "Friend request rejected");
        }
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<Object> getAllPendingFriendRequests(Principal principal) {
        List<PendingResponseDto> pendingRequests = friendshipService.getAllPendingFriendRequests(principal);

        return ApiResponseUtil.success(HttpStatus.OK, "Pending friend requests fetched successfully", pendingRequests);
    }

    @GetMapping("/accepted-friends")
    public ResponseEntity<Object> getAllAcceptedFriends(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> acceptedFriends = friendshipService.getAllAcceptedFriends(principal, page, size);

        if (acceptedFriends.isEmpty()) {
            return ApiResponseUtil.error(HttpStatus.NOT_FOUND, "No accepted friends found");
        }

        return ApiResponseUtil.success(HttpStatus.OK, "Accepted friends fetched successfully", acceptedFriends);
    }

    @DeleteMapping("/remove/{friendId}")
    public ResponseEntity<Object> removeFriend(Principal principal, @PathVariable UUID friendId) {
        friendshipService.removeFriend(principal, friendId);
            return ApiResponseUtil.success(HttpStatus.OK, "Friend removed successfully");
    }
}
