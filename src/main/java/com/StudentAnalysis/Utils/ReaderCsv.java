package com.StudentAnalysis.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ReaderCsv {

    private static final String[][][] data;

    static {
        data = new String[7][][];
        data[0] = loadDataFromFile("/com/StudentAnalysis/csv/CurrentGrades.csv");
        data[1] = loadDataFromFile("/com/StudentAnalysis/csv/bugFreeGraduateGrades.csv");
        data[2] = loadDataFromFile("/com/StudentAnalysis/csv/StudentInfo.csv");
        data[3] = loadDataFromFile("/com/StudentAnalysis/csv/MergedDataSet.csv");
        data[4] = loadDataFromFile("/com/StudentAnalysis/csv/RatioPrediction.csv");
        data[5] = loadDataFromFile("/com/StudentAnalysis/csv/CapedRatioPredictionRaw.csv");
        data[6] = loadDataFromFile("/com/StudentAnalysis/csv/RatioPredictionRaw.csv");
    }

    // getter for the data array
    public static String[][][] getData() {
        return data;
    }

    private static String[][] loadDataFromFile(String resourcePath) {
        List<String[]> list = new ArrayList<>();

        try (InputStream is = ReaderCsv.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                list.add(values);
            }
        } catch (IOException e) {
            System.err.println("Error reading resource: " + resourcePath);
            return new String[0][]; // Return an empty array to avoid NPE in consumer code
        }

        System.out.println("Loaded " + list.size() + " records from " + resourcePath);
        return list.toArray(new String[0][]);
    }
}
