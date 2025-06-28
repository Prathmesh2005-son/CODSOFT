package com.example.ToDoList;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditTaskDialog extends DialogFragment {

    public interface TaskSaveListener {
        void onTaskSaved(Task task);
    }

    private static final String ARG_TASK = "task_arg";
    private Task taskToEdit;
    private TaskSaveListener listener;

    private EditText titleInput, descInput, dueDateInput;
    private Spinner prioritySpinner;
    private Calendar calendar;

    public static AddEditTaskDialog newInstance(Task task, TaskSaveListener listener) {
        AddEditTaskDialog dialog = new AddEditTaskDialog();
        dialog.taskToEdit = task;
        dialog.listener = listener;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_edit_task, null);

        titleInput = view.findViewById(R.id.editTitle);
        descInput = view.findViewById(R.id.editDescription);
        dueDateInput = view.findViewById(R.id.editDueDate);
        prioritySpinner = view.findViewById(R.id.spinnerPriority);
        Button saveButton = view.findViewById(R.id.btnSave);

        calendar = Calendar.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        dueDateInput.setOnClickListener(v -> new DatePickerDialog(getContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        if (taskToEdit != null) {
            titleInput.setText(taskToEdit.getTitle());
            descInput.setText(taskToEdit.getDescription());
            dueDateInput.setText(taskToEdit.getDueDate());
            int spinnerPos = adapter.getPosition(taskToEdit.getPriority());
            prioritySpinner.setSelection(spinnerPos);
        }

        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String desc = descInput.getText().toString().trim();
            String dueDate = dueDateInput.getText().toString();
            String priority = prioritySpinner.getSelectedItem().toString();

            if (title.isEmpty()) {
                titleInput.setError("Title required");
                return;
            }

            Task task = taskToEdit == null ? new Task(0, title, desc, dueDate, priority, false)
                    : new Task(taskToEdit.getId(), title, desc, dueDate, priority, taskToEdit.isCompleted());

            listener.onTaskSaved(task);
            dismiss();
        });

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle(taskToEdit == null ? "Add Task" : "Edit Task")
                .create();
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        dueDateInput.setText(sdf.format(calendar.getTime()));
    }
}