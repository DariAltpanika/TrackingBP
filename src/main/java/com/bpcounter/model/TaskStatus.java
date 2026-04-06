package com.bpcounter.model;

public class TaskStatus {
    //задача
    private final Task task;
    //количество сделаных действий
    private int currentCount = 0;
    private boolean completed = false;

    public TaskStatus(Task task) {
        this.task = task;
    }

    //добавление прогресса
    public boolean addProgress(int count) {
        if (completed || count <= 0) {
            return false;
        }
        //защита от переполнения в случае если count больше чем task.getCount
        currentCount = Math.min(currentCount + count, task.getCount());

        return checkForTaskCompletion();
    }

    //проверка сделано ли достаточно действий для завершения, скрыта от пользователей
    private boolean checkForTaskCompletion() {
        //если флаг еще не true но currentCount >= task.getCount то присвоить значение флагу true и вернуть true, это нужно чтобы избежать дублирование
        if (!completed && currentCount >= task.getCount()) {
            completed = true;
            return true;
        }
        return false;
    }

    //пользовательский метод по получению информации о том, выполнена ли задача
    public boolean isTaskCompleted() {
        return completed;
    }

    //получение количества сделаных действий (опционально)
    public int getCurrentCount() {
        return currentCount;
    }

    //сколько осталось сделать действий(опционально)
    public int getRemainingCount() {
        return task.getCount() - currentCount;
    }

    //сброс прогресса
    public void reset() {
        currentCount = 0;
        completed = false;
    }
}
