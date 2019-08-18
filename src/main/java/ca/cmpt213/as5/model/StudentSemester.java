package ca.cmpt213.as5.model;

import java.util.ArrayList;
import java.util.Comparator;

/** A class that stores information on the number of courses taken in a faculty
 * in a particular time
 */
public class StudentSemester {
    private int semesterCode;
    private int totalCoursesTaken;

    //Constructor
    public StudentSemester() {
    }

    /**
     * Constructor that takes in a semesterCode and the totalCourses taken in that semester
     * @param semesterCode
     * @param totalCoursesTaken
     */
    public StudentSemester(int semesterCode, int totalCoursesTaken){
        this.semesterCode = semesterCode;
        this.totalCoursesTaken = totalCoursesTaken;
    }

    //getters
    public int getSemesterCode() {
        return semesterCode;
    }

    public int getTotalCoursesTaken() {
        return totalCoursesTaken;
    }

    //setters
    public void setSemesterCode(int semesterCode) {
        this.semesterCode = semesterCode;
    }

    public void setTotalCoursesTaken(int totalCoursesTaken) {
        this.totalCoursesTaken = totalCoursesTaken;
    }


    //Sort by semester in ascending order
    public static Comparator<StudentSemester> studentSemsterSort = new Comparator<StudentSemester>(){
        public int compare(StudentSemester objOne , StudentSemester objTwo){
            int a = objOne.getSemesterCode();
            int b = objTwo.getSemesterCode();

            if(a>b){
                return 1;
            } else if(a==b){
                return 0;
            }
            return -1;
        }
    };


    /** Combines elements inside an StudentSemester List that have the same semesterCode
     * @param myList - List of things you want to combine that have the same semester
     * @return the combined list
     */
    public static ArrayList<StudentSemester> combineCode(ArrayList<StudentSemester> myList){

        ArrayList<StudentSemester> result = new ArrayList<>();
        StudentSemester a = new StudentSemester();

        int totStudents = 0;
        boolean firstTime = true;

        for(int i = 0 ; i < myList.size() ; i++){
            if(firstTime){
                a = new StudentSemester();
                a.setSemesterCode(myList.get(i).getSemesterCode());
                totStudents += myList.get(i).getTotalCoursesTaken();
                firstTime = false;
            } else{
                if(a.getSemesterCode() == myList.get(i).getSemesterCode()){
                   totStudents += myList.get(i).getTotalCoursesTaken();
                } else{
                    //if they don't have the same semester code then we add it to the list and move on
                    a.setTotalCoursesTaken(totStudents);
                    result.add(a);
                    totStudents = 0;
                    i--;
                    firstTime = true;
                }
            }
        }
        a.setTotalCoursesTaken(totStudents);
        result.add(a);

        return result;
    }

    /**Checks the last digit,
     * and returns the next semester
     * Ex. 1 is fall, 4 is spring and 7 is summer
     */
    private static int getIncrement(int i){
        if(i == 1 || i == 4)
            return 3;

        return 4;
    }

    /**
     * Iterates through the arrayList and dates that we have no information on are added to the
     * list with 0 courses
     * @param myList - The list you want to addData too
     * @param beginDate - The first known semesterCode known
     * @param endDate - The last known semesterCode known
     * @return - The list with the added StudentSemesters
     */
    public static ArrayList<StudentSemester> addDate(ArrayList<StudentSemester> myList, int beginDate, int endDate){
        int inc = beginDate;
        int currDate = beginDate;
        for(int i = beginDate ; i <= endDate;){
            myList.add(new StudentSemester(currDate,0));

            inc = beginDate % 10;
            inc = getIncrement(inc);

            //update counter for loop
            i += inc;
            //update the date to add into the list
            currDate += inc;
            beginDate += inc;
        }
        return myList;
    }

}
