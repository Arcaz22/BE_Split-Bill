package com.portofolio.splitbill.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.UUID;

import com.portofolio.splitbill.dto.friendship.FriendshipRequestDto;

@Tag(name = "Friendship", description = "API for managing friendships")
public interface FriendshipApi {

    @Operation(summary = "Send Friend Request")
    @RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
            {
                "friend_ids": ["2e6794ec-83dc-4a43-a403-cbf7b80f9583", "2f1fc8ac-9230-4b63-a32d-51da044d59d5"]
            }
            """)))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Friend request sent successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                @ExampleObject(value = """
                        {
                            "message": "Friend request sent successfully"
                        }
                        """)
        })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                @ExampleObject(name = "Invalid User", value = """
                        {
                            "message": "User not found"
                        }
                        """),
                @ExampleObject(name = "Invalid Friend", value = """
                        {
                            "message": "Friend not found"
                        }
                        """)
        }))
    })
    public ResponseEntity<Object> sendFriendRequest(Principal principal, @Valid @RequestBody FriendshipRequestDto friendRequestDto);

    @Operation(summary = "Accept or Reject Friend Request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Friend request accepted or rejected successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                @ExampleObject(name= "accepted", value = """
                        {
                            "message": "Friend request accepted"
                        }
                        """),
                @ExampleObject(name= "rejected",value = """
                        {
                            "message": "Friend request rejected"
                        }
                        """)
        })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                @ExampleObject(name = "Invalid Friendship ID", value = """
                        {
                            "message": "Friendship not found"
                        }
                        """),
                @ExampleObject(name = "Forbidden", value = """
                        {
                            "message": "You are not part of this friendship request"
                        }
                        """)
        }))
    })
    public ResponseEntity<Object> statusFriendRequest(@PathVariable UUID friendshipId, boolean accept, Principal principal);

    @Operation(summary = "Get All Pending Friend Requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pending friend requests fetched successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                    @ExampleObject(value = """
                            {
                                "message": "Pending friend requests fetched successfully",
                                "data": [
                                    {
                                        "id": "fbadcbdb-64a2-4637-94d1-1c3fb2bd503a",
                                        "status": "PENDING",
                                        "user_id": "c031bee2-d28e-4c42-ac9a-7d61145e0703",
                                        "user_username": "akun",
                                        "friend_id": "15b1fe16-c5e2-485b-a507-16cc9f957d83",
                                        "friend_username": "akun3"
                                    },
                                    {
                                        "id": "991e4521-15ef-466b-88fa-748759a26f32",
                                        "status": "PENDING",
                                        "user_id": "c031bee2-d28e-4c42-ac9a-7d61145e0703",
                                        "user_username": "akun",
                                        "friend_id": "1e8d6d29-545e-4aac-910f-38519da2eba0",
                                        "friend_username": "akun2"
                                    }
                                ]
                            }
                            """)
            })),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Object> getAllPendingFriendRequests(Principal principal);

    @Operation(summary = "Get All Friend")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetch all friends successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                    @ExampleObject(value = """
                            {
                                "message": "Fetch all friends successfully",
                                "data": {
                                    "content": [
                                        {
                                            "id": "fbadcbdb-64a2-4637-94d1-1c3fb2bd503a",
                                            "status": "ACCEPTED",
                                            "user_id": "c031bee2-d28e-4c42-ac9a-7d61145e0703",
                                            "user_username": "akun",
                                            "friend_id": "15b1fe16-c5e2-485b-a507-16cc9f957d83",
                                            "friend_username": "akun3"
                                        },
                                        {
                                            "id": "991e4521-15ef-466b-88fa-748759a26f32",
                                            "status": "ACCEPTED",
                                            "user_id": "c031bee2-d28e-4c42-ac9a-7d61145e0703",
                                            "user_username": "akun",
                                            "friend_id": "1e8d6d29-545e-4aac-910f-38519da2eba0",
                                            "friend_username": "akun2"
                                        }
                                    ],
                                    "pageable": {
                                        "sort": {
                                            "sorted": false,
                                            "unsorted": true,
                                            "empty": true
                                        },
                                        "pageNumber": 0,
                                        "pageSize": 10,
                                        "offset": 0,
                                        "paged": true,
                                        "unpaged": false
                                    },
                                    "totalPages": 1,
                                    "totalElements": 2,
                                    "last": true,
                                    "size": 10,
                                    "number": 0,
                                    "sort": {
                                        "sorted": false,
                                        "unsorted": true,
                                        "empty": true
                                    },
                                    "numberOfElements": 2,
                                    "first": true,
                                    "empty": false
                                }
                            }
                            """)
            })),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Object> getAllAcceptedFriends(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Remove Friend")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend removed successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                    @ExampleObject(value = """
                            {
                                "message": "Friend removed successfully"
                            }
                            """)
            })),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Object> removeFriend(Principal principal, @PathVariable UUID friendId);
}
