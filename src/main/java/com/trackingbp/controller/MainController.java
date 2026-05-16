package com.trackingbp.controller;
import com.trackingbp.animation.Animations;
import com.trackingbp.model.Task;
import com.trackingbp.model.TaskStatus;
import com.trackingbp.storage.TaskStorage;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainController {
    private HashMap<Task, TaskStatus> taskMap;
    private Set<String> hiddenTasks;
    TaskStorage storage;
    private String nameCurrentValue = "";
    private boolean EMPTY = true;
    private double xOffset = 0;
    private double yOffset = 0;


    @FXML private HBox titleBar;
   @FXML private ListView<String> taskList;
   @FXML private ListView<String> hiddenTaskList;
   @FXML Label taskSelectionText;
    @FXML Label progressBarText;
  @FXML private ProgressBar progressBar;
  @FXML private Button resetButton;
  @FXML private Button buttonResetProgress;
  @FXML private Button closeButton;
  @FXML private Button minimizeButton;
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
        hiddenTasks = storage.loadHiddenSettings();
        buttonLockSetting(taskMap);
       taskListInitialization(hiddenTasks);
       setupSelectionListener();
       setupHiddenTasksList(hiddenTasks);
       setupWindowDragging();

       taskList.refresh();
    }

    //инициализация taskMap
    private void taskListInitialization(Set<String> hiddenTask) {
        taskList.getItems().clear();
        for (Task task : Task.values()) {
                String name = task.getDisplayName();
                if (hiddenTask.contains(name)) {
                    continue;
                }
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

    //инициализация скрытых задач
    private void setupHiddenTasksList(Set<String> hiddenTask) {
        hiddenTaskList.setSelectionModel(null);
        hiddenTaskList.getItems().clear();

        for (Task task : Task.values()) {
            hiddenTaskList.getItems().add(task.getDisplayName());
        }

        hiddenTaskList.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String taskName, boolean empty) {
                super.updateItem(taskName, empty);

                if (empty || taskName == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(10);


                    CheckBox checkBox = new CheckBox();


                    Label label = new Label(taskName);


                    checkBox.setSelected(hiddenTask.contains(taskName));

                    checkBox.selectedProperty().addListener((obs, oldValue, isSelected) -> {
                        if (isSelected) {
                            storage.addToHidden(taskName);
                            hiddenTask.add(taskName);
                            taskList.getItems().remove(taskName);
                        } else {
                            storage.removeFromHidden(taskName);
                            hiddenTask.remove(taskName);

                            if (!taskList.getItems().contains(taskName)) {
                                taskList.getItems().add(taskName);
                                FXCollections.sort(taskList.getItems());
                            }
                        }
                    });
                    hBox.getChildren().addAll(checkBox, label);
                    setGraphic(hBox);
                }
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
                    setStyle(
                            "-fx-background-color: linear-gradient(to right, rgba(45, 106, 79, 0.4), rgba(82, 183, 136, 0.2));" +
                                    "-fx-border-color: #52b788;" +
                                    "-fx-border-width: 0 0 0 3px;" +
                                    "-fx-background-radius: 12px;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-effect: dropshadow(gaussian, #52b788, 5, 0, 0, 0);"
                    );
                } else {
                    setStyle("");
                }
            }
        });
    }

    //Метод для закрытия окна
    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    //Метод для сворачивания окна
    @FXML
    private void handleMinimize() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    //метод слушатель движения окна
    private void setupWindowDragging() {
        if (titleBar != null) {
            titleBar.setOnMousePressed(mouseEvent -> {
                Stage stage = (Stage) titleBar.getScene().getWindow();
                xOffset = stage.getX() - mouseEvent.getScreenX();
                yOffset = stage.getY() - mouseEvent.getScreenY();
            });

            titleBar.setOnMouseDragged(mouseEvent -> {
                Stage stage = (Stage) titleBar.getScene().getWindow();
                stage.setX(mouseEvent.getScreenX() + xOffset);
                stage.setY(mouseEvent.getScreenY() + yOffset);
            });
        }
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
        Animations.showVBoxGrow(boxWithDetails);
        taskInformationLabel.setText(task.getDescription() + " награда: " + task.getReward() + "/" + (task.getReward() * 2));
        moreDetailsButton.setVisible(false);
    }

    //кнопка скрыть подробности
    @FXML
    private void hideClickButton() {
        Animations.hideVBoxShrink(boxWithDetails);
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
        Animations.animateProgress(progressBar, progress);
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
