package com.bpcounter.controller;
import com.bpcounter.model.Task;
import com.bpcounter.model.TaskStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;

public class MainController {
    private HashMap<Task, TaskStatus> taskMap = new HashMap<>();
    private String nameCurrentValue = "";
   @FXML private ListView<String> taskList;
   @FXML Label taskSelectionText;
   @FXML Label progressBarText;
  @FXML private ProgressBar progressBar;
 @FXML private Button completeEverythingButton;
    @FXML private Button setValueButton;

    @FXML
    public void initialize() {
       hashMapAndTaskListInitialization();
       setupSelectionListener();
    }

    //инициализация taskMap
    private void hashMapAndTaskListInitialization() {
        for (Task task : Task.values()) {
            String name = task.getDisplayName();
            taskList.getItems().add(name);
            taskMap.put(task, new TaskStatus(task));
        }
    }

    //установка слушателя на список заданий
    private void setupSelectionListener() {
        taskList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                onTaskSelected(newVal);
            }
        });
    }

    //метод обработки выбранного задания
    private void onTaskSelected(String nameTask) {
        taskSelectionText.setText(nameTask);
        nameCurrentValue = nameTask;

        progressBarText.setVisible(true);
        progressBar.setVisible(true);

        completeEverythingButton.setVisible(true);

        setValueButton.setVisible(true);

        TaskStatus taskStatus = taskMap.get(getTaskByDisplayName());
        buttonLockSetting(taskStatus);
        updateProgressBar(taskStatus);
    }

    //метод обработки кнопки
    @FXML
    private void oneClickButton() {
        TaskStatus taskStatus = taskMap.get(getTaskByDisplayName());
           taskStatus.addProgress(1);
           updateProgressBar(taskStatus);
           buttonLockSetting(taskStatus);
    }

    //метод обработки кнопки "добавить все"
    @FXML
    private void everythingClickButton() {
        TaskStatus taskStatus = taskMap.get(getTaskByDisplayName());
        taskStatus.addProgress(taskStatus.getRemainingCount());
        updateProgressBar(taskStatus);
        completeEverythingButton.setDisable(true);
        setValueButton.setDisable(true);
    }

    //настройка блокировки кнопки
    private void buttonLockSetting(TaskStatus taskStatus) {
        if (taskStatus.isTaskCompleted()) {
            setValueButton.setDisable(true);
            completeEverythingButton.setDisable(true);
        } else {
            if (setValueButton.disableProperty().getValue()) setValueButton.setDisable(false);
            if (completeEverythingButton.disableProperty().getValue()) completeEverythingButton.setDisable(false);
        }
    }

    //обновляет прогресс бар
    private void updateProgressBar(TaskStatus taskStatus) {
         int currentValue = taskStatus.getCurrentCount();
         int maxValue = currentValue + taskStatus.getRemainingCount();
         double progress = (double) currentValue / maxValue;
         progressBarText.setText(currentValue + "/" + maxValue);
         progressBar.setProgress(progress);
    }

    //получение задания по имени
    private Task getTaskByDisplayName() {
        for (Task task : Task.values()) {
            if (task.getDisplayName().equals(nameCurrentValue)) return task;
        }
        return null;
    }
}
