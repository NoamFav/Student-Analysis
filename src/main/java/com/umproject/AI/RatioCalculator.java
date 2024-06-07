package com.umproject.AI;

import com.umproject.Utils.ReaderCsv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RatioCalculator {

    // Retrieve the data from the ReaderCsv class.
    private static final String[][][] data = ReaderCsv.getData();

    // Calculates the ratio of the average of the specific group to the average of the entire dataset.
    private double totalRatio(String surunaValue, String hurniLevel, String lalCount, String volta){
        double averageGroup = totalAverage(surunaValue, hurniLevel, lalCount, volta);
        double averageAll = 7.22;

        return averageGroup / averageAll;
    }

    // Calculates the total average of the specific attribute for all classes.
    private double totalAverage(String surunaValue, String hurniLevel, String lalCount, String volta){
        double[] averages = averageCalculator(surunaValue, hurniLevel, lalCount, volta);
        double totalAverage = 0;
        int validCount = 0;

        for (double average : averages) {
            if (!Double.isNaN(average)) {
                totalAverage += average;
                validCount++;
            }
        }
        if (validCount > 0) {
            totalAverage /= validCount;
        } else {
            totalAverage = -1;
        }

        return totalAverage;
    }

    // Calculates the sum of grades for each class.
    private double[] sumCalculator(String surunaValue, String hurniLevel, String lalCount, String volta) {
        double[][] arrayWithoutNG3 = subset(surunaValue, hurniLevel, lalCount, volta);

        if (arrayWithoutNG3.length == 0) {
            System.out.println("The array is empty. Cannot calculate the sum.");
            return new double[0]; // returns an empty array
        }

        int numColumns = arrayWithoutNG3[0].length;
        double[] sums = new double[numColumns-1];

        for (int j = 1; j < numColumns; j++) {
            double helper = 0;
            for (double[] doubles : arrayWithoutNG3) {
                helper += doubles[j];
            }
            sums[j-1] = helper;
        }
        return sums;
    }

    // Calculates the average grade for each class.
    private double[] averageCalculator(String surunaValue, String hurniLevel, String lalCount, String volta) {
        double[] sums = sumCalculator(surunaValue, hurniLevel, lalCount, volta);
        double[][] students = subset(surunaValue, hurniLevel, lalCount, volta);
        double[] average = new double[sums.length];
        double[] counter = counter(surunaValue, hurniLevel, lalCount, volta);
        for (int i = 0; i < sums.length; i++) {
            average[i] = sums[i] / (students.length - counter[i]);
        }
        return average;
    }

    // Extracts a subset of the data based on certain criteria.
    private double[][] subset(String surunaValue, String hurniLevel, String lalCount, String volta){
        List<String> students = getArray(surunaValue, hurniLevel, lalCount, volta);
        double[][] studentIDSGrades = new double[students.size()][];

        int rowCounter = 0;

        for(int i = 0; i < data[0].length; i++){
            if(students.contains(data[0][i][0])){
                studentIDSGrades[rowCounter] = new double[data[0][i].length];
                for (int j = 0; j < data[0][i].length; j++) {
                    try {
                        studentIDSGrades[rowCounter][j] = Double.parseDouble(data[0][i][j]);
                    } catch (NumberFormatException e) {
                        studentIDSGrades[rowCounter][j] = 0;
                    }
                }
                rowCounter++;
            }
        }
        return Arrays.copyOf(studentIDSGrades, rowCounter);
    }

    // Counts how many students have a grade of NG (Not Graded) for each class.
    private double[] counter(String surunaValue, String hurniLevel, String lalCount, String volta) {
        double[][] arrayWithoutNG = subset(surunaValue, hurniLevel, lalCount, volta);
        int numCols = arrayWithoutNG[0].length;
        double[] countersAllColumns = new double[numCols];

        for (int j = 1; j < numCols; j++) {
            int counterTwo = 0;
            for (double[] doubles : arrayWithoutNG) {
                if (doubles[j] == 0) {
                    counterTwo++;
                }
            }
            countersAllColumns[j-1] = counterTwo;
        }
        return countersAllColumns;
    }

    // Extracts a list of student IDs based on specific criteria from the dataset.
    private List<String> getArray(String surunaValue, String hurniLevel, String lalCount, String volta) {
        String[][] dataNow = data[2];
        List<String> studentsMatch = new ArrayList<>();
        for (String[] strings : dataNow) {
            if (strings[1].equals(surunaValue) && (strings[2].equals(hurniLevel)) && (strings[3].equals(lalCount)) && (strings[4].equals(volta))) {
                studentsMatch.add(strings[0]);
            }
        }
        return studentsMatch;
    }

    public double getRatio(String surunaValue, String hurniLevel, String lalCount, String volta){
        return totalRatio(surunaValue, hurniLevel, lalCount, volta);
    }
}
