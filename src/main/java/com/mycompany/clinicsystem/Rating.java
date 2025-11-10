
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.clinicsystem;

/**
 *
 * @author Javengers
 */
public class Rating {    
    private Patient patient;
    private Clinic clinic;
    private int score;
    private String comment;
    
    
    Rating( Patient patient , Clinic clinic , int score , String comment){
        this.patient=patient;
        this.clinic=clinic;
        this.score=score;
        this.comment=comment;
    }

    
    public Patient getPatient(){
        return patient;
    }
    
    public Clinic getClinic(){
        return clinic;
    }
    
    public int getScore(){
        return score;
    }
    
    public String getComment(){
        return comment;
    }
    
    public void setScore(int score){
        if(score>=1 && score<=5)
            this.score=score;
        else
            System.out.println("please Score must be between 1 to 5.");
    }
    
    
    public void setComment(String comment){
        this.comment=comment;
    }

    @Override
    public String toString() {
        return "Rating{ patient=" + patient + ", clinic=" + clinic + ", score=" + score + ", comment=" + comment + '}';
    }
    
}
