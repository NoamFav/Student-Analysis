package com.umproject.Cards;

import com.umproject.Utils.ReaderCsv;

public class graduateCourseCard {
    private static final String[][] data = ReaderCsv.getData()[1];
    
    //Import data from file (might have a more effective way)

    private final int rowCount = data.length; //Number of rows of currentGrades
    private final int colCount = data[0].length; //Number of columns of currentGrades
    private final double[] sums = new double[rowCount - 1]; //An array to store the sum of grades that each student gets
    private final double[] averages = new double[rowCount - 1]; //An array to store the average grades that each student gets

    //Initialize a 3D array to store course cards with student id and grades
    public String[][] infoArray = new String[rowCount][3];
    public String[][] courseInfo(String id){

        double[] averageArray = processGrades();

        //Use a for loop to find the course name in the graduate student database
        for (int i = 1; i < colCount; i++) {

            //if found, insert the all information of the course to the infoArray
            if (data[0][i].equals(id)){

                infoArray[0][0] = "Student ID"; 
                infoArray[0][1] = data[0][i] + " Grade"; //store course names and the student's corresponding grade of the course
                infoArray[0][2] = "Student Average Grade"; //store the student's average grade

                for (int j = 1; j < rowCount; j++){
                    infoArray[j][0] = data[j][0]; //place student id on the 0th column of infoArray
                    infoArray[j][1] = data[j][i]; //place actual grades on the 1st column of infoArray
                    infoArray[j][2] = Double.toString(averageArray[j-1]); //place student average grades on the 2nd column of infoArray
                }

                return infoArray;

            }

        }

        //If the course name is invalid

        System.out.println("The course name is not valid. Please enter a different number.");

        return null;

    }

        
    // Method to process and compute averages for the grades in the given file.
    // The method computes sums and averages for each course in the given CSV file and returns the average array.
    public double[] processGrades() {

        for(int i = 1; i < rowCount; i++){

            for(int j = 1; j < colCount; j++){

                String value = data[i][j];

                try {
                       
                    //Sum up all graduate students grades of each student
                    if (i-1 < sums.length) {
                        sums[i-1] += Double.parseDouble(value);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number at position [" + i + "][" + j + "]: " + e.getMessage());
                        
                }
            }
        }
    

        //Calculate the average grade of each student
        for(int i = 0; i < sums.length; i++) {
            averages[i] = sums[i] / (colCount - 1);
        }

        return averages;
        
        
    }
    public String[][] getCourseInfo(String courseName){ return courseInfo(courseName); }
}
