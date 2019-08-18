package ca.cmpt213.as5.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static ca.cmpt213.as5.model.Department.sortDept;

public class FileProcessor {
    //Keep track of lines scanned for testing purposes
    private int numOfLines = 0;

    /****************
     * Constructors *
     ****************/
    public FileProcessor(){

    }

    //Dump File data from Model into a txt file
    public void writeToFile(String fileName, Model model){
        ArrayList<Department> departments = model.getDepartments();
        Collections.sort(departments,sortDept);
        File targetFile = new File(fileName);
        try {
            PrintWriter writer = new PrintWriter(targetFile);

            //write to file for every department
            for (Department department : departments) {
                writer.println(department);
            }
            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println("Failed to create file");
        }
    }

    //Process data file into Model and returns true if successful
    public boolean processFile(String sourcePath, Model model) {
        File sourceFile = new File(sourcePath);
        try {
            Scanner scanner = new Scanner(sourceFile);

            //Skip line number 1 since it's not data
            scanner.nextLine();

            // Read from the csv file:
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] courseData = data.split(",");

                //Eliminate all white spaces on side
                for(int i = 0; i < courseData.length; i++){
                    courseData[i] = courseData[i].trim();
                }
                addToModel(courseData, model);
                numOfLines++;
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
        model.sort();
        return true;
    }

    //Adds Array Of Data to appropriate location
    //inside model
    public void addToModel(String[] courseData, Model model){
        String deptName = courseData[1].trim();
        if (model.departmentExists(deptName)) {
            model.addToDept(courseData, deptName);
        } else {
            model.addNewDept(courseData);
        }
    }

    /***************
     *Print Methods*
     ***************/
    //for testing purposes
    private void printCourseData(String[] courseData){
        for(String s: courseData){
            System.out.print(s + ",");
        }
        System.out.println();
    }

}
