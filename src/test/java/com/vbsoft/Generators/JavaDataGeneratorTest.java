package com.vbsoft.Generators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intellij.openapi.ui.Messages;
import com.vbsoft.Utils.ServicePluginUtils;
import com.vbsoft.menu.ModelGenerator;
import com.vbsoft.models.entity.EntityModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.apache.tools.ant.taskdefs.SetPermissions.NonPosixMode.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JavaDataGeneratorTest {

    private static Configuration fConfig;

    @BeforeAll
    public static void init() {
        fConfig = new Configuration(Configuration.VERSION_2_3_28);
        fConfig.setClassLoaderForTemplateLoading(JavaDataGeneratorTest.class.getClassLoader(),"/fileTemplates");
        fConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        fConfig.setDefaultEncoding("UTF-8");
        fConfig.setWrapUncheckedExceptions(true);
        fConfig.setLogTemplateExceptions(false);
    }


    private EntityModel fillModel() {
        String url = getClass().getResource("/Models/ModelExample.json").getFile();
        File json = FileUtils.getFile(url);
        assertNotNull(json, "Не найдено файла с json");
        AtomicReference<String> jsonString = new AtomicReference<>();
        assertDoesNotThrow(() ->jsonString.set(FileUtils.readFileToString(json, StandardCharsets.UTF_8)), "При чтении файла обнаружна ошибка");
        assertNotNull(jsonString.get(), "Прочитан пустой файл с json");
        EntityModel model = assertDoesNotThrow(() -> ServicePluginUtils.fillModel(EntityModel.class, jsonString.get()));
        assertNotNull(model, "Модель не заполнена");
        return model;
    }

    @Test
    public void generate() {
        Map<String, Object> freemarkerDataModel = new HashMap<>();
        Template template = assertDoesNotThrow(() -> fConfig.getTemplate("TestClassTemplate.ftl"), "Ошибка чтения шаблона");
        String outputPath = "D:/temp";
        List<Throwable> throwFlag = new LinkedList<>();
        List<EntityModel> specifications = Collections.singletonList(this.fillModel());
        specifications.forEach(s -> {
            try {

                File javaSourceFile = new File(outputPath, s.getClassName() + ".java");
                Writer javaSourceFileWriter = new FileWriter(javaSourceFile);
                freemarkerDataModel.put("classSpecification", s);
                template.process(freemarkerDataModel, javaSourceFileWriter);

            } catch (TemplateException e) {
                throwFlag.add(e);
            } catch (IOException e) {
                throwFlag.add(e);
            }
        });

        if (!throwFlag.isEmpty()) {
            String messages = throwFlag.stream().map(e -> e.getMessage()).collect(Collectors.joining(". \n"));
                Assertions.fail(messages);

        }
    }
}