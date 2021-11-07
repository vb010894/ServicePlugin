package com.vbsoft.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vbsoft.models.entity.EntityModel;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ServicePluginUtilsTest {

    @Test
    void fillModel() throws JsonProcessingException {
        String url = getClass().getResource("/Models/ModelExample.json").getFile();
        File json = FileUtils.getFile(url);
        assertNotNull(json, "Не найдено файла с json");
        AtomicReference<String> jsonString = new AtomicReference<>();
        assertDoesNotThrow(() ->jsonString.set(FileUtils.readFileToString(json, StandardCharsets.UTF_8)), "При чтении файла обнаружна ошибка");
        assertNotNull(jsonString.get(), "Прочитан пустой файл с json");
        EntityModel model = ServicePluginUtils.fillModel(EntityModel.class, jsonString.get());
        assertNotNull(model, "Модель не заполнена");
    }
}