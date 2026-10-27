package com.example.todolist.model;

public class Task {

    private int id;
    private String title;
    private String description;
    private String category;
    private String priority;
    private String date;
    private int completed;
    public Task(int id, String title, String description, String category, String priority, String date, int completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.date = date;
        this.completed = completed;
    }

    public Task(String title, String description, String category, String priority, String date, int completed) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.date = date;
        this.completed = completed;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getCompleted() { return completed; }
    public void setCompleted(int completed) { this.completed = completed; }
}
