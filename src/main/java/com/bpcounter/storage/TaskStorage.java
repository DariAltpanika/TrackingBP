package com.bpcounter.storage;

import com.bpcounter.model.Task;
import com.bpcounter.model.TaskStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TaskStorage {
    //файл для хранения данных
    private static final String SAVE_FILE = "tasks_progress.json";
    // объект который сохраняет все в файл
    private final ObjectMapper mapper;

    //конструктор который создает mapper и форматирует данные в читабельный вид
    public TaskStorage() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    //загрузка из файла
    public Map<Task, TaskStatus> load() {
        File file = new File(SAVE_FILE);
        //если файла не существует
        if (!file.exists()) {
            System.out.println("файл не найден");
            //создаем новую map со значениями по умолчанию
            return initNewStatuses();
        }

        try {
            //читаем содержимое файла
            Map<Task, TaskStatus> loaded = mapper.readValue(file, new TypeReference<HashMap<Task, TaskStatus>>() {
            });
            System.out.println("файл загружен");
            return loaded;
        } catch (IOException e) {
            System.out.println("Ошибка загрузки");
            return initNewStatuses();
        }
    }

    //сохраняем все в файл
    public void save(Map<Task, TaskStatus> taskStatusMap) {
        try {
            mapper.writeValue(new File(SAVE_FILE), taskStatusMap);
            System.out.println("прогресс сохранен");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения");
        }
    }

    //инициализируем map значениями по умолчанию
    private Map<Task, TaskStatus> initNewStatuses() {
        Map<Task, TaskStatus> newMap = new HashMap<>();
        for (Task task : Task.values()) {
            newMap.put(task, new TaskStatus(task));
        }
        return newMap;
    }
}
