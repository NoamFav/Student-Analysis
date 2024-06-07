package com.umproject.Cards;

import com.umproject.Utils.ReaderCsv;

public class studentCard {
    private final String[][] data1 = ReaderCsv.getData()[0];
    private final String[][] data2 = ReaderCsv.getData()[5];

    private final int rowCount = data1.length; //Number of rows of currentGrades
    private final int colCount = data1[0].length; //Number of columns of currentGrades
    private final double[][] arrayWithoutNG = new double[rowCount - 1][colCount - 1]; //Initialize an array of student grades without NGs
    private final double[] countersAllColumns = new double[colCount - 1]; //30 entries
    private final double[] sums = new double[colCount - 1]; //30 entries
    private final double[] averages = new double[colCount - 1]; //30 entries

    //Initialize a 3D array to store student cards with student id and grades
    public String[][][] infoArray = new String[rowCount - 1][colCount][6];

    public String[][][] studentInfo( String[][] data){

        processGrades(data);
        printMaxMin();

        //Store course names, first row, and actual grades of each course to infoArray
        for (int i = 0; i < rowCount - 1; i++) {
            
            infoArray[i][0][0] = "Student " + data[i + 1][0]; //store student id
            infoArray[i][0][1] = "Grades";
            infoArray[i][0][2] = "Average";
            infoArray[i][0][3] = "Min";
            infoArray[i][0][4] = "Max";
            infoArray[i][0][5] = "NG Count";

            for (int j = 1; j < colCount; j++){
                infoArray[i][j][0] = data[0][j]; //place course names on the 0th column of infoArray
                infoArray[i][j][1] = data[i+1][j]; //place actual grades on the 1st column of infoArray
            }
        }

        return infoArray;

    }


    // Method to process and compute averages for the grades in the given file.
    // The method computes sums, averages and count NGs for each course in the given CSV file and stores them in the infoArray.
    public void processGrades(String[][] data) {

        for(int i = 1; i < rowCount; i++){

            for(int j = 1; j < colCount; j++){

                String value = data[i][j];

                //If there is NG, turn NG to 0 in arrayWithoutNG
                if(value.equals("NG") || value.isEmpty()){
                    arrayWithoutNG[i-1][j-1] = 0;

                    //Keep track of how many NGs are there in a course
                    if (j-1 < countersAllColumns.length) {
                        countersAllColumns[j-1]++;
                    }
                }
                //If it is a number, copy the value to arrayWithoutNG
                else {
                    try {
                        arrayWithoutNG[i-1][j-1] = Double.parseDouble(value);

                        //Sum up all student's grades of each course
                        if (j-1 < sums.length) {
                            sums[j-1] += arrayWithoutNG[i-1][j-1];
                        }

                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing number at position [" + i + "][" + j + "]: " + e.getMessage());
                        arrayWithoutNG[i-1][j-1] = 0;
                    }
                }
            }
        }


        //Calculate the average grade of each course without NGs
        for(int i = 0; i < sums.length; i++) {
            averages[i] = sums[i] / ((rowCount - 1) - countersAllColumns[i]);
        }

        //Store the average grade and NG counts to infoArray
        for (int i = 0; i < rowCount - 1; i++) {
            for (int j = 1; j < colCount; j++){
                infoArray[i][j][2] = Double.toString(averages[j - 1]); //place averages on the 2nd column of infoArray
                infoArray[i][j][5] = Double.toString(countersAllColumns[j - 1]); //place NG counts on the 5th column of infoArray
            }
        }

    }


    // Method to find the maximum and minimum grades of each course in the given CSV file and stores them in the infoArray.
    public void printMaxMin(){

        //Create two arrays to store the max and min grades of each course
        double[] minArray = new double[colCount - 1]; //30 entries
        double[] maxArray = new double[colCount - 1]; //30 entries


        for (int i = 0; i < arrayWithoutNG[0].length; i++) {

            //Set the first number of each column as the initial value
            double min = arrayWithoutNG[0][i];
            double max = arrayWithoutNG[0][i];

            for (double[] doubles : arrayWithoutNG) {

                if (doubles[i] > max) {
                    max = doubles[i];
                }
                if (min == 0) {
                    min = doubles[i];
                }
                if (doubles[i] < min && doubles[i] != 0) {
                    min = doubles[i];
                }
            }
            //Store max and min grades to minArray and maxArray
            minArray[i] = min;
            maxArray[i] = max;
        }

        //Store the minArray and maxArray to infoArray
        for (int i = 0; i < rowCount - 1; i++) {
            for (int j = 1; j < colCount; j++){
                infoArray[i][j][3] = Double.toString(minArray[j - 1]); //place min on the 3rd column of infoArray
                infoArray[i][j][4] = Double.toString(maxArray[j - 1]); //place max on the 4th column of infoArray
            }
        }

    }

    public String[][] infoStudent(String input, boolean checked) {

        String[][] activeData = checked ? data2 : data1;

        for (int i = 1; i < rowCount; i++) {
            if (activeData[i][0].equals(input)){
                return studentInfo(activeData)[i-1];
            }
        }
        return null;
	}
}
