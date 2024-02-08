package com.company.JerkyflowProcessing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.TreeMap;

public class SortAndProcessing extends Application {
    public static void main(String[] args) throws ParseException, IOException {

        JerkyFinder jerkyFinder = new JerkyFinder();
        FileScanner fileScanner = new FileScanner();
        List<String> rows = fileScanner.fileScanner();
        jerkyFinder.jerkyFinder(rows);

        SortAndProcessingMethods methods = new SortAndProcessingMethods();
        methods.sorting();
        methods.processing();

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        init(stage);
    }

    private void init(Stage primaryStage) {
        TreeMap<Double, Integer> graph = null;
        SortAndProcessingMethods methods = new SortAndProcessingMethods();
        try {
            graph = methods.processing();
//            System.out.println(graph);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        StackPane group = new StackPane();
        Scene scene = new Scene(group);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Drop, MPa");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("N");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Drop Numbers");

        XYChart.Series<String, Number> data = new XYChart.Series<>();
        XYChart.Series<String, Number> points = new XYChart.Series<>();
        if (graph != null) {
            graph.forEach((key, value) -> {
                String x = String.valueOf(key);
                data.getData().add(new XYChart.Data(x, value));

                XYChart.Data chartData;
                chartData = new XYChart.Data(x, value);
                chartData.setNode(new ShowCoordinatesNode(x, value));
//                chartData.getNode().setStyle("-fx-padding: 2px;-fx-background-color: black;");
                points.getData().add(chartData);

            });
        }
        lineChart.getData().add(data);
        lineChart.getData().add(points);
        group.getChildren().add(lineChart);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
