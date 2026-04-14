package com.bpcounter.controller;
import com.bpcounter.model.Task;
import com.bpcounter.model.TaskStatus;
import com.bpcounter.storage.TaskStorage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class MainController {
    private HashMap<Task, TaskStatus> taskMap;
    TaskStorage storage;
    private String nameCurrentValue = "";
    private boolean EMPTY = true;


   @FXML private ListView<String> taskList;
   @FXML Label taskSelectionText;
    @FXML Label progressBarText;
  @FXML private ProgressBar progressBar;
  @FXML private Button resetButton;
  @FXML private Button buttonResetProgress;
    @FXML private Button completeEverythingButton;
    @FXML private Button setValueButton;
    @FXML private Button moreDetailsButton;
    @FXML private VBox boxWithDetails;
    @FXML private Button hideButton;
    @FXML Label taskInformationLabel;


    //блок инициализации
    @FXML
    public void initialize() {
        storage = new TaskStorage();
        taskMap = (HashMap<Task, TaskStatus>) storage.load();
        buttonLockSetting(taskMap);
       taskListInitialization();
       setupSelectionListener();
       taskList.refresh();
    }

    //инициализация taskMap
    private void taskListInitialization() {
        taskList.getItems().clear();
        for (Task task : Task.values()) {
            String name = task.getDisplayName();
            taskList.getItems().add(name);
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
                    setStyle("-fx-background-color: #3b7a4a");
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
        buttonResetProgress.setVisible(true);

        TaskStatus taskStatus = taskMap.get(getTaskByDisplayName());
        buttonLockSetting(taskStatus);
        updateProgressBar(taskStatus);
    }

    //метод обработки кнопки +1
    @FXML
    private void oneClickButton() {
        TaskStatus taskStatus = taskMap.get(getTaskByDisplayName());
           taskStatus.addProgress(1);
           updateProgressBar(taskStatus);

           buttonLockSetting(taskStatus);

           if (EMPTY) {
               resetButton.setDisable(false);
               EMPTY = false;
           }

        taskList.refresh();
        storage.save(taskMap);
    }

    //метод обработки кнопки "добавить все"
    @FXML
    private void everythingClickButton() {
        TaskStatus taskStatus = taskMap.get(getTaskByDisplayName());
        taskStatus.addProgress(taskStatus.getRemainingCount());
        updateProgressBar(taskStatus);

        if (EMPTY) {
            resetButton.setDisable(false);
            EMPTY = false;
        }

        completeEverythingButton.setDisable(true);
        setValueButton.setDisable(true);
        taskList.refresh();
        storage.save(taskMap);
    }

    //кнопка показать подробности
    @FXML
    private void buttonClickShowDetails() {
        Task task = getTaskByDisplayName();
        boxWithDetails.setVisible(true);
        taskInformationLabel.setText(task.getDescription() + " награда: " + task.getReward() + "/" + (task.getReward() * 2));
        moreDetailsButton.setVisible(false);
    }

    //кнопка скрыть подробности
    @FXML
    private void hideClickButton() {
        boxWithDetails.setVisible(false);
        moreDetailsButton.setVisible(true);
    }

    //сброс прогресса
    @FXML
    private void resetClickButton() {
        for (TaskStatus t : taskMap.values()) {
            t.reset();
        }
        storage.save(taskMap);
        if (getTaskByDisplayName() != null) {
            TaskStatus taskStatus = taskMap.get(getTaskByDisplayName());
            buttonLockSetting(taskStatus);
            updateProgressBar(taskStatus);
        }

        EMPTY = true;
        resetButton.setDisable(true);

        taskList.refresh();
    }

    //сброс прогресса определенной задачи
    @FXML
    private void buttonClickResetProgress() {
        if (getTaskByDisplayName() != null) {
            TaskStatus taskStatus = taskMap.get(getTaskByDisplayName());
                taskStatus.reset();
                buttonLockSetting(taskStatus);
                updateProgressBar(taskStatus);
                storage.save(taskMap);
                taskList.refresh();
                buttonLockSetting(taskMap);
        }
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

        if (taskStatus.getCurrentCount() > 0) {
            buttonResetProgress.setDisable(false);
        } else {
            buttonResetProgress.setDisable(true);
        }
    }

    //настройка блокировки кнопки сброса вех задач
    private void buttonLockSetting(Map<Task, TaskStatus> map) {
        for (TaskStatus t : map.values()) {
            if (t.getCurrentCount() > 0) {
                EMPTY = false;
                resetButton.setDisable(false);
                return;
            }
        }
        EMPTY = true;
        resetButton.setDisable(true);
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
