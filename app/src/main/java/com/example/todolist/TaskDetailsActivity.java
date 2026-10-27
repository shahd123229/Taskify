package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.db.DatabaseHelper;
import com.example.todolist.model.Task;

public class TaskDetailsActivity extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvCategory, tvPriority, tvDate;
    private DatabaseHelper dbHelper;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        dbHelper = new DatabaseHelper(this);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvPriority = findViewById(R.id.tvDetailPriority);
        tvDate = findViewById(R.id.tvDetailDate);

        Button btnEdit = findViewById(R.id.btnDetailEdit);
        Button btnDelete = findViewById(R.id.btnDetailDelete);

        taskId = getIntent().getIntExtra("task_id", -1);
        loadTaskDetails();

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(TaskDetailsActivity.this, AddEditTaskActivity.class);
            intent.putExtra("task_id", taskId);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            dbHelper.deleteTask(taskId);
            Toast.makeText(this, "Task deletd", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    //مننساش نحدث القايمة اكيد

    @Override
    protected void onResume() {
        super.onResume();
        loadTaskDetails();
    }

    private void loadTaskDetails() {
        Task task = dbHelper.getTask(taskId);
        if (task == null) { finish(); return; }

        tvTitle.setText(task.getTitle());
        tvDescription.setText(task.getDescription());
        tvCategory.setText("Category: " + task.getCategory());
        tvPriority.setText("Priority: " + task.getPriority());
        tvDate.setText("Date: " + task.getDate());
    }
}