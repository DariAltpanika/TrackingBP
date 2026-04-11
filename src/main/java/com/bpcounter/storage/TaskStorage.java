package com.bpcounter.storage;

import com.bpcounter.model.Task;
import com.bpcounter.model.TaskStatus;

import java.util.HashMap;

public class TaskStorage {
    private HashMap<Task, TaskStatus> taskMap = new HashMap<>();
}
