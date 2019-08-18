package ca.cmpt213.as5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static ca.cmpt213.as5.model.Offering.sortOffering;

/*
 * Course.java
 * Class Description: A Course is in charge of storing
 *                    information about itself and the
 *                    variety of offerings of the course.
 * Last modified on: April 9th, 2018
 * Authors: Bowen He & Jordan Hoang
 * Emails : bhhe@sfu.ca & jha257@sfu.ca
 */
public class Course {

    //data variables
    private long courseId;
    private String catalogNumber;
    private String courseTitle;
    private String courseName;
    private ArrayList<Offering> offerings = new ArrayList<>();
    private ArrayList<CourseObserver> observers = new ArrayList<>();
    Date today;

    /****************
     * Constructors *
     ****************/
    public Course(){

    }

    public Course(String courseName, String catalogNumber, Offering offering, Long courseId){
        this.courseName = courseName.trim();
        this.catalogNumber = catalogNumber.trim();
        this.courseId = courseId;
        offerings.add(offering);
    }

    public Course(String courseName, String catalogNumber, Offering offering){
        this.courseName = courseName.trim();
        this.catalogNumber = catalogNumber.trim();
        offerings.add(offering);
    }

    /****************
     *Helper Methods*
     ****************/

    //Add new offering to the course
    //and notify change to observers
    public void addToOfferings(String[] courseData,Offering offerToAdd){
        offerings.add(offerToAdd);
        if(observers.size() != 0) {
            notifyObservers(courseData, offerToAdd);
        }
    }

    //Assuming existing offering exists
    //add/change data of existing offer
    //and notify changes to observers
    public void addToOffering(String[] courseData){
        for(Offering offering: offerings){
            if(offering.isSameOffering(courseData)){
                addToSections(courseData,offering);
                if(observers.size() != 0) {
                    notifyObservers(courseData, offering);
                }
            }
        }

    }

    private void addToSections(String[] courseData, Offering offering){
        if(offering.hasSection(courseData)){
            offering.addToSection(courseData);
        }else{
            Section section = new Section(courseData[courseData.length - 1],
                                          Integer.parseInt(courseData[4]),
                                          Integer.parseInt(courseData[5]));
            offering.addSection(section);
            offering.addInstructors(courseData);
        }
    }

    /***************
     *Getter Methods*
     ****************/
    @JsonIgnore
    public String getCourseTitle(){
        courseTitle = courseName + " " + catalogNumber;
        return courseTitle;
    }

    @JsonIgnore
    public ArrayList<Offering> getOfferings(){
        return offerings;
    }

    public String getCatalogNumber(){
        return catalogNumber;
    }

    @JsonIgnore
    public String getCourseName(){
        return courseName;
    }

    public long getCourseId() {
        return courseId;
    }

    @JsonIgnore
    public ArrayList<CourseObserver> getObservers() {
        return observers;
    }

    /***************
     *  toString   *
     ***************/
    public String toString(){
        String output = getCourseTitle();
        for(Offering offering: offerings){
            output += "\n      " + offering;
        }
        return output;
    }

    /***************
     *Boolean Checks*
     ****************/
    public boolean hasOffering(String[] courseData){
        for(Offering offering: offerings){
            if(offering.isSameOffering(courseData) &&
                    courseData[1].equalsIgnoreCase(courseName) &&
                    courseData[2].equalsIgnoreCase(catalogNumber)){
                return true;
            }
        }
        return false;
    }

    public boolean isSameCourse(String[] courseData){
        return (this.catalogNumber.trim().equalsIgnoreCase(courseData[2].trim()) &&
                this.courseName.trim().equalsIgnoreCase(courseData[1].trim()));
    }

    /***************
     *  Comparator *
     ***************/
    public static Comparator<Course> sortCourse = new Comparator<Course>(){
        public int compare(Course a1, Course a2){
            String courseOne = a1.getCatalogNumber();
            String courseTwo = a2.getCatalogNumber();

            return courseOne.compareTo(courseTwo);
        }
    };

    public void sortOfferings() {
        Collections.sort(offerings,sortOffering);
        for(Offering o: offerings){
            o.sortSections();
        }
    }

    /******************
     *Observer Methods*
     ******************/
    public void addObserver(CourseObserver observer){
        observers.add(observer);
    }

    public void removeObserver(CourseObserver observer){
        observers.remove(observer);
    }

    private void notifyObservers(String[] courseData, Offering offering) {
        today = Calendar.getInstance().getTime();
        String event = today + ": Added section " + courseData[7] +
                       " with enrollment (" + courseData[5] + "/" + courseData[4] +
                        ") to offering " + offering.getTerm() + " " + offering.getYear();
        //Add the event to all observer records
        for (CourseObserver observer : observers) {
            observer.addToEvents(event);
        }
    }
}
