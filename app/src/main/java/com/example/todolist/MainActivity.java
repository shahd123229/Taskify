package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.adapter.TaskAdapter;
import com.example.todolist.db.DatabaseHelper;
import com.example.todolist.model.Task;

import java.util.ArrayList;
import java.util.List;

import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private DatabaseHelper dbHelper;
    private Spinner spinnerCategory;
    private List<Task> allTasks;

    private final String[] categories = {"All", "Work", "Study", "Personal", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        registerForContextMenu(recyclerView);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterByCategory(categories[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        findViewById(R.id.fabAddTask).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });
    }
// مننساش نحدث المهام بعد تعديييل او اضافة جديدة او مسح طبعا
    @Override
    protected void onResume() {
        super.onResume();
        allTasks = dbHelper.getAllTasks();
        adapter.updateList(allTasks);
    }

    private void filterByCategory(String category) {
        if (category.equals("All")) {
            adapter.updateList(allTasks);
            return;
        }
        List<Task> filtered = new ArrayList<>();
        for (Task task : allTasks) {
            if (task.getCategory().equals(category)) {
                filtered.add(task);
            }
        }
        adapter.updateList(filtered);
    }

    // ترتيب المهام حسب الأولوية high الأول... بعدها Medum.... بعدها low
    private void sortTasksByPriority() {
        Collections.sort(allTasks, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return getPriorityValue(t1.getPriority()) - getPriorityValue(t2.getPriority());
            }
        });
        adapter.updateList(allTasks);
    }

    private int getPriorityValue(String priority) {
        switch (priority) {
            case "High": return 1;
            case "Medium": return 2;
            case "Low": return 3;
            default: return 4;
        }
    }

    ////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if (id == R.id.sort_by_date) {
            Toast.makeText(this, "Sorted by Date", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.sort_by_priority) {
            sortTasksByPriority();
            Toast.makeText(this, "Sorted by priority", Toast.LENGTH_SHORT).show();
            return true;
        }

          else if (id == R.id.action_delete_all) {
            dbHelper.deleteAllTasks();
            Toast.makeText(this, "All tasks deleted", Toast.LENGTH_SHORT).show();
            allTasks = dbHelper.getAllTasks();
            adapter.updateList(allTasks);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context_task, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (adapter.selectedTask == null) return super.onContextItemSelected(item);

        int id = item.getItemId();
        if (id == R.id.context_edit) {
            Intent intent = new Intent(this, AddEditTaskActivity.class);
            intent.putExtra("task_id", adapter.selectedTask.getId());
            startActivity(intent);
            return true;
        } else if (id == R.id.context_delete) {
            dbHelper.deleteTask(adapter.selectedTask.getId());
            Toast.makeText(this, "Task deletted", Toast.LENGTH_SHORT).show();
            allTasks = dbHelper.getAllTasks();
            adapter.updateList(allTasks);
            return true;
        }
        return super.onContextItemSelected(item);
    }
}