package com.umproject.Cards;

import com.umproject.Utils.ReaderCsv;

public class CourseCard {
    
    //Import data from file (might have a more effective way)
    private final String[][] data1 = ReaderCsv.getData()[0];
    private final String[][] data2 = ReaderCsv.getData()[5];

    private final int rowCount = data1.length; //Number of rows of currentGrades
    private final int colCount = data1[0].length; //Number of columns of currentGrades
    private final double[][] arrayWithoutNG = new double[rowCount - 1][colCount - 1]; //Initialize an array of student grades without NGs
    private final double[] sums = new double[rowCount - 1]; //An array to store the sum of grades that each student obtains
    private final double[] averages = new double[rowCount - 1]; //An array to store the average grades that each student obtains
    private final double[] grades = new double[rowCount - 1]; //An array to store the number of grades that each student obtains
    private final double[] NGs = new double[rowCount - 1]; //An array to store the number of NGs that each student obtains


    //Initialize a 3D array to store course cards with student id and grades
    public String[][][] infoArray = new String[colCount - 1][rowCount][5];

    public String[][][] courseInfo(String[][] data) {

        processGrades(data);

        //Store student numbers, first row, and actual grades of each course to infoArray
        for (int i = 0; i < colCount - 1; i++) {

            infoArray[i][0][0] = "Course " + data[0][i + 1];
            infoArray[i][0][1] = "Grade"; //store course names and the student's corresponding grade of the course
            infoArray[i][0][2] = "Student Average Grade"; //store the student's average grade
            infoArray[i][0][3] = "Grade Count"; //store the number of grades that the student obtains
            infoArray[i][0][4] = "NG count"; //store the number of NGs that the student obtains

            for (int j = 1; j < rowCount; j++){
                infoArray[i][j][0] = data[j][0]; //place student id on the 0th column of infoArray
                infoArray[i][j][1] = data[j][i + 1]; //place actual grades on the 1st column of infoArray
            }
        }

        return infoArray;

    }

    // The method computes sums, averages and count NGs for each course in the given CSV file and stores them in the infoArray.
    // The method also computes the number of NGs and grades for each student in the given CSV file and stores them in the infoArray.
    public void processGrades(String[][] data) {

        for(int i = 1; i < rowCount; i++){

            for(int j = 1; j < colCount; j++){

                String value = data[i][j];

                //If there is NG, turn NG to 0 in arrayWithoutNG
                if(value.equals("NG") || value.isEmpty()){
                    arrayWithoutNG[i-1][j-1] = 0;

                    //Keep track of how many NGs that a student has
                    if (i-1 < NGs.length){
                        NGs[i-1]++;
                    }
                }
                //If it is a number, copy the value to arrayWithoutNG
                else {
                    try {
                        //Keep track of how many grades that a student has
                        grades[i-1]++;

                        arrayWithoutNG[i-1][j-1] = Double.parseDouble(value);

                        //Sum up all student's grades of each course
                        if (i-1 < sums.length) {
                            sums[i-1] += arrayWithoutNG[i-1][j-1];
                        }

                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing number at position [" + i + "][" + j + "]: " + e.getMessage());
                        arrayWithoutNG[i-1][j-1] = 0;
                    }
                }
            }
        }


        //Calculate the average grade of each student without NGs
        for(int i = 0; i < sums.length; i++) {
            averages[i] = sums[i] / ((colCount - 1) - NGs[i]);
        }

        //Store the average grade of each course, the number of NGs and grades, of each student to infoArray
        for (int i = 0; i < colCount - 1; i++) {
            for (int j = 1; j < rowCount; j++){
                infoArray[i][j][2] = Double.toString(averages[j-1]); //place averages on the 2nd column of infoArray
                infoArray[i][j][3] = Double.toString(grades[j-1]); //place the number of grades on the 3rd column of infoArray
                infoArray[i][j][4] = Double.toString(NGs[j-1]); //place the number of NGs on the 4th column of infoArray
            }
        }

    }


    public String[][] coursesArray(String input, boolean checked) {

        String[][] data = checked? data2 : data1;

        for (int i = 1; i < colCount; i++) {
            if (data[0][i].equals(input)) {
                return courseInfo(data)[i-1];
            }
        }
        return null;
    }
}
