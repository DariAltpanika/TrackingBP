package com.bpcounter.controller;
import com.bpcounter.model.Task;
import com.bpcounter.model.TaskStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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
    @FXML private Button moreDetailsButton;
    @FXML private VBox boxWithDetails;
    @FXML private Button hideButton;
    @FXML Label taskInformationLabel;

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
        initializationCellFactory();
    }

    //установка слушателя на список заданий
    private void setupSelectionListener() {
        taskList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                onTaskSelected(newVal);
            }
        });
    }

    //метод установки цвета ячейки таблицы
    private void initializationCellFactory() {
        taskList.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(item);
                TaskStatus taskStatus = taskMap.get(getTaskByDisplayName(item));
                if (taskStatus != null && taskStatus.isTaskCompleted()) {
                    setStyle("-fx-background-color: #51cf66; -fx-text-fill: white;");
                } else {
                    setStyle("");
                }
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
        buttonShowDetailsSetting();
        moreDetailsButton.setVisible(true);

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
        taskList.refresh();
    }

    //метод обработки кнопки "добавить все"
    @FXML
    private void everythingClickButton() {
        TaskStatus taskStatus = taskMap.get(getTaskByDisplayName());
        taskStatus.addProgress(taskStatus.getRemainingCount());
        updateProgressBar(taskStatus);
        completeEverythingButton.setDisable(true);
        setValueButton.setDisable(true);
        taskList.refresh();
    }

    //кнопка показать подробности
    @FXML
    private void buttonClickShowDetails() {
        Task task = getTaskByDisplayName();
        taskInformationLabel.setText(task.getDescription() + " награда: " + task.getReward());
        boxWithDetails.setVisible(true);
        moreDetailsButton.setVisible(false);
    }

    //кнопка скрыть подробности
    @FXML
    private void hideClickButton() {
        boxWithDetails.setVisible(false);
        moreDetailsButton.setVisible(true);
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

    //проверка на включенный Vbox с деталями задачи
    private void buttonShowDetailsSetting() {
        if (boxWithDetails.visibleProperty().getValue()) {
            boxWithDetails.setVisible(false);
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

    //получение задания по имени для фабрики
    private Task getTaskByDisplayName(String displayName) {
        for (Task task : Task.values()) {
            if (task.getDisplayName().equals(displayName)) return task;
        }
        return null;
    }
}
