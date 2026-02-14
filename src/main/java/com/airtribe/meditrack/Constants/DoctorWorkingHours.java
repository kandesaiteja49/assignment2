package com.airtribe.meditrack.Constants;

import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class DoctorWorkingHours {

    public static final LocalTime START = LocalTime.of(9, 0);
    public static final LocalTime END = LocalTime.of(21, 0);

    public static final int SLOT_MINUTES = 30;

    public static final List<LocalTime> BREAKS = List.of(
            LocalTime.of(13, 0), // lunch break start
            LocalTime.of(14, 0)
    );
}
