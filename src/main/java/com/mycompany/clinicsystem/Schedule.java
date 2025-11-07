package com.mycompany.clinicsystem;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Javengers
 */
public class Schedule {
    private int ID;
    private int slotDurationInMinutes;
    private List<WorkingHoursRule> weeklyRules;
    private List<TimeSlot> slots;

    public Schedule(int ID, int slotDurationInMinutes, List<WorkingHoursRule> weeklyRules) {
        this.ID = ID;
        this.slotDurationInMinutes = slotDurationInMinutes;
        this.weeklyRules = weeklyRules;
        this.slots = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public int getSlotDurationInMinutes() {
        return slotDurationInMinutes;
    }

    public void setSlotDurationInMinutes(int slotDurationInMinutes) {
        this.slotDurationInMinutes = slotDurationInMinutes;
    }

    public List<WorkingHoursRule> getWeeklyRules() {
        return weeklyRules;
    }

    public void setWeeklyRules(List<WorkingHoursRule> weeklyRules) {
        this.weeklyRules = weeklyRules;
    }
    
    public void generateTimeSlots() {
        slots.clear();

        if (weeklyRules == null) return;

        for (WorkingHoursRule rule : weeklyRules) {
            LocalTime current = rule.getStartTime();
            while (current.plusMinutes(slotDurationInMinutes).isBefore(rule.getEndtTime()) ||
                   current.plusMinutes(slotDurationInMinutes).equals(rule.getEndtTime())) {
                LocalTime end = current.plusMinutes(slotDurationInMinutes);
                slots.add(new TimeSlot(rule.getDay(), current, end));
                current = end;
            }
        }
    }
    
    
}