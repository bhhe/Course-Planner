package ca.cmpt213.as5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static ca.cmpt213.as5.model.Course.sortCourse;

/*
 * Department.java
 * Class Description: A Department is in charge of storing
 *                    information about itself and the
 *                    variety of courses it contain.
 * Last modified on: April 9th, 2018
 * Authors: Bowen He & Jordan Hoang
 * Emails : bhhe@sfu.ca & jha257@sfu.ca
 */
public class Department {

    //data variables
    private long deptId;
    private String name;
    private ArrayList<Course> courses = new ArrayList<>();

    /****************
     * Constructors *
     ****************/
    public Department(){

    }

    public Department(String name, long deptId){
        this.name = name.trim();
        this.deptId = deptId;
    }

    /****************
     *Helper Methods*
     ****************/
    //Course Exists Already
    public void addNewOffering(String[] courseData, Long offeringID) {
        for(Course course: courses){
            if(course.isSameCourse(courseData)){
                Offering offering = createOffering(courseData,offeringID);
                course.addToOfferings(courseData,offering);
            }
        }
    }

    //Add new course to department
    public void addNewCourse(String[] courseData, Long courseID, Long offeringID) {
        Offering offering = createOffering(courseData,offeringID);
        Course course = new Course(courseData[1],courseData[2],offering, courseID);
        courses.add(course);
    }

    //Adds to existing offering
    public void addToOffering(String[] courseData) {
        for(Course course: courses){
            if(course.hasOffering(courseData)){
                course.addToOffering(courseData);
            }
        }
    }

    //Adds new offering
    private Offering createOffering(String[] courseData, long offeringID){
        String componentCode = courseData[courseData.length - 1];
        int enrollCapacity = Integer.parseInt(courseData[4]);
        int enrollTotal = Integer.parseInt(courseData[5]);

        ArrayList<String> instructors = new ArrayList<>();

        for (int i = 6; i < courseData.length - 1; i++) {

            //make sure instructor is not a null var
            if(!courseData[i].contains("null")) {
                instructors.add(courseData[i].trim().replace("\"","").trim());
            }
        }

        Section section = new Section(componentCode,enrollCapacity,enrollTotal);
        Offering offering = new Offering(courseData[0], courseData[3],
                                         instructors, section, offeringID);
        return offering;
    }

    /***************
     *Setter Methods*
     ****************/
    public void setName(String name){
        this.name = name;
    }

    /***************
     *Getter Methods*
     ****************/
    public long getDeptId() {
        return deptId;
    }

    public String getName(){
        return this.name;
    }

    @JsonIgnore
    public ArrayList<Course> getCourses(){
        return courses;
    }


    /***************
     *Boolean Checks*
     ****************/
    public boolean isSameDepartment(String name){
        if(this.name.equalsIgnoreCase(name)){
            return true;
        }
        return false;
    }

    public boolean courseExists(String[] courseData){
        for(Course course: courses){
            if(course.isSameCourse(courseData)){
                return true;
            }
        }
        return false;
    }

    public boolean offeringExists(String[] courseData) {
        for(Course course: courses){
            if(course.hasOffering(courseData) && course.getCourseName().equalsIgnoreCase(courseData[1])
                    && course.getCatalogNumber().equalsIgnoreCase(courseData[2])){
                return true;
            }
        }
        return false;
    }

    /***************
     *  toString   *
     ***************/
    public String toString(){
        String output = "Department: " + this.name;
        for(Course course: courses){
            output += "\n   " + course;
        }
        return output;
    }

    /***************
     *  Comparator *
     ***************/
    public static Comparator<Department> sortDept = new Comparator<Department>(){
        public int compare(Department s1, Department s2){
            String dept1 = s1.getName();
            String dept2 = s2.getName();
            return dept1.compareTo(dept2);
        }
    };

    public void sortCourses(){
        Collections.sort(courses,sortCourse);

        for(Course c: courses){
            c.sortOfferings();
        }

    }

}
