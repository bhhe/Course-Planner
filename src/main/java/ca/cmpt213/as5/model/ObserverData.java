package ca.cmpt213.as5.model;

/*
 * ObserverData.java
 * Class Description: A JsonBody that is read and to be
 *                    converted to appropriate data to be
 *                    used for a observer
 * Last modified on: April 9th, 2018
 * Authors: Bowen He & Jordan Hoang
 * Emails : bhhe@sfu.ca & jha257@sfu.ca
 */
public class ObserverData {

    //keep track of ids observer is
    //watching
    private long deptId;
    private long courseId;

    /****************
     * Constructors *
     ****************/
    public ObserverData(){

    }

    public ObserverData(String deptId, String courseId){
        this.deptId = Long.parseLong(deptId);
        this.courseId = Long.parseLong(courseId);
    }

    /***************
     *Setter Methods*
     ****************/
    public void setDeptId(String deptId) {
        this.deptId = Long.parseLong(deptId);
    }

    public void setCourseId(String courseId) {
        this.courseId = Long.parseLong(courseId);
    }

    /***************
     *Getter Methods*
     ****************/
    public long getDeptId() {
        return deptId;
    }

    public long getCourseId() {
        return courseId;
    }
}
