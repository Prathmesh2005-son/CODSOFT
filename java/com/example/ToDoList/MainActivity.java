package com.example.ToDoList;


import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasktrack.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.TaskAdapterListener {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private TaskDatabaseHelper dbHelper;
    private List<Task> taskList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);

        dbHelper = new TaskDatabaseHelper(this);
        taskList = dbHelper.getAllTasks();

        adapter = new TaskAdapter(this, taskList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(view -> showAddEditDialog(null));
    }

    private void showAddEditDialog(@Nullable Task taskToEdit) {
        AddEditTaskDialog dialog = AddEditTaskDialog.newInstance(taskToEdit, newTask -> {
            if (taskToEdit == null) {
                dbHelper.addTask(newTask);
                taskList.add(newTask);
                Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.updateTask(newTask);
                int index = getIndexById(newTask.getId());
                if (index >= 0) taskList.set(index, newTask);
                Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        });
        dialog.show(getSupportFragmentManager(), "AddEditTaskDialog");
    }

    private int getIndexById(int id) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId() == id) return i;
        }
        return -1;
    }

    @Override
    public void onEditClicked(Task task) {
        showAddEditDialog(task);
    }

    @Override
    public void onDeleteClicked(Task task) {
        dbHelper.deleteTask(task.getId());
        taskList.remove(task);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(Task task, boolean isChecked) {
        task.setCompleted(isChecked);
        dbHelper.updateTask(task);
        Toast.makeText(this, isChecked ? "Marked Completed" : "Marked Active", Toast.LENGTH_SHORT).show();
    }
}
