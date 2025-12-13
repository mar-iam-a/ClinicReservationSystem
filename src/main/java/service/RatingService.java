/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.RatingDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import database.DBConnection;
import model.*;


public class RatingService{

    private final RatingDAO ratingDAO = new RatingDAO();
    private final   ClinicService clinicService=new ClinicService();

    public boolean addRating(Rating rating) throws SQLException {
        if(isDuplicateRating(rating.getPatient(), rating.getClinic())) {
            return false;
        }
        
        ratingDAO.add(rating);
        rating.getClinic().getRatings().add(rating);

        clinicService.updateAverageRating(rating.getClinic().getID());
        return true;
    }

    public void deleteRating(Rating rating) throws SQLException {
        ratingDAO.delete(rating.getId());
        rating.getClinic().getRatings().remove(rating);
    }

    public boolean isDuplicateRating(Patient patient, Clinic clinic) throws SQLException {
        List<Rating> ratings = ratingDAO.getAll();
        for(Rating r: ratings) {
            if(r.getPatient().getID() == patient.getID() && r.getClinic().getID() == clinic.getID()) {
                return true;
            }
        }
        return false;
    }
    public double calculateAverageRating(int clinicId) throws SQLException {

        List<Rating> ratings = ratingDAO.getRatingsByClinicId(clinicId);

        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }

        double totalScore = 0;
        for (Rating r : ratings) {
            totalScore += r.getScore();
        }

        return totalScore / ratings.size();
    }


}