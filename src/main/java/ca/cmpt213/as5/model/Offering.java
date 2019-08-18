package ca.cmpt213.as5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static ca.cmpt213.as5.model.Section.sortType;

/*
 * Offering.java
 * Class Description: A Offering is in charge of storing
 *                    information about itself and the numerous
 *                    different sections that it may contain.
 * Last modified on: April 9th, 2018
 * Authors: Bowen He & Jordan Hoang
 * Emails : bhhe@sfu.ca & jha257@sfu.ca
 */
public class Offering {

    //Data variables
    private long courseOfferingId;
    private String location;
    private ArrayList<String> instructors;
    private String term;
    private int semester;
    private int year;
    private String offeringTitle;
    private ArrayList<Section> sections = new ArrayList<>();

    //keep track of sections of semester
    //
    private int firstDigit;
    private int middleTwoDigit;
    private int lastDigit;

    /****************
     * Constructors *
     ****************/
    public Offering(String semester, String location, ArrayList<String> instructors, Section section, long courseOfferingId){
        this.semester = Integer.parseInt(semester);
        this.location = location.trim();
        this.instructors = instructors;
        sections.add(section);
        this.courseOfferingId = courseOfferingId;
        setDigits();
        this.year = 1900 + (100 * firstDigit) + middleTwoDigit;
        setTerm();
    }

    public Offering(int semester, String location){
        this.semester = semester;
        this.location = location;
        setDigits();
        this.year = 1900 + (100 * firstDigit) + middleTwoDigit;
        setTerm();
    }

    /****************
     *Helper Methods*
     ****************/
    public void addSection(Section section){
        sections.add(section);
    }

    public void addToSection(String[] courseData) {
        for(Section section: sections){
            if(section.isSameSection(courseData)){
                addInstructors(courseData);
                section.addEnrollCapacity(Integer.parseInt(courseData[4]));
                section.addEnrollTotal(Integer.parseInt(courseData[5]));
            }
        }
    }

    public void addInstructors(String[] courseData){
        for(int i = 6; i < courseData.length - 1; i++){
            if(!hasInstructor(courseData[i]) && !courseData[i].contains("null")){
                this.instructors.add(courseData[i].replace("\"",""));
            }
        }
    }

    /***************
     *Setter Methods*
     ****************/
    private void setDigits(){
        this.firstDigit = (this.semester / 1000);
        this.middleTwoDigit = (this.semester /10) % 100;
        this.lastDigit = this.semester % 10;
    }

    private void setTerm() {
        if(lastDigit == 1){
            term = "Spring";
        }else if(lastDigit == 4){
            term = "Summer";
        }else if(lastDigit == 7){
            term = "Fall";
        }else{
            term = "Unknown";
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public void setInstructors(ArrayList<String> instructors) {
        this.instructors = instructors;
    }

    /***************
     *Getter Methods*
     ****************/
    public int getSemester() {
        return semester;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<String> getInstructors() {
        return instructors;
    }


    @JsonIgnore
    public ArrayList<Section> getSections() {
        return sections;
    }

    @JsonIgnore
    public String getOfferingTitle(){
        String instructorString = "";
        for(String s: instructors){
            instructorString += s + ", ";
        }
        offeringTitle = semester + " in " + location + " by " + instructorString;
        return offeringTitle;
    }

    public int getYear() {
        return year;
    }

    public String getTerm() {
        return term;
    }

    public long getCourseOfferingId() {
        return courseOfferingId;
    }

    public int getLecTotal(){
        int totStudents = 0;
        for (Section s : sections) {
            if (s.getType().contains("LEC"))
                totStudents += s.getEnrollmentTotal();
        }
        return totStudents;
    }
    /***************
     *Boolean Checks*
     ****************/
    public boolean isSameOffering(String[] courseData) {

        if(location.trim().equalsIgnoreCase(courseData[3].trim()) &&
            semester == Integer.parseInt(courseData[0])){
            return true;
        }
        return false;
    }

    public boolean hasSection(String[] courseData) {
        for(Section section: sections){
            if(section.isSameSection(courseData)){
                return true;
            }
        }
        return false;
    }

    public boolean hasInstructor(String checkInstructor){
        for(String instructor: instructors){
            if(checkInstructor.contains(instructor)) {
                return true;
            }
        }
        return false;
    }

    //Checks if the semester is in fall, spring or summer
    public boolean isValidSemester(){
        if(lastDigit == 4 || lastDigit == 7 || lastDigit == 1){
            return true;
        }
        return false;
    }

    /***************
     *  toString   *
     ***************/
    public String toString(){
        String output = getOfferingTitle();
        for(Section section: sections){
            output += "\n         " + section;
        }
        return output;
    }

    /***************
     *  Comparator *
     ***************/
    public static Comparator<Offering> sortOffering = new Comparator<Offering>(){
        public int compare(Offering a1, Offering a2){

            int sem1 = a1.getSemester();
            int sem2 = a2.getSemester();

            if(sem1 != sem2){
                if(sem1 > sem2){
                    return 1;
                } else{
                    return -1;
                }
            }

            //Then they must be the same so..
            String loc1 = a1.getLocation();
            String loc2 = a2.getLocation();

            return loc1.compareTo(loc2);

        }
    };

    public void sortSections(){
        Collections.sort(sections,sortType);
    }
}
