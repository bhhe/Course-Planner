package ca.cmpt213.as5.model;

import java.util.ArrayList;
import java.util.Date;

/*
 * CourseObserver.java
 * Class Description: An observer inserted into a course
 *                    that monitors and records any changes
 *                    made to the course class.
 * Last modified on: April 9th, 2018
 * Authors: Bowen He & Jordan Hoang
 * Emails : bhhe@sfu.ca & jha257@sfu.ca
 */
public class CourseObserver {

    //data variables
    private long id;
    private Department department;
    private Course course;
    private ArrayList<String> events = new ArrayList<>();

    /****************
     * Constructors *
     ****************/
    public CourseObserver(){
    }

    public CourseObserver(long id, Department department, Course course){
        this.id = id;
        this.department = department;
        this.course = course;
    }

    /****************
     *Helper Methods*
     ****************/
    public void addToEvents(String event){
        events.add(event);
    }

    /***************
     *Setter Methods*
     ****************/
    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    /***************
     *Getter Methods*
     ****************/
    public Department getDepartment() {
        return department;
    }

    public Course getCourse() {
        return course;
    }

    public long getId() {
        return id;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    /***************
     *Boolean Checks*
     ****************/
    public boolean isSameObserver(long id){
        if(this.id == id){
            return true;
        }
        return false;
    }
}