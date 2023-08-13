package com.example.todoapplication;

public class Task {
    private final int id;
    private final String title;
    private boolean completed;
    private int originalPosition = -1;

    public Task(int id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed; // by default, tasks are not completed
    }

    // getter and setter methods for properties
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void toggleCompleted() {
        completed = !completed;
    }

    public int getOriginalPosition() {
        return originalPosition;
    }

    public void setOriginalPosition(int originalPosition) {
        this.originalPosition = originalPosition;
    }

    @Override
    public String toString() {
        return title;
    }
}
