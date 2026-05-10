package com.trackingbp.storage;

import com.trackingbp.model.Task;
import com.trackingbp.model.TaskStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TaskStorage {
    //файл для хранения данных
    private static final String APP_DIR = System.getProperty("user.home") + File.separator + ".TrackingBPFiles";
    //файл прогресса
    private static final String SAVE_FILE = APP_DIR + File.separator + "tasks_progress.json";
    //файл настроек видимости
    private static final String SAVE_SETTINGS_FILE = APP_DIR + File.separator + "hidden_settings.json";
    // объект который сохраняет все в файл
    private final ObjectMapper mapper;

    //создание папки для файла
    static {
        try {
            Files.createDirectories(Paths.get(APP_DIR));
        } catch (IOException e) {
            System.err.println("Не удалось создать папку для данных: " + e.getMessage());
        }
    }

    //конструктор который создает mapper и форматирует данные в читабельный вид
    public TaskStorage() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    //загрузка из файла
    public Map<Task, TaskStatus> load() {
        File file = new File(SAVE_FILE);
        //если файла не существует
        if (!file.exists()) {
            System.out.println("won't find the file");
            //создаем новую map со значениями по умолчанию
            return initNewStatuses();
        }

        try {
            //читаем содержимое файла
            Map<Task, TaskStatus> loaded = mapper.readValue(file, new TypeReference<HashMap<Task, TaskStatus>>() {
            });
            System.out.println("file uploaded");

            for (Task task : Task.values()) {
                if (!loaded.containsKey(task)) {
                    loaded.put(task, new TaskStatus(task));
                }
            }
            return loaded;
        } catch (IOException e) {
            System.out.println("download error");
            e.printStackTrace();
            return initNewStatuses();
        }

    }

    //сохраняем все в файл
    public void save(Map<Task, TaskStatus> taskStatusMap) {
        try {
            mapper.writeValue(new File(SAVE_FILE), taskStatusMap);
            System.out.println("progress saved");
        } catch (IOException e) {
            System.out.println("save error");
        }
    }

    //сохранение всех скрытых задач в файл
    public void saveHiddenSettings(Set<String> hiddenTasks) {
        try {
            mapper.writeValue(new File(SAVE_SETTINGS_FILE), hiddenTasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //добавление одной задачи в сет со скрытыми файлами и обновление json
    public void addToHidden(String taskName) {
        Set<String> hidden = loadHiddenSettings();
        if (hidden.add(taskName)) {
            saveHiddenSettings(hidden);
        }
    }

    //удаление одной задачи из сета и обновление файла json
    public void removeFromHidden(String taskName) {
        Set<String> hidden = loadHiddenSettings();
        if (hidden.remove(taskName)) {
            saveHiddenSettings(hidden);
        }
    }

    //загрузка задач из файла json
    public Set<String> loadHiddenSettings() {
        File file = new File(SAVE_SETTINGS_FILE);

        if (!file.exists()) {
            return new HashSet<>();
        }

        try {
            Set<String> loaded = mapper.readValue(file, new TypeReference<Set<String>>() {});
            return loaded;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    //сброс всех настроек
    public void factoryReset() {
        saveHiddenSettings(new HashSet<>());
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
