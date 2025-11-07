package com.mycompany.clinicsystem;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * @author Javengers
 */
public class WorkingHoursRule {
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endtTime;

    public WorkingHoursRule(DayOfWeek day, LocalTime startTime, LocalTime endtTime) {
        this.day = day;
        this.startTime = startTime;
        this.endtTime = endtTime;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndtTime() {
        return endtTime;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndtTime(LocalTime endtTime) {
        this.endtTime = endtTime;
    }
    
}