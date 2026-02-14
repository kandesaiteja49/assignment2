package com.airtribe.meditrack.exceptions;

public class AppointmentNotFoundException extends RuntimeException{
    public AppointmentNotFoundException(String message){
        super(message);
    }
}
