package com.example.todolist;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.db.DatabaseHelper;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private Spinner spinnerCategory;
    private RadioGroup radioGroupPriority;
    private TextView tvHeader;

    private DatabaseHelper dbHelper;
    private int taskId = -1; // سالب واحد يعني اضافة غير كده تعديييل فورا

    private final String[] categories = {"Work", "Study", "Personal", "Other"};

    private int currentCompleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        dbHelper = new DatabaseHelper(this);

        etTitle = findViewById(R.id.etTaskTitle);
        etDescription = findViewById(R.id.etTaskDescription);
        spinnerCategory = findViewById(R.id.spinnerTaskCategory);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        tvHeader = findViewById(R.id.tvHeader);
        Button btnSave = findViewById(R.id.btnSaveTask);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        if (getIntent().hasExtra("task_id")) {
            taskId = getIntent().getIntExtra("task_id", -1);
            tvHeader.setText("Edit Task");
            fillFieldsForEdit();
        } else {
            tvHeader.setText("Add new Tsak");
        }

        btnSave.setOnClickListener(v -> saveTask());
    }

    private void fillFieldsForEdit() {
        Task task = dbHelper.getTask(taskId);
        if (task == null) return;

        currentCompleted = task.getCompleted();

        etTitle.setText(task.getTitle());
        etDescription.setText(task.getDescription());

        int categoryIndex = java.util.Arrays.asList(categories).indexOf(task.getCategory());
        if (categoryIndex >= 0) spinnerCategory.setSelection(categoryIndex);

        if (task.getPriority().equals("High")) {
            radioGroupPriority.check(R.id.radioHigh);
        } else if (task.getPriority().equals("Medium")) {
            radioGroupPriority.check(R.id.radioMedium);
        } else {
            radioGroupPriority.check(R.id.radioLow);
        }
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please enter the task title", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioGroupPriority.getCheckedRadioButtonId();
        RadioButton selectedRadio = findViewById(selectedId);
        String priority = selectedRadio.getText().toString();

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        if (taskId == -1) {
            Task newTask = new Task(title, description, category, priority, date, 0);
            dbHelper.insertTask(newTask);
            Toast.makeText(this, "Task Added ٍٍSuccEssfully", Toast.LENGTH_SHORT).show();
        } else {
            Task updatedTask = new Task(taskId, title, description, category, priority, date, currentCompleted);
            dbHelper.updateTask(updatedTask);
            Toast.makeText(this, "Task updated SuccssfUlly", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}

