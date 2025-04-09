package com.example.paymentsystem.Exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "API error response model")
public class ApiError {

    @Schema(description = "HTTP status of the error", example = "BAD_REQUEST")
    private HttpStatus status;

    @Schema(description = "Error message", example = "Invalid VIN supplied")
    private String message;

}
