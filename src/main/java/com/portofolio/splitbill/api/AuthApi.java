package com.portofolio.splitbill.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.portofolio.splitbill.dto.auth.signin.SigninDto;
import com.portofolio.splitbill.dto.auth.signup.SignupDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "API for authentication")
public interface AuthApi {
    @Operation(summary = "Register")
    @RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
            {
                "username": "chandra",
                "password": "password",
                "confirm_password": "password"
            }
            """)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signup success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
                    {
                        "message": "Signup success",
                        "data": {
                            "username": "chandra",
                            "password": "password",
                            "confirm_password": "password"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "422", description = "Validation failed", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                    @ExampleObject(name = "Username has been taken", value = """
                            {
                                "message": "Validation failed",
                                "errors": {
                                    "username": "Username already been taken"
                                }
                            }
                            """),
                            @ExampleObject(name = "Password not match", value = """
                                {
                                    "message": "Signup success",
                                    "data": {
                                        "username": "chandra",
                                        "password": "password",
                                        "confirm_password": "password"
                                    }
                                }
                                """),
            }))
    })
    public ResponseEntity<Object> signup(SignupDto signupDto);

    @Operation(summary = "Login")
    @RequestBody(required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {
                "username": "chandra",
                "password": "password"
            }
            """)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signin success", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "message": "Signin success",
                        "data": {
                            "username": "chandra",
                            "full_name": null,
                            "phone": null,
                            "access_token": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmZGUyODgzOS03MjJkLTQwYzItYjhmYS02ZTM1MmU2YWFiOTAiLCJzdWIiOiJjaGFuZHJhIiwiaWF0IjoxNzM3ODg5MjE4LCJleHAiOjE3Mzc4OTI4MTh9.H80-pXgeSiWc6mlBrYg6XFXx8evm4WWZA6B6Z4KKvv0",
                            "refresh_token": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwMTBiZmUwZS1mNmMzLTRjNTMtYmU1Yi03ODcwYTllNDhjYTYiLCJzdWIiOiJjaGFuZHJhIiwiaWF0IjoxNzM3ODg5MjE4LCJleHAiOjE3Mzc4OTI4MTh9.7bln3qLKzoM4AGzJNU7O8ZN5cWUPISU25dg3jQNfNz8"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "message": "Invalid username or password"
                    }
                    """))),
            @ApiResponse(responseCode = "422", description = "Validation failed", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = """
                    {
                        "message": "Validation failed",
                        "errors": {
                            "password": "Password is required",
                            "username": "Username is required"
                        }
                    }
                    """)))
    })
    public ResponseEntity<Object> signin(SigninDto signinDto);

    @Operation(summary = "Logout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout success", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "message": "Signout success"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "message": "Unauthorized"
                    }
                    """)))
    })
    public ResponseEntity<Object> signOut();
}
