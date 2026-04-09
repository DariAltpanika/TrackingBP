package com.bpcounter.controller;
import com.bpcounter.model.Task;
import com.bpcounter.model.TaskStatus;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.HashMap;

public class MainController {
    private HashMap<Task, TaskStatus> taskMap = new HashMap<>();
   @FXML private ListView taskList;

    @FXML
    public void initialize() {
       hashMapAndTaskListInitialization();
    }

    //инициализация taskMap
    private void hashMapAndTaskListInitialization() {
        for (Task task : Task.values()) {
            String name = task.getDisplayName();
            taskList.getItems().add(name);
            taskMap.put(task, new TaskStatus(task));
        }
    }
}
