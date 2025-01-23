package com.StudentAnalysis.Utils;

public class GradeCalculator {
    private final String[][] infoArray;
    private final String[][] graduateInfoArray;
    private int validNumbersCount = 0;

    public GradeCalculator(String[][] infoArray, String[][] graduateInfoArray) {
        this.infoArray = infoArray;
        this.graduateInfoArray = graduateInfoArray;
    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public double calculateAverageGrade(int index) {
        double averages = 0;
        int count = 0;
        if (index == 0) {
            for (String[] strings : infoArray) {
                String stringValue = strings[1];
                if (isNumeric(stringValue) && !stringValue.equals("NG")) {
                    averages += Double.parseDouble(stringValue);
                    count++;
                }
            }
        } else if (index == 1) {
            for (String[] strings : graduateInfoArray) {
                String stringValue = strings[1];
                if (isNumeric(stringValue) && !stringValue.equals("NG")) {
                    averages += Double.parseDouble(stringValue);
                    count++;
                }
            }
        }
        validNumbersCount = count;
        return averages / count;
    }

    public int getValidNumbersCount() {
        return validNumbersCount;
    }
}
