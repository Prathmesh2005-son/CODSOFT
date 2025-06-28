package com.example.ToDoList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> taskList;
    private final Context context;
    private final TaskAdapterListener listener;

    public interface TaskAdapterListener {
        void onEditClicked(Task task);
        void onDeleteClicked(Task task);
        void onStatusChanged(Task task, boolean isChecked);
    }

    public TaskAdapter(Context context, List<Task> taskList, TaskAdapterListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
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
        holder.title.setText(task.getTitle());
        holder.dueDate.setText("Due: " + task.getDueDate());
        holder.completed.setChecked(task.isCompleted());

        holder.completed.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            listener.onStatusChanged(task, isChecked);
        });

        holder.edit.setOnClickListener(v -> listener.onEditClicked(task));
        holder.delete.setOnClickListener(v -> listener.onDeleteClicked(task));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, dueDate;
        CheckBox completed;
        ImageView edit, delete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitle);
            dueDate = itemView.findViewById(R.id.taskDueDate);
            completed = itemView.findViewById(R.id.taskCompleted);
            edit = itemView.findViewById(R.id.editTask);
            delete = itemView.findViewById(R.id.deleteTask);
        }
    }
}