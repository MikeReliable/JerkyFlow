package com.company.JerkyflowProcessing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class SortAndProcessing extends Application {
    public static void main(String[] args) throws ParseException {
        SortAndProcessingMethods methods = new SortAndProcessingMethods();
        methods.sorting();
        methods.processing();

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        init(stage);
    }

    private void init(Stage stage) throws IOException {
        TreeMap<Double, Integer> graph = null;
        SortAndProcessingMethods methods = new SortAndProcessingMethods();
        try {
            graph = methods.processing();
//            System.out.println(graph);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        HBox root = new HBox();
        Scene scene = new Scene(root, 600, 300);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Drop, MPa");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("N");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Drop Numbers");

        XYChart.Series<String, Number> data = new XYChart.Series<>();
        if (graph != null) {
            graph.forEach((key, value) -> {
                String x = String.valueOf(key);
                Integer y = (Integer) value;
                data.getData().add(new XYChart.Data(x, y));
            });
        }


        lineChart.getData().add(data);
        root.getChildren().add(lineChart);

        stage.setScene(scene);
        stage.show();
    }
}


class SortAndProcessingMethods {

    private String line;
    private List<String> lines;
    double stressDrop, stress;
    double stressResult = 0, meanStress = 0;
    int count = 0;
    double k = 0.1; // начальное значение итервала stressDrop
    double m = 50.0; // конечное значение итервала stressDrop
    double n = 0.1; // шаг сканирования  итервала stressDrop

    BufferedReader reader;
    FileWriter fileWriter, fileWriterProcessing;
    Map<String, Double> initialMap = new HashMap<>();
    Map<String, Double> sortedMap;
    Map<Double, Integer> processingMap = new TreeMap<>();


    void sorting() {
        try {
            fileWriter = new FileWriter("src/main/java/com/company/JerkyflowProcessing/SortedAnswer.txt");
            fileWriter.append("time,s\tstrain,%\tstress,MPa\tstressDrop,MPa\tstrainDropLength\n");
            reader = new BufferedReader(new FileReader("src/main/java/com/company/JerkyFlowDetecting/Answer.txt"));
            lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            for (int i = 1; i < lines.size(); i++) {
                String row = lines.get(i);
                String[] values = row.split("\t");
                stressDrop = Double.parseDouble(values[3]);
                initialMap.put(row, stressDrop);
            }
            sortedMap = initialMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors
                    .toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            for (String key : sortedMap.keySet()) {
//                System.out.println(key);
                fileWriter.write(key + "\n");
            }
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TreeMap<Double, Integer> processing() throws ParseException {
        try {
            fileWriterProcessing = new FileWriter("src/main/java/com/company/JerkyflowProcessing/Processing.txt");
            reader = new BufferedReader(new FileReader("src/main/java/com/company/JerkyflowProcessing/SortedAnswer.txt"));
            lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            while (k < m) {
                for (int i = 1; i < lines.size(); i++) {
                    String row = lines.get(i);
                    String[] values = row.split("\t");
                    stress = Double.parseDouble(values[2]);
                    stressDrop = Double.parseDouble(values[3]);
//                System.out.println(stress + " " + stressDrop);
                    if (stressDrop < k + n && stressDrop >= k) {
                        count++;
                        stressResult += stress;
                        System.out.println(count + " " + stressDrop + " " + stressResult);
                    }
                }
                if (stressResult != 0) {
                    meanStress = stressResult / count;
                    DecimalFormat df = new DecimalFormat("###.###");
                    fileWriterProcessing.write((df.format(k)).replace(",", ".") + "\t" + count
                            + "\t" + (df.format(meanStress)).replace(",", ".") + "\n");
                    processingMap.put(k, count);
                    count = 0;
                    stressResult = 0;
                }
                k += n;
            }
            fileWriterProcessing.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return (TreeMap<Double, Integer>) processingMap;
    }
}