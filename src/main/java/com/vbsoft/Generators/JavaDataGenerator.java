package com.vbsoft.Generators;

import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.vbsoft.models.entity.EntityModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaDataGenerator {

    private final Configuration fConfig;
    private List<ClassSpecification> specifications;

    public JavaDataGenerator() {
        this.fConfig = new Configuration(Configuration.VERSION_2_3_28);
        this.fConfig.setClassLoaderForTemplateLoading(getClass().getClassLoader(),"/fileTemplates");
        this.fConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        this.fConfig.setDefaultEncoding("UTF-8");
        this.fConfig.setWrapUncheckedExceptions(true);
        this.fConfig.setLogTemplateExceptions(false);
    }


    public void generate(List<ClassSpecification> specifications, PsiDirectory output) throws IOException {
        Map<String, Object> freemarkerDataModel = new HashMap<>();
        Template template = fConfig.getTemplate("TestClassTemplate.ftl");
        String outputPath = output.getVirtualFile().getPath();
        List<Throwable> throwFlag = new LinkedList<>();
        specifications.forEach(s -> {
            try {

                File javaSourceFile = new File(outputPath, s.getName() + ".java");
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
            Messages.showMessageDialog(
                    new String(("При создании моделей обнаружены следующие ошибки.\n" + messages).getBytes(), StandardCharsets.UTF_8),
                    new String("Ошибки создания моделей".getBytes(), StandardCharsets.UTF_8),
                    Messages.getWarningIcon());
        }
    }
}
