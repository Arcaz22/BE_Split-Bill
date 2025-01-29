package com.portofolio.splitbill.service.friendship;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.portofolio.splitbill.dto.friendship.FriendshipRequestDto;
import com.portofolio.splitbill.dto.friendship.PendingResponseDto;

public interface FriendshipService {
   public void sendFriendRequest(Principal principal, FriendshipRequestDto friendRequestDto);

   public void statusFriendRequest(Principal principal, UUID friendshipId, boolean accept);

   public List<PendingResponseDto> getAllPendingFriendRequests(Principal principal);

   public Map<String, Object> getAllAcceptedFriends(Principal principal, int page, int size);

   public void removeFriend(Principal principal, UUID friendId);
}
