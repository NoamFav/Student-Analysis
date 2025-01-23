package com.StudentAnalysis.Cards;

import com.StudentAnalysis.Utils.ReaderCsv;

public class graduateStudentCard {
    private final String[][] data = ReaderCsv.getData()[1];
    private final int rowCount = data.length; // Number of rows of graduateGrades
    private final int colCount = data[0].length; // Number of columns of graduateGrades
    private final double[] sums = new double[colCount - 1]; // 30 entries
    private final double[] averages = new double[colCount - 1]; // 30 entries

    // Initialize a 3D array to store graduate student cards with student id and grades
    public String[][] infoArray = new String[colCount][3];

    public String[][] studentInfo(String id) {

        double[] averageArray = processGrades();

        // Use a for loop to find the id in the graduate student database
        for (int i = 1; i < rowCount; i++) {

            // if found, insert the all information of the student to the infoArray
            if (data[i][0].equals(id)) {

                infoArray[0][0] = "Student " + data[i][0]; // store student id
                infoArray[0][1] = "Grade";
                infoArray[0][2] = "Course Average Grade";

                for (int j = 1; j < colCount; j++) {
                    infoArray[j][0] =
                            data[0][j]; // place course names on the 0th column of infoArray
                    infoArray[j][1] =
                            data[i][j]; // place actual grades on the 1st column of infoArray
                    infoArray[j][2] =
                            Double.toString(
                                    averageArray[j - 1]); // place course average grades on the 2nd
                    // column of infoArray
                }

                return infoArray;
            }
        }

        return null;
    }

    // Method to process and compute averages for the grades in the given file.
    // The method computes sums and averages for each course in the given CSV file and returns the
    // average array.
    public double[] processGrades() {

        for (int i = 1; i < rowCount; i++) {

            for (int j = 1; j < colCount; j++) {

                String value = data[i][j];

                try {
                    // Sum up all graduate students grades of each course
                    if (j - 1 < sums.length) {
                        sums[j - 1] += Double.parseDouble(value);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace(new java.io.PrintWriter(System.err));
                }
            }
        }
        // Calculate the average grade of each course
        for (int i = 0; i < sums.length; i++) {
            averages[i] = sums[i] / (rowCount - 1);
        }

        return averages;
    }

    public String[][] getStudentInfo(String courseName) {
        return studentInfo(courseName);
    }
}
