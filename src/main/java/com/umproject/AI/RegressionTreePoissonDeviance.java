package com.umproject.AI;

import com.umproject.Launcher;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RegressionTreePoissonDeviance {
    private TreeNode root; // Root of the regression tree
    private static final List<String> courses = Arrays.asList("JTE-234","ATE-003","TGL-013","PPL-239","WDM-974","GHL-823","HLU-200","MON-014","FEA-907","LPG-307","TSO-010","LDE-009","JJP-001","MTE-004","LUU-003","LOE-103","PLO-132","BKO-800","SLE-332","BKO-801","DSE-003","DSE-005","ATE-014","JTW-004","ATE-008","DSE-007","ATE-214","JHF-101","KMO-007","WOT-104");
    private int MIN_SAMPLES;
    private static final Map<Integer, Map<String, Object>> studentInfo = new HashMap<>();
    private static final Map<Integer, Map<String, Double>> currentGrades = new HashMap<>();
    private static final Map<String, Object> unknownStudentInfo = new HashMap<>();
    private static final List<Double> averageAccuracy = new ArrayList<>();
    private static final List<Double> averageMSE = new ArrayList<>();
    private static final Map<String, Double> accuraciesForCourses = new HashMap<>();
    private static final Map<String, Double> MSEForCourses = new HashMap<>();
    private static final Map<String, Double> randomForestAccuracies = new HashMap<>();
    private static final Map<String, Double> randomForestMSE = new HashMap<>();
    private static final Map<String ,List<TreeNode>> bigForest = new HashMap<>();
    private static final Map<String, TreeNode> bestTree = new HashMap<>();

    private static class TreeNode {
        String splitFeature;
        double splitThreshold;
        String splitCategory;
        int sample;
        double average;
        double poissonDeviance;
        TreeNode leftChild;
        TreeNode rightChild;
        double prediction;
        boolean isLeaf;
        private boolean isNumerical;

        TreeNode() {
            this.isLeaf = false;
            this.splitThreshold = Double.NaN; // Initialize with NaN to indicate no numerical threshold
            this.splitCategory = null;       // Initialize as null for non-categorical splits
        }
        public String toJson() {
            return toJson(1);
        }

        //convert the tree to a json file with indentation
        private String toJson(int indentLevel) {
            StringBuilder jsonBuilder = new StringBuilder();
            String indent = String.join("", Collections.nCopies(indentLevel, "    ")); // 4 spaces per indent level
            String innerIndent = String.join("", Collections.nCopies(indentLevel + 1, "    "));
            String childIndent = String.join("", Collections.nCopies(indentLevel + 2, "    ")); // additional indent for child nodes

            jsonBuilder.append("{\n");

            if (isLeaf) {
                jsonBuilder.append(innerIndent).append("\"Leaf Node\": {\n");
                jsonBuilder.append(childIndent).append("\"Prediction\": ").append(prediction).append(",\n");
                jsonBuilder.append(childIndent).append("\"Sample\": ").append(sample).append(",\n");
                jsonBuilder.append(childIndent).append("\"Poisson Deviance\": ").append(poissonDeviance).append(",\n");
                jsonBuilder.append(childIndent).append("\"Average\": ").append(average).append("\n");
                jsonBuilder.append(innerIndent).append("}");
            } else {
                jsonBuilder.append(innerIndent).append("\"Split Node\": {\n");
                jsonBuilder.append(childIndent).append("\"Feature\": \"").append(splitFeature).append("\",\n");
                jsonBuilder.append(childIndent).append("\"Threshold\": ").append(isNumerical ? splitThreshold : "\"" + splitCategory + "\"").append(",\n");
                jsonBuilder.append(childIndent).append("\"Sample\": ").append(sample).append(",\n");
                jsonBuilder.append(childIndent).append("\"Poisson Deviance\": ").append(poissonDeviance).append(",\n");
                jsonBuilder.append(childIndent).append("\"Average\": ").append(average).append(",\n");

                if (leftChild != null) {
                    jsonBuilder.append(childIndent).append("\"Left Child\": ").append(leftChild.toJson(indentLevel + 2)).append(",\n");
                }
                if (rightChild != null) {
                    jsonBuilder.append(childIndent).append("\"Right Child\": ").append(rightChild.toJson(indentLevel + 2)).append("\n");
                }

                jsonBuilder.append(innerIndent).append("}");
            }

            jsonBuilder.append("\n").append(indent).append("}");

            return jsonBuilder.toString();
        }
    }

    public RegressionTreePoissonDeviance() throws IOException{
        //load data as a Map of maps
        loadStudentInfo();
        loadCurrentGrades();
    }
    private void loadStudentInfo() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/com/example/umproject/StudentInfo.csv"))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int studentNumber = Integer.parseInt(parts[0]);
                Map<String, Object> info = new HashMap<>();

                info.put("Suruna Value", parts[1]);

                //convert "Hurni Level" to numeric and store
                double hurniLevelNumeric = hurniLevelToNumeric(parts[2]);
                info.put("Hurni Level", hurniLevelNumeric);

                //parse continuous values as Double
                try {
                    double lalCount = Double.parseDouble(parts[3]);
                    info.put("Lal Count", lalCount);
                } catch (NumberFormatException ignored) {}

                info.put("Volta", parts[4]); //treated as a discrete variable

                try {
                    double ratio = Double.parseDouble(parts[5]);
                    info.put("Ratios", ratio);
                } catch (NumberFormatException ignored) {}

                studentInfo.put(studentNumber, info);
            }
        }
    }
    private void loadCurrentGrades() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/com/example/umproject/CurrentGrades.csv"))) {
            String[] headers = br.readLine().split(",");
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int studentID = Integer.parseInt(parts[0]);
                Map<String, Double> grades = new HashMap<>();
                for (int i = 1; i < headers.length; i++) {
                    if (!parts[i].equals("NG") && !parts[i].isEmpty()) {
                        grades.put(headers[i], Double.parseDouble(parts[i]));
                    }
                }
                currentGrades.put(studentID, grades);
            }
        }
    }
    private double hurniLevelToNumeric(String level) {
        //convert "Hurni Level" to numeric and store
        return switch (level) {
            case "low" -> 1;
            case "medium" -> 2;
            case "high" -> 3;
            case "full" -> 4;
            default -> 0;
        };
    }
    private double Average(List<Double> list) {
        //calculate the average of a list of doubles
        double sum = 0;
        for (Double i: list) {
            sum += i;
        }
        return sum / list.size();
    }
    public void buildTree(String course, List<Integer> studentIds, Map<Integer, Map<String, Object>> studentInfo, Map<Integer, Map<String, Double>> currentGrades) {
        // Add previous course grades with lower or equal NG as features
        addPreviousCourseGrades(course);

        // Build the tree
        double MIN_SAMPLE_PERCENTAGE = .1;
        MIN_SAMPLES = (int)(studentIds.size() * MIN_SAMPLE_PERCENTAGE);
        this.root = build(course, studentIds, studentInfo, currentGrades,  0);
    }
    private TreeNode build(String course, List<Integer> studentIds, Map<Integer, Map<String, Object>> studentInfo, Map<Integer, Map<String, Double>> currentGrades, int currentDepth) {

        //create a leaf node if the current depth is greater than the max depth or if the number of samples is less than the minimum samples
        int MAX_DEPTH = 10;
        if (currentDepth > MAX_DEPTH || studentIds.size() <= MIN_SAMPLES) {
            return createLeafNode(studentIds, course, currentGrades, calculatePoissonDeviance(getGrades(studentIds, course, currentGrades)), Average(getGrades(studentIds, course, currentGrades)), studentIds.size());
        }

        //calculate the best split for the current node
        String bestSplit = findBestPropertyAndThresholdForCourse(course, studentInfo, studentIds);
        String[] splitParts = bestSplit.split("=");
        String bestAttribute = splitParts[0];
        Object splitValue = splitParts[1];

        //verify that the best attributes isn't pure
        Set<Object> uniqueValues = new HashSet<>();
        for (Integer id : studentIds) {
            Object value = studentInfo.get(id).get(bestAttribute);
            uniqueValues.add(value);
        }
        if (uniqueValues.size() == 1) {
            //all values for this attribute are the same, create a leaf node
            return createLeafNode(studentIds, course, currentGrades, calculatePoissonDeviance(getGrades(studentIds, course, currentGrades)), Average(getGrades(studentIds, course, currentGrades)), studentIds.size());
        }

        //initialize the left and right split ids
        List<Integer> leftSplitIds = new ArrayList<>();
        List<Integer> rightSplitIds = new ArrayList<>();
        boolean isNumerical = studentInfo.values().stream().anyMatch(map -> map.get(bestAttribute) instanceof Double);

        //split the data based on the best attribute, and whether it is numerical or not
        double numericThreshold = isNumerical ? Double.parseDouble(splitValue.toString()) : 0;
        for (Integer id : studentIds) {
            Object value = studentInfo.get(id).get(bestAttribute);
            if (value == null) continue;

            if (isNumerical) {
                if ((Double) value <= numericThreshold) {
                    leftSplitIds.add(id);
                } else {
                    rightSplitIds.add(id);
                }
            } else {
                if (value.equals(splitValue)) {
                    leftSplitIds.add(id);
                } else {
                    rightSplitIds.add(id);
                }
            }
        }

        //create a node for the current split and save the information
        TreeNode node = new TreeNode();
        node.splitFeature = bestAttribute;
        node.sample = studentIds.size();
        node.average = Average(getGrades(studentIds, course, currentGrades));
        node.poissonDeviance = calculatePoissonDeviance(getGrades(studentIds, course, currentGrades));
        if (isNumerical) {
            node.splitThreshold = numericThreshold;
            node.isNumerical = true;
        } else {
            node.splitCategory = splitValue.toString();
            node.isNumerical = false;
        }

        //calculate the poisson deviance for the left and right splits
        double poissonDevianceLeft = calculatePoissonDeviance(getGrades(leftSplitIds, course, currentGrades));
        double poissonDevianceRight = calculatePoissonDeviance(getGrades(rightSplitIds, course, currentGrades));

        //create leaf nodes if the poisson deviance is less than the minimum poisson deviance
        double MIN_POISSON_DEVIANCE = .1;
        if (poissonDevianceLeft <= MIN_POISSON_DEVIANCE) {
            node.leftChild = createLeafNode(leftSplitIds, course, currentGrades, poissonDevianceLeft, Average(getGrades(leftSplitIds, course, currentGrades)), leftSplitIds.size());
        } else {
            //otherwise recursively build the tree
            node.leftChild = build(course, leftSplitIds, studentInfo, currentGrades,  currentDepth + 1);
        }
        if (poissonDevianceRight <= MIN_POISSON_DEVIANCE) {
            node.rightChild = createLeafNode(rightSplitIds, course, currentGrades, poissonDevianceRight, Average(getGrades(rightSplitIds, course, currentGrades)), rightSplitIds.size());
        } else {
            //otherwise recursively build the tree
            node.rightChild = build(course, rightSplitIds, studentInfo, currentGrades, currentDepth + 1);
        }

        return node;
    }
    private TreeNode createLeafNode(List<Integer> studentIds, String course, Map<Integer, Map<String, Double>> currentGrades, double poissonDeviance, double average, double sample) {
        //create a leaf node and save the information
        TreeNode leaf = new TreeNode();
        leaf.isLeaf = true;
        leaf.prediction = studentIds.stream()
                .mapToDouble(id -> currentGrades.get(id).get(course))
                .average()
                .orElse(0);
        leaf.sample = (int)sample;
        leaf.average = average;
        leaf.poissonDeviance = poissonDeviance;
        return leaf;
    }
    private String findBestPropertyAndThresholdForCourse(String course, Map<Integer, Map<String, Object>> studentInfo, List<Integer> subsetStudentIds) {
        double minPoissonDeviance = Double.POSITIVE_INFINITY;
        String bestPropertyAndThreshold = "";

        //define properties to check
        //each time a property is checked, the poisson deviance is calculated and if it is less than the minimum poisson deviance, it is saved
        //return the property with the lowest poisson deviance
        List<String> continuousProperties = Arrays.asList("Lal Count", "Ratios", "Hurni Level");
        List<String> discreteProperties = List.of("Suruna Value");

        //check for continuous properties
        for (String property : continuousProperties) {
            List<Double> thresholds = getPossibleThresholds(property, studentInfo, subsetStudentIds);
            for (Double threshold : thresholds) {
                Map<String, Double> result = checkScoreDifferenceByThreshold(course, property, threshold, studentInfo, subsetStudentIds);
                Double poissonDeviance = result.get("PoissonDeviance");
                if (poissonDeviance != null && poissonDeviance < minPoissonDeviance) {
                    minPoissonDeviance = poissonDeviance;
                    bestPropertyAndThreshold = property + "=" + threshold;
                }
            }
        }

        //check for discrete properties
        for (String property : discreteProperties) {
            Set<String> uniqueValues = new HashSet<>();
            for (Integer id : subsetStudentIds) {
                Object value = studentInfo.get(id).get(property);
                if (value != null) {
                    uniqueValues.add(value.toString());
                }
            }

            for (String value : uniqueValues) {
                Map<String, Double> result = checkScoreDifferenceByProperty(course, property, value, studentInfo, subsetStudentIds);
                Double poissonDeviance = result.get("PoissonDeviance");
                if (poissonDeviance != null && poissonDeviance < minPoissonDeviance) {
                    minPoissonDeviance = poissonDeviance;
                    bestPropertyAndThreshold = property + "=" + value;
                }
            }
        }

        //check for previous course grades
        for (int i = 0; i < courses.indexOf(course); i++) {
            String prevCourse = courses.get(i);
            String gradeFeature = "grade_" + prevCourse;
            List<Double> thresholds = getPossibleThresholds(gradeFeature, studentInfo, subsetStudentIds);
            for (Double threshold : thresholds) {
                Map<String, Double> result = checkScoreDifferenceByThreshold(course, gradeFeature, threshold, studentInfo, subsetStudentIds);
                Double poissonDeviance = result.get("PoissonDeviance");
                if (poissonDeviance != null && poissonDeviance < minPoissonDeviance) {
                    minPoissonDeviance = poissonDeviance;
                    bestPropertyAndThreshold = gradeFeature + "=" + threshold;
                }
            }
        }

        return bestPropertyAndThreshold;
    }
    public Map<String, Double> checkScoreDifferenceByProperty(String course, String property, String boundaryValue, Map<Integer, Map<String, Object>> studentInfo, List<Integer> subsetStudentIds) {
        // Calculate poisson deviance for each group based on the property value
        List<Double> group1 = new ArrayList<>();
        List<Double> group2 = new ArrayList<>();

        //split the data based on the property value into two groups
        for (Integer studentID : subsetStudentIds) {
            if (studentInfo.containsKey(studentID)) {
                Object propValue = studentInfo.get(studentID).get(property);
                Double grade = currentGrades.get(studentID).get(course);
                if (grade != null) {
                    if (propValue != null && propValue.toString().equals(boundaryValue)) {
                        group1.add(grade);
                    } else {
                        group2.add(grade);
                    }
                }
            }
        }

        return calculateTotalPoissonDeviance(group1, group2);
    }
    private Map<String, Double> checkScoreDifferenceByThreshold(String course, String property, double threshold, Map<Integer, Map<String, Object>> studentInfo, List<Integer> subsetStudentIds) {
        //calculate poisson deviance for each group based on the threshold
        List<Double> group1 = new ArrayList<>();
        List<Double> group2 = new ArrayList<>();

        //split the data based on the threshold into two groups
        for (Integer studentID : subsetStudentIds) {
            if (studentInfo.containsKey(studentID) && currentGrades.containsKey(studentID)) {
                Object propValueObj = studentInfo.get(studentID).get(property);
                Double grade = currentGrades.get(studentID).get(course);
                if (grade != null && propValueObj instanceof Double) {
                    double propValueNumeric = (Double) propValueObj;
                    if (propValueNumeric <= threshold) {
                        group1.add(grade);
                    } else {
                        group2.add(grade);
                    }
                }
            }
        }

        return calculateTotalPoissonDeviance(group1, group2);
    }
    private List<Double> getPossibleThresholds(String property, Map<Integer, Map<String, Object>> studentInfo, List<Integer> subsetStudentIds) {
        //get all possible thresholds for a continuous property
        //collect unique values
        TreeSet<Double> uniqueValues = new TreeSet<>();
        for (Integer id : subsetStudentIds) {
            Object value = studentInfo.get(id).get(property);
            if (value instanceof Double) {
                uniqueValues.add((Double) value);
            }
        }

        //generate midpoints
        List<Double> thresholds = new ArrayList<>();
        Double prevValue = null;
        for (Double currentValue : uniqueValues) {
            if (prevValue != null) {
                double midpoint = (prevValue + currentValue) / 2.0;
                thresholds.add(midpoint);
            }
            prevValue = currentValue;
        }
        return thresholds;
    }
    private void addPreviousCourseGrades(String currentCourse) {
        //add previous course grades with lower or equal NG as features
        Map<String, Integer> ngCounts = calculateNGCounts();
        int currentCourseNGCount = ngCounts.getOrDefault(currentCourse, 0);

        for (Integer studentID : currentGrades.keySet()) {
            Map<String, Double> grades = currentGrades.get(studentID);
            for (String course : courses) {
                //check NG count for each course, compare it with the current course's NG count and skip the current course
                if (!course.equals(currentCourse) && ngCounts.getOrDefault(course, 0) <= currentCourseNGCount) {
                    Double grade = grades.get(course);

                    studentInfo.get(studentID).put("grade_" + course, Objects.requireNonNullElse(grade, Double.NaN));
                }
            }
        }
    }
    private List<Double> getGrades(List<Integer> studentIds, String course, Map<Integer, Map<String, Double>> currentGrades) {
        //get grades for a list of students on a specific course
        List<Double> grades = new ArrayList<>();
        for (Integer id : studentIds) {
            Map<String, Double> studentGradeMap = currentGrades.get(id);
            if (studentGradeMap != null) {
                Double grade = studentGradeMap.get(course);
                if (grade != null) {
                    grades.add(grade);
                }
            }
        }
        return grades;
    }
    private Map<String, Integer> calculateNGCounts() {
        //initialize the map to store NG counts for each course
        Map<String, Integer> ngCounts = new HashMap<>();

        //iterate through each course and count NGs
        for (String course : courses) {
            int ngCount = 0;
            for (Map.Entry<Integer, Map<String, Double>> entry : currentGrades.entrySet()) {
                Double grade = entry.getValue().get(course);
                if (grade == null || grade.equals(Double.NaN)) {
                    ngCount++;
                }
            }
            ngCounts.put(course, ngCount);
        }

        return ngCounts;
    }
    public Map<String, Double> calculateTotalPoissonDeviance(List<Double> group1, List<Double> group2) {
        //calculate the poisson deviance for each group and return the total poisson deviance
        double poissonDevianceGroup1 = calculatePoissonDeviance(group1);
        double poissonDevianceGroup2 = calculatePoissonDeviance(group2);

        double totalPoissonDeviance = (group1.size() * poissonDevianceGroup1 + group2.size() * poissonDevianceGroup2) / (group1.size() + group2.size());

        Map<String, Double> result = new HashMap<>();
        result.put("PoissonDeviance", totalPoissonDeviance);

        return result;
    }
    private double calculatePoissonDeviance(List<Double> group) {
        //calculate the poisson deviance for a group as a whole
        if (group.isEmpty()) {
            return 0.0;
        }

        double mean = group.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        return group.stream().mapToDouble(score -> {
            if (score > 0) {
                return 2 * (score * Math.log(score / mean) - (score - mean));
            } else {
                return 2 * mean;
            }
        }).sum();
    }
    public Pair<List<Integer>, List<Integer>> randomForestSplit(List<Integer> studentIds) {
        //split the data into a bootstrap sample and an OOB sample
        //the bootstrap sample is used to build the tree, the bootstrap sample is a random sample with replacement
        //the OOB sample is used to evaluate the tree, the OOB sample is the data that was not selected for the bootstrap sample
        Random rand = new Random();
        List<Integer> bootstrapSample = new ArrayList<>();
        Set<Integer> selectedIndices = new HashSet<>();

        for (int i = 0; i < studentIds.size(); i++) {
            int randomIndex = rand.nextInt(studentIds.size());
            bootstrapSample.add(studentIds.get(randomIndex));
            selectedIndices.add(randomIndex);
        }

        List<Integer> oobSample = IntStream.range(0, studentIds.size())
                .filter(index -> !selectedIndices.contains(index))
                .mapToObj(studentIds::get)
                .collect(Collectors.toList());

        return new Pair<>(bootstrapSample, oobSample);
    }
    public void runRegressionTree(String course, List<Integer> studentIds, Map<Integer, Map<String, Object>> studentInfo, Map<Integer, Map<String, Double>> currentGrades, int treesNumber) throws IOException {
        //run the regression tree for a specific course
        //the regression tree is run multiple times and the best tree is saved
        //the best tree is the tree with the lowest MSE
        //the MSE is calculated by evaluating the tree on a test set
        double bestMSE = Double.POSITIVE_INFINITY;
        double bestAccuracy = 0;
        for (int i = 0; i < treesNumber; i++) {

            //filter the student ids to only include students with a grade for the course
            List<Integer> filteredStudentIds = new ArrayList<>(studentIds.stream()
                    .filter(id -> currentGrades.containsKey(id) && currentGrades.get(id).containsKey(course))
                    .toList());

            //shuffle the student ids and select 80% of the ids to build the tree
            Collections.shuffle(filteredStudentIds, new Random());
            int numToSelect = (int) (.80 * filteredStudentIds.size());
            List<Integer> selectedIds = filteredStudentIds.subList(0, numToSelect);

            //build the tree
            buildTree(course, selectedIds, studentInfo, currentGrades);

            //evaluate the tree on the remaining 20% of the ids
            List<Integer> testIds = filteredStudentIds.subList(numToSelect, filteredStudentIds.size());
            evaluateModelPerformance(root, testIds, course, studentInfo, currentGrades);

            //save the tree if it has the lowest MSE
            if (averageMSE.getLast() < bestMSE) {

                bestMSE = averageMSE.getLast();
                bestAccuracy = averageAccuracy.getLast();

                bestTree.put(course, root);
                String json = root.toJson();
                writeTreeToFile(json, course);
            }
        }

        accuraciesForCourses.put(course, bestAccuracy == 0 ? Double.NaN : bestAccuracy);
        MSEForCourses.put(course, Double.isFinite(bestMSE) ? bestMSE : Double.NaN);
    }
    public void runRandomForest(String course, List<Integer> allStudentIds, Map<Integer, Map<String, Object>> studentInfo, Map<Integer, Map<String, Double>> currentGrades, int numberOfTrees) throws IOException {
        //run the random forest for a specific course
        averageAccuracy.clear();
        averageMSE.clear();

        //initialize the lists to store the results for each tree
        List<Double> forestPoissonDeviances = new ArrayList<>();
        List<Double> forestAccuracies = new ArrayList<>();
        List<TreeNode> smallForest = new ArrayList<>();

        //filter the student ids to only include students with a grade for the course
        List<Integer> filteredStudentIds = allStudentIds.stream()
                .filter(id -> currentGrades.containsKey(id) && currentGrades.get(id).containsKey(course))
                .toList();

        //run the random forest for the specified number of trees

        for (int i = 0; i < numberOfTrees; i++) {
            Pair<List<Integer>, List<Integer>> splitResult = randomForestSplit(new ArrayList<>(filteredStudentIds));
            List<Integer> subsetStudentIds = splitResult.getKey(); //bootstrap sample
            List<Integer> oobSampleIds = splitResult.getValue(); //OOB sample

            buildTree(course, subsetStudentIds, studentInfo, currentGrades);
            writeForestToFile(root.toJson(), course, i);

            smallForest.add(getTree());

            //evaluate the tree on the OOB sample
            evaluateModelPerformance(root, oobSampleIds, course, studentInfo, currentGrades);

            //aggregate the results
            forestPoissonDeviances.add(averageMSE.getLast());
            forestAccuracies.add(averageAccuracy.getLast());
        }

        //calculate average performance across all trees
        double avgPoissonDeviance = forestPoissonDeviances.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        double avgAccuracy = forestAccuracies.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);

        //store the averaged results
        randomForestAccuracies.put(course, avgAccuracy);
        randomForestMSE.put(course, avgPoissonDeviance);

        //store the forest
        bigForest.put(course,smallForest);
    }
    private void evaluateModelPerformance(TreeNode root, List<Integer> testIds, String course, Map<Integer, Map<String, Object>> studentInfo, Map<Integer, Map<String, Double>> currentGrades) {
        //evaluate the model performance on a test set
        //calculate the MSE and accuracy for the test set
        double totalSquaredError = 0;
        int count = 0;
        int accuratePredictions = 0;

        for (Integer id : testIds) {
            double predictedGrade = predictGrade(root, id, studentInfo, true);
            double actualGrade = currentGrades.get(id).get(course);
            totalSquaredError += Math.pow(predictedGrade - actualGrade, 2);
            count++;

            double tolerance = .8;
            if (Math.abs(predictedGrade - actualGrade) <= tolerance) {
                accuratePredictions++;
            }
        }

        double poissonDeviance = totalSquaredError / count;
        double accuracyPercentage = (double) accuratePredictions / testIds.size() * 100;

        averageMSE.add(poissonDeviance);
        averageAccuracy.add(accuracyPercentage);
    }
    private double predictGrade(TreeNode root, int studentId, Map<Integer, Map<String, Object>> studentInfo, boolean isKnown) {
        //predict the grade for a student
        TreeNode node = root;
        while (!node.isLeaf) {
            Object value;
            //check if the student is known or unknown
            if (isKnown) {
                //if the student is known, get the value for the split feature from student info
                value = studentInfo.get(studentId).get(node.splitFeature);
            } else {
                //if the student is unknown, get the value for the split feature from unknown student info
                value = unknownStudentInfo.get(node.splitFeature);
            }
            //check if the split feature is numerical or categorical
            //recursively traverse the tree until a leaf node is reached
            if (node.isNumerical) {
                double numericValue = (Double) value;
                node = (numericValue <= node.splitThreshold) ? node.leftChild : node.rightChild;
            } else {
                String stringValue = (String) value;
                node = stringValue.equals(node.splitCategory) ? node.leftChild : node.rightChild;
            }
        }
        return node.prediction;
    }
    public double predictForestGrade(int studentId, Map<Integer, Map<String, Object>> studentInfo, String course, boolean isKnown) {
        //predict the grade for a student using the random forest
        List<Double> predictions = new ArrayList<>();
        for (TreeNode tree : bigForest.get(course)) {
            predictions.add(predictGrade(tree, studentId, studentInfo, isKnown));
        }
        return Average(predictions);
    }
    public double predictRegressionTreeGrade(int studentId, Map<Integer, Map<String, Object>> studentInfo, String course, boolean isKnown) {
        //predict the grade for a student using the regression tree
        TreeNode bestRoot = bestTree.get(course);
        return predictGrade(bestRoot, studentId, studentInfo, isKnown);
    }

    private TreeNode getTree() {
        return root; //return the regression tree
    }
    private static void writeTreeToFile(String content, String course) throws IOException {
        //write the tree to a json file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Json/PoissonDeviance/RegressionTree/tree:"  + course + ".json"))) {
            writer.write(content);
        }
    }
    private static void writeForestToFile(String content, String course, int number) throws IOException {
        //write the forest to a json file for each course
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Json/PoissonDeviance/forest/" + course + "/tree number:" + number + ".json"))) {
            writer.write(content);
        }
    }

    private void drawTree(TreeNode node, int x, int y, int depth, int maxWidth, int maxDepth, Pane pane, Pane root) {
        if (node == null) {
            return;
        }

        // Calculate dynamic spacing
        double depthScalingFactor = Launcher.screenHeight / maxDepth; // Adjust vertical spacing based on maximum depth

        // Rectangle dimensions
        double rectWidth = Launcher.Distance(1920, 80, root.getWidth());
        double rectHeight = Launcher.Distance(1080, 90, root.getHeight());

        double rectX = x - rectWidth / 2;
        double rectY = y - rectHeight / 2;

        Rectangle rect = new Rectangle(rectX, rectY, rectWidth, rectHeight);
        if (node.isLeaf) {
            rect.setFill(Color.LIGHTGREEN);
        } else {
            rect.setFill(Color.LIGHTBLUE);
        }
        rect.setStroke(Color.BLACK);
        rect.toFront();
        pane.getChildren().add(rect);

        String nodeInfo = getNodeInfo(node); // Method to get node information for display
        Text text = new Text(rectX + Launcher.Distance(1920, 5, root.getWidth()), rectY + Launcher.Distance(1080,15, root.getHeight()), nodeInfo);
        text.setFont(new Font("Georgia", Launcher.Distance(1920, 15, root.getWidth())));
        pane.getChildren().add(text);

        // Adjust positions for children nodes
        double widthOffset = (double) maxWidth / Math.pow(2, depth + 1); // Corrected depth for offset calculation
        double childY = y + depthScalingFactor;


        // Draw right child and connecting line
        if (node.rightChild != null) {
            double childXRight = x + widthOffset;
            Line rightLine = new Line(x, y + rectHeight / 2, childXRight, childY - rectHeight / 2);
            pane.getChildren().add(rightLine);
            drawTree(node.rightChild, (int) childXRight, (int) childY, depth + 1, maxWidth, maxDepth, pane, root);
        }

        // Draw left child and connecting line
        if (node.leftChild != null) {
            double childXLeft = x - widthOffset;
            Line leftLine = new Line(x, y + rectHeight / 2, childXLeft, childY - rectHeight / 2);
            pane.getChildren().add(leftLine);
            drawTree(node.leftChild, (int) childXLeft, (int) childY, depth + 1, maxWidth, maxDepth, pane, root);
        }


    }
    protected void buttonPrediction(boolean isSelected, String courseInput, int idInput, Label result2) throws IOException {
        List<Integer> studentIds = new ArrayList<>(RegressionTreePoissonDeviance.getCurrentGrades().keySet());
        if (isSelected) {
            runRandomForest(courseInput, studentIds, studentInfo, currentGrades,5);
        } else {
            runRegressionTree(courseInput, studentIds, studentInfo, currentGrades,10);
        }

        double prediction;
        if (isSelected) {
            prediction = predictForestGrade(idInput, RegressionTreePoissonDeviance.getStudentInfo(), courseInput, true);
        } else {
            prediction = predictRegressionTreeGrade(idInput, RegressionTreePoissonDeviance.getStudentInfo(), courseInput, true);
        }
        result2.setText("Predicted Grade: " + String.format("%.4f", prediction) + ", ±.8 range");
    }
    private String getNodeInfo(TreeNode node) {
        // Get node information for display
        StringBuilder sb = new StringBuilder();
        if (node.isLeaf) {
            sb.append("Pred: ").append(String.format("%.2f", node.prediction)).append("\n");
        } else {
            sb.append("Feat: ").append(node.splitFeature).append("\n");
            sb.append(node.isNumerical ? ("Thr: " + String.format("%.4f", node.splitThreshold)) : ("Cat: " + node.splitCategory)).append("\n");
        }
        sb.append("Sample: ").append(node.sample).append("\n");
        sb.append("Avg: ").append(String.format("%.2f", node.average)).append("\n");
        sb.append("Dev: ").append(String.format("%.2f", node.poissonDeviance));
        return sb.toString();
    }
    private int calculateMaxDepth(TreeNode node) {
        // Calculate the maximum depth of the tree
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(calculateMaxDepth(node.leftChild), calculateMaxDepth(node.rightChild));
    }
    public void showTreeInPopup(Stage stage, String courseName, RegressionTreePoissonDeviance tree, Pane root) throws IOException {
        //create the popup
        Popup popup = new Popup();
        Pane pane = new Pane();

        List<Integer> studentIds = new ArrayList<>(currentGrades.keySet());
        tree.runRegressionTree(courseName, studentIds, studentInfo, currentGrades, 5);
        String poisson = String.valueOf(MSEForCourses.get(courseName));
        String accuracies = String.valueOf(accuraciesForCourses.get(courseName));

        //create the title
        Label title = new Label("Regression Tree for " + courseName + "\n" + "MSE: " + poisson + "\n" + "Accuracy: " + accuracies);
        title.setFont(new Font("Georgia", Launcher.Distance(1920, 25, root.getWidth())));
        title.setLayoutX(Launcher.Distance(1920, 10, root.getWidth()));
        title.setLayoutY(Launcher.Distance(1080, 10, root.getHeight()));
        pane.getChildren().add(title);

        //draw the tree on the existing pane
        tree.drawTree(bestTree.get(courseName), (int) (Launcher.screenWidth / 2), 50, 0, (int) Launcher.screenWidth / 2, tree.calculateMaxDepth(bestTree.get(courseName)), pane, root);
        pane.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");

        //add the graph pane to the popup
        popup.getContent().add(pane);
        popup.setAutoHide(true);

        //set the size of the popup to match the pane
        double paneWidth = pane.getPrefWidth();
        double paneHeight = pane.getPrefHeight();
        popup.setWidth(paneWidth);
        popup.setHeight(paneHeight);

        //get the screen size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double popupX = (screenBounds.getWidth() - paneWidth) / 2;
        double popupY = (screenBounds.getHeight() - paneHeight) / 2;

        //set the position of the popup
        popup.setX(popupX);
        popup.setY(popupY);
        //show the popup
        popup.show(stage);
    }

    public static void main(String[] args) throws IOException{
        averageAccuracy.clear();
        averageMSE.clear();

        double start = System.currentTimeMillis();

        //initialize the lists to store the results for each course
        RegressionTreePoissonDeviance tree = new RegressionTreePoissonDeviance();

        //create the regression tree for each course
        for (String course : courses) {
            List<Integer> studentIds = new ArrayList<>(currentGrades.keySet());
            tree.runRegressionTree(course, studentIds, studentInfo, currentGrades, 10);
        }

        //create the random forest for each course
        for (String course : courses) {
            List<Integer> studentIds = new ArrayList<>(currentGrades.keySet());
            tree.runRandomForest(course, studentIds, studentInfo, currentGrades, 10);
        }

        //get some analytics on the results
        System.out.println("\nlist of accuracies" + accuraciesForCourses);
        System.out.println("list of MSE" + MSEForCourses);

        double accuracy = accuraciesForCourses.values().stream().filter(Double::isFinite).mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        double MSE = MSEForCourses.values().stream().filter(Double::isFinite).mapToDouble(Double::doubleValue).average().orElse(Double.NaN);

        System.out.println("average accuracy: " + accuracy);
        System.out.println("average MSE: " + MSE);

        System.out.println("\nlist of random forest accuracies" + randomForestAccuracies);
        System.out.println("list of random forest MSE" + randomForestMSE);

        double randomForestAccuracy = randomForestAccuracies.values().stream()
                .filter(Double::isFinite)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(Double.NaN);

        double randomForestMSEs = randomForestMSE.values().stream()
                .filter(Double::isFinite)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(Double.NaN);

        System.out.println("\naverage random forest accuracy: " + randomForestAccuracy);
        System.out.println("average random forest MSE: " + randomForestMSEs);

        //predict the grade for students
        System.out.println("(forest) grade for student 1001196 in course JTE-234: " + tree.predictForestGrade(1001196, studentInfo, "JTE-234", true));
        System.out.println("(forest) grade for student 1002438 in course TGL-013: " + tree.predictForestGrade(1002438, studentInfo, "TGL-013", true));

        System.out.println("(tree) grade for student 1001196 in course JTE-234: " + tree.predictRegressionTreeGrade(1001196, studentInfo, "JTE-234", true));
        System.out.println("(tree) grade for student 1002438 in course TGL-013: " + tree.predictRegressionTreeGrade(1002438, studentInfo, "TGL-013", true));

        unknownStudentInfo.clear();

        double end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) / 1000 + " seconds");
    }
    public static Map<Integer, Map<String, Object>> getStudentInfo() {
        return studentInfo;
    }
    public static Map<Integer, Map<String, Double>> getCurrentGrades() {
        return currentGrades;
    }
}
