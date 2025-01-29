package com.portofolio.splitbill.dto.friendship;

import java.util.UUID;

import com.portofolio.splitbill.enums.FriendshipStatus;

import lombok.Data;

@Data
public class PendingResponseDto  {
    private UUID id;
    private FriendshipStatus status;
    private UUID userId;
    private String userUsername;
    private UUID friendId;
    private String friendUsername;
}
