package com.trackingbp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskStatus {
    //задача
    private Task task;
    //количество сделаных действий
    private int currentCount = 0;
    private boolean taskCompleted = false;


    //конструктор для Json
    @JsonCreator
    public TaskStatus(@JsonProperty("task") Task task) {
        this.task = task;
    }

    //добавление прогресса
    public boolean addProgress(int count) {
        if (taskCompleted || count <= 0) {
            return false;
        }
        //защита от переполнения в случае если count больше чем task.getCount
        currentCount = Math.min(currentCount + count, task.getCount());

        return checkForTaskCompletion();
    }

    //проверка сделано ли достаточно действий для завершения, скрыта от пользователей
    private boolean checkForTaskCompletion() {
        //если флаг еще не true но currentCount >= task.getCount то присвоить значение флагу true и вернуть true, это нужно чтобы избежать дублирование
        if (!taskCompleted && currentCount >= task.getCount()) {
            taskCompleted = true;
            return true;
        }
        return false;
    }

    //пользовательский метод по получению информации о том, выполнена ли задача
    public boolean isTaskCompleted() {
        return taskCompleted;
    }

    //установить значение Completed (для Json)
    public void setCompleted(boolean completed) {
        this.taskCompleted = completed;
    }

    //получение количества сделаных действий (опционально)
    public int getCurrentCount() {
        return currentCount;
    }

    //установить значение currentCount (для Json)
    public void setCurrentCount(int count) {
        this.currentCount = count;
    }

    //сколько осталось сделать действий(опционально)
    public int getRemainingCount() {
        return task.getCount() - currentCount;
    }

    //получение задачи
    public Task getTask() {
        return task;
    }

    //установить значение task (для Json)
    public void setTask(Task task) {
        this.task = task;
    }

    //сброс прогресса
    public void reset() {
        currentCount = 0;
        taskCompleted = false;
    }
}
