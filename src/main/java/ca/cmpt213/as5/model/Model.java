package ca.cmpt213.as5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;
import static ca.cmpt213.as5.model.Department.sortDept;

/*
 * Model.java
 * Class Description: Main Database in charged of storing all
 *                    Information of the Course Planner. Mainly
 *                    the different departments.
 * Last modified on: April 9th, 2018
 * Authors: Bowen He & Jordan Hoang
 * Emails : bhhe@sfu.ca & jha257@sfu.ca
 */
public class Model {

    //data variables for database
    private ArrayList<Department> departments = new ArrayList<Department>();
    private AtomicLong deptID = new AtomicLong();
    private AtomicLong courseID = new AtomicLong();
    private AtomicLong offeringID = new AtomicLong();
    private long depts;
    private long courses;
    private long offerings;

    /****************
     * Constructors *
     ****************/
    public Model(){
        //Make sure nextIdsGet is starting from 0
        deptID.decrementAndGet();
        courseID.decrementAndGet();
        offeringID.decrementAndGet();
    }

    /***************
     *Boolean Checks*
     ****************/
    @JsonIgnore
    public boolean departmentExists(String deptName){
        for(Department department: departments){
            if(department.isSameDepartment(deptName)){
                return true;
            }
        }
        return false;
    }

    /****************
     *Helper Methods*
     ****************/

    //assumed dept exists
    public void addToDept(String[] courseDatatest, String deptID) {
        String[] courseData = courseDatatest;
        String courseName = courseData[1].trim();
        for(Department department: departments){
            if(department.isSameDepartment(courseName)){
                //if course exists
                if(department.courseExists(courseData)){
                    //if offering exists
                    if(department.offeringExists(courseData)){

                        //alter offering
                        department.addToOffering(courseData);
                    }else{

                        //add new offering
                        department.addNewOffering(courseData,offeringID.incrementAndGet());
                    }

                    //course doesn't exist, add new course and offering
                }else{
                    department.addNewCourse(courseData,courseID.incrementAndGet(),offeringID.incrementAndGet());
                }
            }
        }
    }

    public void addNewDept(String[] courseData) {
        String deptName = courseData[1].trim();
        Department department = new Department(deptName, deptID.incrementAndGet());
        department.addNewCourse(courseData, courseID.incrementAndGet(), offeringID.incrementAndGet());
        departments.add(department);
    }

    /***************
     *Print Methods*
     ***************/
    public void printDepartments(){
        int total = 0;
        for(Department d: departments){
            System.out.println(d.getDeptId() + ": " + d.getName() +  " " + d.getCourses().size());
            total += d.getCourses().size();
        }

        System.out.println("TotalNumOfCourses: " + total);
    }

    public void printCourses(){
        for(Department d: departments){
            System.out.println("Department: " + d.getName());
            System.out.println("CoursesNum: " + d.getCourses().size());
            for(Course c: d.getCourses()){
                System.out.println(c.getCourseTitle() + " " + c.getCourseId());
            }
            System.out.println();
        }
    }

    public void printNumCourses(){
        System.out.println("CourseIds up to: " + courseID);
        System.out.println("OfferingIDs up to: " + offeringID);
    }

    //Temporary for Testing purposes
    public void dumpModel(){
        for(Department d: departments){
            System.out.println(d);
        }
    }

    //Temporary for Testing purposes
    public void dumpSpecific(String department){
        for(Department d: departments){
            if(d.getName().equalsIgnoreCase(department)) {
                System.out.println(d);
            }
        }
    }

    /***************
     *Getter Methods*
     ****************/
    public long getDeptsCount(){
        this.depts = deptID.get() + 1;
        return depts;
    }

    @JsonIgnore
    public ArrayList<Department> getDepartments(){
        return departments;
    }

    public long getCoursesCount() {
        this.courses = courseID.get() + 1;
        return courses;
    }

    public long getOfferingsCount() {
        this.offerings = offeringID.get() + 1;
        return offerings;
    }

    /***************
     *  Comparator *
     ***************/
    public void sort(){
        Collections.sort(departments,sortDept);
        for(Department d: departments){
            d.sortCourses();
        }
    }
}
