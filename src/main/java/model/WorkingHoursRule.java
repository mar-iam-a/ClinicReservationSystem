package model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class WorkingHoursRule {

    private int scheduleId;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;

    public WorkingHoursRule(int scheduleId, DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.scheduleId = scheduleId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "WorkingHoursRule{" +
                "day=" + day +
                ", start=" + startTime +
                ", end=" + endTime +
                '}';
    }
}
