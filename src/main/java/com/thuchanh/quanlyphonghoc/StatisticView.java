package com.thuchanh.quanlyphonghoc;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.DatabaseUtil;
import model.Statistic;

public class StatisticView extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("Thống Kê Phòng");
        title.setFont(new Font("Tahoma", 24));

        int totalRooms = DatabaseUtil.getTotalRooms();
        Label totalLabel = new Label("Tổng số phòng: " + totalRooms);
        totalLabel.setFont(new Font("Tahoma", 16));

        int bookedRooms = DatabaseUtil.getBookedRoomCount();
        Label bookedLabel = new Label("Tổng số lượt đặt phòng: " + bookedRooms);
        bookedLabel.setFont(new Font("Tahoma", 16));

        ObservableList<Statistic> typeData = DatabaseUtil.getRoomTypeCounts();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Phòng");
        NumberAxis yAxis = new NumberAxis(0,100,10);
        yAxis.setLabel("Phòng đã đặt");
        LineChart lineChart = new LineChart(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName("Bảng phân bố đặt phòng");
        series.getData().add(new XYChart.Data("Lập trình Java", 2));
        series.getData().add(new XYChart.Data("Phòng gym", 3));
        series.getData().add(new XYChart.Data("Lập trình Mobile", 2));
        series.getData().add(new XYChart.Data("Thị giác máy tính", 7));
        
        lineChart.getData().add(series);

        root.getChildren().addAll(title, totalLabel, bookedLabel, lineChart);

        Scene scene = new Scene(root, 500, 420);
        primaryStage.setTitle("Thống Kê Phòng");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
