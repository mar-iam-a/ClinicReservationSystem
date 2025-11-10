package com.mycompany.clinicsystem;
import java.time.DayOfWeek;
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

    public List<TimeSlot> getSlots() {
        return slots;
    }

    public void setSlots(List<TimeSlot> slots) {
        this.slots = slots;
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
    slots.clear(); // نمسح أي مواعيد قديمة

    if (weeklyRules==null || weeklyRules.isEmpty()) {
        System.out.println(" No working hours defined yet.");
        return;
    }

    for (WorkingHoursRule rule : weeklyRules) {
        DayOfWeek day = rule.getDay();
        LocalTime start = rule.getStartTime();
        LocalTime end = rule.getEndtTime();

        LocalTime current = start;

        while (current.plusMinutes(slotDurationInMinutes).isBefore(end) ||
               current.plusMinutes(slotDurationInMinutes).equals(end)) {

            LocalTime slotEnd = current.plusMinutes(slotDurationInMinutes);

            TimeSlot slot = new TimeSlot(day, current, slotEnd);

         
            slots.add(slot);

            
            current = slotEnd;
        }
    }
}

   

    void updateSchedule() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}