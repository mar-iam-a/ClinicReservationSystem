# Clinic Reservation System


The **Clinic Reservation System** is a Java-based console application designed to manage patients, appointments, and clinics.  
It follows **Object-Oriented Programming (OOP)** principles and provides role-based functionality for patients and practitioners to simulate a real-world clinic workflow.

---

## Features

### User Management
- Register new users (patients and practitioners) with unique credentials.  
- Login system through the console.  
- Role-based options: different menus for patients and doctors.  

### Clinic Management
- Practitioners can create and manage their clinics.  
- Set clinic details such as name, address, department, and consultation price.  
- Define and update working hours using schedules and time slots.  

### Appointment Management
- Patients can search for available clinics by department.  
- Book, view, and cancel appointments.  
- Practitioners can view all upcoming appointments.  
- Prevents double-booking for the same time slot.  

### Rating System
- Patients can rate and comment on clinics after their appointments.  
- Clinics display average ratings based on patient feedback.  

### Data Handling
- Data is currently stored in memory (no permanent database).    
- Clear structure for extending persistence later.

---

## Classes Structure
The system can be organized as follows:

- **ClinicSystem:** Main application file, contains menus and program logic.  
- **User:** Base abstract class for shared user data and behavior.  
- **Practitioner** Represents a doctor who owns a clinic.  
- **Patient** Represents a patient who can book appointments.  
- **Clinic:** Stores clinic details and its schedule.  
- **Schedule/ WorkingHoursRule.java:** Defines clinic working hours and available slots.  
- **Appointment.java:** Connects a patient, clinic, and time slot.  
- **Rating:** Manages patient feedback for clinics.  

---
