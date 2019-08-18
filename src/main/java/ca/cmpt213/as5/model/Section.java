package ca.cmpt213.as5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Comparator;

/*
 * Section.java
 * Class Description: A Section is in charge of storing
 *                    about information regarding the enrollment
 *                    counts and the type it is.
 * Last modified on: April 8th, 2018
 * Authors: Bowen He & Jordan Hoang
 * Emails : bhhe@sfu.ca & jha257@sfu.ca
 */
public class Section {
    private String type;
    private int enrollmentTotal;
    private int enrollmentCap;

    /****************
     * Constructors *
     ****************/
    public Section(String type, int enrollmentCap, int enrollmentTotal){
        this.type = type.trim();
        this.enrollmentCap = enrollmentCap;
        this.enrollmentTotal = enrollmentTotal;
    }

    /****************
     *Helper Methods*
     ****************/
    public void addEnrollTotal(int totalIncrement){
        enrollmentTotal += totalIncrement;
    }

    public void addEnrollCapacity(int capacityIncrement){
        enrollmentCap += capacityIncrement;
    }

    public void setType(String type) {
        this.type = type;
    }

    /***************
     *Setter Methods*
     ****************/
    public void setEnrollmentTotal(int enrollmentTotal) {
        this.enrollmentTotal = enrollmentTotal;
    }

    public void setEnrollmentCap(int enrollmentCap) {
        this.enrollmentCap = enrollmentCap;
    }
    public String getType() {
        return type;
    }

    /***************
    *Getter Methods*
    ****************/
    public int getEnrollmentCap() {
        return enrollmentCap;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public boolean isSameSection(String[] courseData){
        if(type.equalsIgnoreCase(courseData[courseData.length - 1])){
            return true;
        }
        return false;
    }

    @JsonIgnore
    public String getSectionTitle() {
        String s = "Type=" + type + ", Enrollment=" + enrollmentTotal + "/" + enrollmentCap;
        return s;
    }

    @Override
    public String toString(){
        return getSectionTitle();
    }

    /***************
     *  Comparator *
     ***************/
    //Sorts the compomentCodes in alphabetical order
    public static Comparator<Section> sortType = new Comparator<Section>(){
        public int compare(Section s1, Section s2){
            String a = s1.getType();
            String b = s2.getType();
            return a.compareTo(b);
        }
    };
}
