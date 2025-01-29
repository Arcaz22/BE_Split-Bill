package com.portofolio.splitbill.dto.friendship;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendshipRequestDto {
    @NotNull
    @JsonProperty("friend_ids")
    private List<UUID> friendIds;
}
