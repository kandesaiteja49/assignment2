package com.airtribe.meditrack.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;
}
