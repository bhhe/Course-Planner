package ca.cmpt213.as5.controllers;

import ca.cmpt213.as5.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ca.cmpt213.as5.model.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

import static ca.cmpt213.as5.model.StudentSemester.studentSemsterSort;

/*
 * CourseController.java
 * Class Description: Controller methods for a Web UI Course Planner
 * Last modified on: April 8th, 2018
 * Authors: Bowen He & Jordan Hoang
 * Emails : bhhe@sfu.ca & jha257@sfu.ca
 */

@RestController
public class CourseController {
    private ArrayList<Course> courses = new ArrayList<>();
    private String inputFile = "data/course_data_2018.csv";
    private String outputFile = "docs/output_dump.txt";
    Model model = new Model();
    FileProcessor processor = new FileProcessor();
    boolean filedScanned = processor.processFile(inputFile,model);
    private AtomicLong id = new AtomicLong();
    private ArrayList<CourseObserver> observers = new ArrayList<CourseObserver>();

    /*********************
     *General Controllers*
     *********************/
    @GetMapping("api/about")
    public Object aboutGame(){
        return new Object(){
            public String appName = "Course Planner";
            public String authorName = "Bowen He & Jordan Hoang";
        };
    }

    @GetMapping("api/dump-model")
    public Model dumpModel(){
        processor.writeToFile(outputFile,model);
        model.dumpModel();
        return model;
    }

    /*****************************************************************
     *Access Departments, Courses, Offerings, and Sections Controller*
     *****************************************************************/
    @GetMapping("api/departments")
    public ArrayList<Department> getDepartments(){
        return model.getDepartments();
    }

    @GetMapping("/api/departments/{deptId}/courses")
    public ArrayList<Course> getCourses(@PathVariable("deptId") Long deptId)
                                        throws ResourceNotFoundException{
        Department dept = getDepartment(deptId);
        return dept.getCourses();
    }

    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings")
    public ArrayList<Offering> getOfferings(@PathVariable("deptId") long deptId,
                                            @PathVariable("courseId") long courseId)
                                            throws ResourceNotFoundException{

        ArrayList<Course> courses = getCourses(deptId);
        Course course = getCourse(courses, courseId);
        return course.getOfferings();
    }

    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings/{offeringId}")
    public ArrayList<Section> getSections(@PathVariable("deptId") long deptId,
                                          @PathVariable("courseId") long courseId,
                                          @PathVariable("offeringId") long offeringId)
                                          throws ResourceNotFoundException{

        ArrayList<Offering> offerings = getOfferings(deptId,courseId);
        Offering offering = getOffering(offerings, offeringId);
        return offering.getSections();
    }

    /*************************
     * Graph Data Controller *
     *************************/
    @GetMapping("/api/stats/students-per-semester")
    private ArrayList<StudentSemester> getGraphData(@RequestParam("deptId") long deptId)
            throws ResourceNotFoundException{
        ArrayList<StudentSemester> mySet = new ArrayList<>();
        Department myDepty = getDepartment(deptId);

        //Return this to the original
        ArrayList<Course> listCourses = myDepty.getCourses();
        for (Course a : listCourses) {
            ArrayList<Offering> myOffer = a.getOfferings();
            processOffers(myOffer,mySet);
        }
        Collections.sort(mySet, studentSemsterSort);
        mySet = StudentSemester.combineCode(mySet);

        return mySet;
    }

    /*************************
     *Add Offering Controller*
     *************************/
    @PostMapping("/api/addoffering")
    public Section addCourseOffering(@RequestBody CourseOffering courseOffering){
        String[] courseData = courseOffering.convertToCourseData();
        processor.addToModel(courseData, model);
        model.sort();
        Section addedSection = new Section(courseOffering.getComponent(),
                                           courseOffering.getEnrollmentCap(),
                                           courseOffering.getEnrollmentTotal());
        return addedSection;
    }

    /****************************
     *Course Watcher Controllers*
     ****************************/
    @GetMapping("/api/watchers")
    public ArrayList<CourseObserver> getObservers(){
        return observers;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/api/watchers")
    public CourseObserver addObserver(@RequestBody ObserverData observerData) throws ResourceNotFoundException{
        Department dept = getDepartment(observerData.getDeptId());
        Course course = getCourse(dept.getCourses(),observerData.getCourseId());
        CourseObserver observer = new CourseObserver(id.incrementAndGet(),dept, course);
        course.addObserver(observer);
        observers.add(observer);

        return observer;
    }

    @GetMapping("/api/watchers/{id}")
    public CourseObserver getObservers(@PathVariable("id") long id) throws ResourceNotFoundException{
        return getObserver(id);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/watchers/{id}")
    public void removeObserver(@PathVariable("id") long id)throws ResourceNotFoundException{
        CourseObserver observer = getObserver(id);
        observer.getCourse().removeObserver(observer);
        observers.remove(observer);
    }

    /*********************
     *Help/Getter Methods*
     *********************/
    public CourseObserver getObserver(long id)throws ResourceNotFoundException{

        for(CourseObserver observer: observers){
            if(observer.isSameObserver(id)){
                return observer;
            }
        }
        throw new ResourceNotFoundException("Observer of ID " + id + " is not found.");
    }

    //get Department with desired ID
    public Department getDepartment(Long deptId)throws ResourceNotFoundException{

        ArrayList<Department> departments = model.getDepartments();
        for(Department d: departments){
            if(d.getDeptId() == deptId) {
                return d;
            }
        }
        throw new ResourceNotFoundException("Department of ID " + deptId + " not found.");
    }

    //get Course with desired ID
    public Course getCourse(ArrayList<Course> courses, long courseId)
                            throws ResourceNotFoundException {

        for(Course c: courses){
            if(c.getCourseId() == courseId){
                return c;
            }
        }
        throw new ResourceNotFoundException("Course of ID " + courseId + " not found.");
    }

    //Get Offering with desired ID
    public Offering getOffering(ArrayList<Offering> offerings, long offeringId)
                                throws ResourceNotFoundException{

        for(Offering o: offerings){
            if(o.getCourseOfferingId() == offeringId){
                return o;
            }
        }
        throw new ResourceNotFoundException("Course offering of ID " + offeringId + " not found.");
    }



    private static void processOffers(ArrayList<Offering> myOffer, ArrayList<StudentSemester> mySet){
        int beginDate = 0;
        int endDate = 0;
        for (Offering o : myOffer) {

            int semCode = o.getSemester();
            //Find the beginning date and the ending date
            if (beginDate == 0) {
                beginDate = o.getSemester();
                endDate = o.getSemester();
            } else {
                if (o.getSemester() < beginDate) {
                    beginDate = o.getSemester();
                }
                if (o.getSemester() > endDate) {
                    endDate = o.getSemester();
                }
            }

            if (o.isValidSemester()) {
                StudentSemester data = new StudentSemester(semCode,o.getLecTotal());
                mySet.add(data);
            }
        }
        mySet = StudentSemester.addDate(mySet, beginDate, endDate);
    }
}
