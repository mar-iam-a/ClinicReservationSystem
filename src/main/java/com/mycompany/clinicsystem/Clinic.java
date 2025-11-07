/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinicsystem;
import java.util.*;

/**
 *
 * @author Javengers
 */ 
public class Clinic {
    private int ID;
    private int departmentID;
    private String name;
    private String address;
    private double price;
    private Schedule schedule;
    private double avgRating;

    public Clinic(int ID, int departmentID, String name, String address, double price, Schedule schedule) {
        this.ID = ID;
        this.departmentID = departmentID;
        this.name = name;
        this.address = address;
        this.price = price;
        this.schedule = schedule;
    }
    
    public Clinic(int ID, int departmentID, String name, String address, double price, Schedule schedule, double avgRating) {
        this.ID = ID;
        this.departmentID = departmentID;
        this.name = name;
        this.address = address;
        this.price = price;
        this.schedule = schedule;
        this.avgRating = avgRating;
    }
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    //some logic
    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {    
        this.avgRating = avgRating;
    }

//    // Methods
//    public void addSchedule(Schedule schedule) {
//        this.schedule = schedule;
//    }
//
//    public void updateSchedule(Schedule schedule) {
//        this.schedule = schedule;
//    }
    
    
}

