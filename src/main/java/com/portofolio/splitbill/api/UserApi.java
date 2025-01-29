package com.portofolio.splitbill.api;

import java.security.Principal;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.portofolio.splitbill.dto.user.UserChangePasswordDto;
import com.portofolio.splitbill.dto.user.UserChangeProfileDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "API for user")
public interface UserApi {
    @Operation(summary = "Get User Profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetch user profile information for the currently logged-in user.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
                    {
                        "data": {
                            "avatar": "http://localhost:9153/uploads/4072a851-6744-442f-befa-215a809bfdae.png",
                            "full_name": "chandra",
                            "username": "chandra",
                            "phone": "628889912394444"
                        },
                        "message": "User profile"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
                    {
                        "message": "Unauthorized"
                    }
                    """)))
    })
    public ResponseEntity<Object> getUserProfile(Principal principal);

    @Operation(summary = "Change User Profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update user profile", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
                    {
                        "message": "Profile has been changed"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                    @ExampleObject(name = "Invalid Image Type", value = """
                            {
                                "message": "Only image files are allowed"
                            }
                            """),
                    @ExampleObject(name = "Failed Upload", value = """
                            {
                                "message": "Failed to upload avatar"
                            }
                            """)
            })),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
                    {
                        "message": "Unauthorized"
                    }
                    """)))
    })
    public ResponseEntity<Object> changeProfile(Principal principal,
            @ModelAttribute UserChangeProfileDto userChangeProfileDto);

    @Operation(summary = "Change User Password")
    @RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
            {
                "old_password": "password",
                "new_password": "newpassword",
                "confirm_password": "newpassword"
            }
            """)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
                    {
                        "message": "Password has been changed"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                    @ExampleObject(name = "Passwords Do Not Match", value = """
                            {
                                "message": "Password and confirm password must be the same"
                            }
                            """),
                    @ExampleObject(name = "New Password Same as Old", value = """
                            {
                                "message": "New password cannot be the same as the old password"
                            }
                            """)
            })),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
                    {
                        "message": "Unauthorized"
                    }
                    """))),
    })
    public ResponseEntity<Object> changePassword(Principal principal,
            @RequestBody UserChangePasswordDto userChangePasswordDto);

    @Operation(summary = "Get All Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users" , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
                    {
                        "data": {
                            "users": [
                                {
                                    "id": "6abcca75-1c18-4459-aecb-28330522c0ab",
                                    "username": "chandra",
                                    "full_name": "chandra",
                                    "phone": "628889912394444",
                                    "avatar": "http://localhost:9153/uploads/4072a851-6744-442f-befa-215a809bfdae.png"
                                }
                            ],
                            "total": 1,
                            "page": 0,
                            "size": 10
                        },
                        "message": "List of users"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "Unauthorized" , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
                    {
                        "message": "Unauthorized"
                    }
                    """)))
    })
    public ResponseEntity<Object> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String username);
}
