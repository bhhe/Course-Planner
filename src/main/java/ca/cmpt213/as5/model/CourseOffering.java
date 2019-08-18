package ca.cmpt213.as5.model;

/*
 * CourseOffering.java
 * Class Description: A Json body converted into an object to
 *                    to added into the model database for course
 *                    planner
 * Last modified on: April 9th, 2018
 * Authors: Bowen He & Jordan Hoang
 * Emails : bhhe@sfu.ca & jha257@sfu.ca
 */
public class CourseOffering {

    //data variables
    private String semester;
    private String subjectName;
    private String catalogNumber;
    private String location;
    private int enrollmentCap;
    private String component;
    private int enrollmentTotal;
    private String instructor;

    /****************
     * Constructors *
     ****************/
    public CourseOffering(){

    }

    public CourseOffering(String semester, String subjectName, String catalogNumber,
                          String location, String enrollmentCap, String component,
                          String enrollmentTotal, String instructor) {
        this.semester = semester;
        this.subjectName = subjectName;
        this.catalogNumber = catalogNumber;
        this.location = location;
        this.enrollmentCap = Integer.parseInt(enrollmentCap);
        this.component = component;
        this.enrollmentTotal = Integer.parseInt(enrollmentTotal);
        this.instructor = instructor;
    }

    /****************
     *Helper Methods*
     ****************/
    public String[] convertToCourseData(){
        String[] courseData = toString().split(",");
        return courseData;
    }

    /***************
     *Getter Methods*
     ****************/
    public String getSemester() {
        return semester;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public String getLocation() {
        return location;
    }

    public int getEnrollmentCap() {
        return enrollmentCap;
    }

    public String getComponent() {
        return component;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public String getInstructor() {
        return instructor;
    }

    /***************
     *  toString   *
     ***************/
    public String toString(){
        String returnString = semester + "," + subjectName + "," + catalogNumber +
                              "," + location + "," + enrollmentCap + "," +
                              enrollmentTotal + "," + instructor + "," + component;
        return returnString;
    }
}
