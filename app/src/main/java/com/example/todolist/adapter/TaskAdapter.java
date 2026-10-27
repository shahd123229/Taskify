package com.example.todolist.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddEditTaskActivity;
import com.example.todolist.R;
import com.example.todolist.TaskDetailsActivity;
import com.example.todolist.db.DatabaseHelper;
import com.example.todolist.model.Task;

import android.graphics.Paint;

import java.util.List;

// adaapter المسءول عن عرض كل مهمة
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList;
    private DatabaseHelper dbHelper;

    // متغير بيحفظ اخر مهمة اتضغط عليها ..ضغط طوييييلcontext
    public Task selectedTask;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        this.dbHelper = new DatabaseHelper(context);
    }

    //بتحدث القايمة بعد الفلتر او بعد ااي تعديل
    public void updateList(List<Task> newList) {
        this.taskList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.tvTitle.setText(task.getTitle());
        // لو المهمةاتعملت اعمل خط على عنوانها
        if (task.getCompleted() == 1) {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.tvCategory.setText(task.getCategory());
        holder.tvPriority.setText(task.getPriority());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaskDetailsActivity.class);
            intent.putExtra("task_id", task.getId());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            selectedTask = task;
            return false;
        });

//        زرار ال 3 نقط popup
        holder.btnMore.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context,   holder.btnMore);
            popupMenu.getMenuInflater().inflate(R.menu.menu_popup_task, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_edit) {
                    Intent intent = new Intent(context, AddEditTaskActivity.class);
                    intent.putExtra("task_id", task.getId());
                    Toast.makeText(context, " The task Edited Successfully ", Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.action_delete) {
                    dbHelper.deleteTask(task.getId());
                    Toast.makeText(context, "The  tsak deleted successfully", Toast.LENGTH_SHORT).show();
                    updateList(dbHelper.getAllTasks());
                    return true;
                }

                  else if (item.getItemId() == R.id.action_complete) {
                    boolean newStatus = task.getCompleted() != 1;
                    dbHelper.toggleTaskCompleted(task.getId(), newStatus);
                    Toast.makeText(context, newStatus ? " Marked as done" : "Marked as not done", Toast.LENGTH_SHORT).show();
                    updateList(dbHelper.getAllTasks());
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }
///////////////////////////////////////////////////////////////
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvPriority;
        View btnMore;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvCategory = itemView.findViewById(R.id.tvTaskCategory);
            tvPriority = itemView.findViewById(R.id.tvTaskPriority);
            btnMore = itemView.findViewById(R.id.btnMore);
        }
    }
}
