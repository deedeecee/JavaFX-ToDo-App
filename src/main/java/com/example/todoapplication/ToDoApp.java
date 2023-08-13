package com.example.todoapplication;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;

public class ToDoApp extends Application {
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        loadTasksFromFile();    // loading tasks from file
        primaryStage.setTitle("ToDo Application");

        // creating UI components
        ListView<Task> taskListView = new ListView<>();
        TextField taskInput = new TextField();
        Button addButton = new Button("+");
        taskInput.setPromptText("Enter a new task...");

        // inline styling
        addButton.setStyle(
                "-fx-background-color: #3498db; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8px 12px; " +
                        "-fx-background-radius: 5px;"
        );

        taskInput.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: #3498db; " +
                        "-fx-text-fill: #333333; " +
                        "-fx-font-size: 14px; " +
                        "-fx-prompt-text-fill: #a0a0a0; " +
                        "-fx-padding: 6px;"
        );

        taskListView.setStyle(
                "-fx-background-color: #f7f7f7;"
        );

        addButton.setOnAction(e -> {
            String taskTitle = taskInput.getText();
            if (!taskTitle.isEmpty()) {
                Task newTask = new Task(tasks.size() + 1, taskTitle, false);
                tasks.add(0, newTask); // a new task is added at the top of the list
                taskInput.clear();
            }
        });


        HBox inputBox = new HBox(5, taskInput, addButton);
        inputBox.setAlignment(Pos.CENTER);

        HBox hBox = new HBox(10, inputBox);
        hBox.setAlignment(Pos.CENTER);


        // list item appearance
        taskListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    CheckBox checkBox = new CheckBox(task.getTitle());
                    checkBox.setSelected(task.isCompleted());

                    checkBox.setOnAction(event -> {
                        task.toggleCompleted();
                        if (task.isCompleted()) {
                            task.setOriginalPosition(tasks.indexOf(task));
                            tasks.remove(task);
                            tasks.add(task);
                        } else {
                            int originalPosition = task.getOriginalPosition();
                            if (originalPosition >= 0 && originalPosition < tasks.size()) {
                                tasks.remove(task);
                                tasks.add(originalPosition, task);
                            }
                        }
                        taskListView.refresh();
                    });

                    if (task.isCompleted()) {
                        checkBox.setStyle("-fx-text-fill: #27ae60;"); // green for completed tasks
                    } else {
                        checkBox.setStyle("-fx-text-fill: #333333;"); // default color for incomplete tasks
                    }

                    Button deleteButton = new Button("Delete");
                    deleteButton.setStyle(
                            "-fx-background-color: transparent; " +
                                    "-fx-text-fill: #e74c3c; " +
                                    "-fx-font-size: 10px; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-padding: 4px 8px; " +
                                    "-fx-border-color: #e74c3c; " +
                                    "-fx-border-radius: 5px;"
                    );


                    deleteButton.setOnAction(event -> {
                        tasks.remove(task);
                    });

                    HBox hbox = new HBox(10, checkBox, deleteButton);
                    setGraphic(hbox);
                    setText(null);
                }
            }
        });

        taskListView.setItems(tasks);

        VBox root = new VBox(10, hBox, taskListView);
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 300, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        saveTasksToFile(); // saving tasks to file before the application closes
    }


    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
            for (Task task : tasks) {
                String taskLine = (task.isCompleted() ? "[x]" : "[]") + " " + task.getTitle();
//                System.out.println("Saving task: " + taskLine);
                writer.write(taskLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadTasksFromFile() {
        tasks.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                boolean isCompleted = line.startsWith("[x]");
                String title = "";
                if (!isCompleted) {
                    title = line.substring(3);
                } else {
                    title = line.substring(4);
                }
                Task task = new Task(tasks.size() + 1, title, isCompleted);
                tasks.add(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
