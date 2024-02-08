package com.company.JerkyflowProcessing;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.text.DecimalFormat;

public class ShowCoordinatesNode extends StackPane {
    public ShowCoordinatesNode(String x, double y) {

        final Label label = createDataThresholdLabel(Double.parseDouble(x), y);

        setOnMouseEntered(mouseEvent -> {
            setScaleX(1);
            setScaleY(1);
            getChildren().setAll(label);
            setCursor(Cursor.NONE);
            toFront();
        });
        setOnMouseExited(mouseEvent -> {
            getChildren().clear();
            setCursor(Cursor.CROSSHAIR);
        });
    }

    private Label createDataThresholdLabel(double x, double y) {
        DecimalFormat df = new DecimalFormat("0.##");
        final Label label = new Label("(" + df.format(x) + "; " + df.format(y) + ")");
        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
        label.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");
        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }
}
