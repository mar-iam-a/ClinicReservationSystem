//package com.mycompany.clinicsystem;
//
//import java.io.*;
//import java.util.*;
//import java.nio.file.*;
//
//public class FileManager {
//    private static final String CLINIC_FILE = "clinics.txt";
//    private static final String APPOINTMENT_FILE = "appointments.txt";
//    private static final String PATIENT_FILE = "patients.txt"; // ✅ file for patients
//    private String delimit = ",";
//
//    // --- CLINIC OPERATIONS ---
//    public void addToFile(Clinic clinic) {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CLINIC_FILE, true))) {
//            bw.write(clinic.getID() + delimit +
//                     clinic.getDepartmentID() + delimit +
//                     clinic.getName() + delimit +
//                     clinic.getAddress() + delimit +
//                     clinic.getPrice() + "\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List<Clinic> retrieveClinics(Department department) {
//        List<Clinic> clinics = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(CLINIC_FILE))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] data = line.split(delimit);
//                if (Integer.parseInt(data[1]) == department.getID()) {
//                    Clinic c = new Clinic(
//                        Integer.parseInt(data[0]),
//                        department.getID(),
//                        data[2],
//                        data[3],
//                        Double.parseDouble(data[4]),
//                        null
//                    );
//                    clinics.add(c);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return clinics;
//    }
//
//    public void updateFile(Clinic updatedClinic) {
//        List<String> lines;
//        try {
//            lines = Files.readAllLines(Paths.get(CLINIC_FILE));
//            for (int i = 0; i < lines.size(); i++) {
//                String[] data = lines.get(i).split(delimit);
//                if (Integer.parseInt(data[0]) == updatedClinic.getID()) {
//                    lines.set(i, updatedClinic.getID() + delimit +
//                              updatedClinic.getDepartmentID() + delimit +
//                              updatedClinic.getName() + delimit +
//                              updatedClinic.getAddress() + delimit +
//                              updatedClinic.getPrice());
//                }
//            }
//            Files.write(Paths.get(CLINIC_FILE), lines);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void removeFromFile(Clinic clinic) {
//        try {
//            List<String> lines = Files.readAllLines(Paths.get(CLINIC_FILE));
//            lines.removeIf(line -> line.startsWith(String.valueOf(clinic.getID())));
//            Files.write(Paths.get(CLINIC_FILE), lines);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // --- APPOINTMENT OPERATIONS ---
//    public void addToFile(Appointment appointment) {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_FILE, true))) {
//            bw.write(appointment.getPatient().getID() + delimit +
//                     appointment.getClinic().getID() + delimit +
//                     appointment.getAppointmentDateTime().toString() + "\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List<Appointment> getAppointmentList(Clinic clinic) {
//        List<Appointment> appointments = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_FILE))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] data = line.split(delimit);
//                if (Integer.parseInt(data[1]) == clinic.getID()) {
//                    Patient p = retrievePatientByID(Integer.parseInt(data[0]));
//                    Appointment a = new Appointment(p, clinic, retrieveTimeSlotByID(Integer.parseInt(data[2])));
//                    appointments.add(a);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return appointments;
//    }
//
//    // --- ✅ NEW: Retrieve Patient by ID ---
//    public Patient retrievePatientByID(int id) {
//        try (BufferedReader br = new BufferedReader(new FileReader(PATIENT_FILE))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] data = line.split(delimit);
//                if (Integer.parseInt(data[0]) == id) {
//                    // assuming file order: ID,Name,Phone,Email,Password
//                    return new Patient(
//                        Integer.parseInt(data[0]),
//                        data[1],
//                        data[2],
//                        data[3],
//                        data[4]
//                    );
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null; // if not found
//    }
//    
//    // --- ✅ NEW: Retrieve TimeSlot by ID ---
//public TimeSlot retrieveTimeSlotByID(int id) {
//    String TIMESLOT_FILE = "timeslots.txt";
//    String delimit = ",";
//
//    try (BufferedReader br = new BufferedReader(new FileReader(TIMESLOT_FILE))) {
//        String line;
//        while ((line = br.readLine()) != null) {
//            String[] data = line.split(delimit);
//            // assuming file format: ID,ClinicID,StartTime,EndTime,Available(true/false)
//            if (Integer.parseInt(data[0]) == id) {
//                return new TimeSlot(
//                    Integer.parseInt(data[0]),
//                    Integer.parseInt(data[1]),
//                    data[2],
//                    data[3],
//                    Boolean.parseBoolean(data[4])
//                );
//            }
//        }
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//
//    return null; // if not found
//}
//
//}
