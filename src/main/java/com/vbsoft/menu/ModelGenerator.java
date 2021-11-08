package com.vbsoft.menu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.vbsoft.Generators.JavaDataGenerator;
import com.vbsoft.Utils.ServicePluginUtils;
import com.vbsoft.models.entity.EntityModel;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ModelGenerator extends AnAction {

    /**
     * Resource directory.
     */
    private PsiDirectory fResource;

    /**
     * When model generator activated.
     *
     * @param e Carries information on the invocation place
     */
    @SuppressWarnings("deprecation")
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiDirectory root = PsiDirectoryFactory.getInstance(Objects.requireNonNull(e.getProject())).createDirectory(e.getProject().getBaseDir());
        PsiDirectory outputRoot = ServicePluginUtils.findDirectory(root, "src/main/java");
        this.fResource = ServicePluginUtils.findDirectory(root, "src/main/resources").findSubdirectory("Models");

        Properties structureMap = new Properties();
        try {
            structureMap.load(this.getClass().getResourceAsStream("/Properties/Structure.properties"));
        } catch (IOException ex) {
            Messages.showMessageDialog(
                    new String(("Модели не созданы. Не удалось получить файл с описанием структуры сервиса.\n Сообщение: \n" + ex.getMessage()).getBytes(), StandardCharsets.UTF_8),
                    new String("Создвние моделей".getBytes(), StandardCharsets.UTF_8),
                    Messages.getErrorIcon());
            return;
        }

        List<EntityModel> specifications = this.getJsonData();

        if (specifications.isEmpty()) {
            Messages.showMessageDialog(
                    new String("Не удалось сконвертировать JSON спецификации".getBytes(), StandardCharsets.UTF_8),
                    new String("Невозможно создать модели".getBytes(), StandardCharsets.UTF_8),
                    Messages.getErrorIcon());
            return;
        }

        JavaDataGenerator gen = new JavaDataGenerator();
        try {
            PsiDirectory dir = ServicePluginUtils.findDirectory(outputRoot, ((String) structureMap.get("path.models")).replace(".", "/"));
            gen.generate(specifications, dir);

        } catch (IOException ex) {
            Messages.showMessageDialog(
                    new String(("Модели не созданы. Ошибка чтения шаблона модели.\n Сообщение: \n" + ex.getMessage()).getBytes(), StandardCharsets.UTF_8),
                    new String("Создвние моделей".getBytes(), StandardCharsets.UTF_8),
                    Messages.getErrorIcon());
        }

        String classNames = specifications.stream().map(EntityModel::getClassName).collect(Collectors.joining(",\n"));
        Messages.showMessageDialog(
                new String(("Модели успешно созданы:\n " + classNames).getBytes(), StandardCharsets.UTF_8),
                new String("Создвние моделей".getBytes(), StandardCharsets.UTF_8),
                Messages.getInformationIcon());

    }

    /**
     * Get model definition as psi file.
     * @return Model definitions
     */
    private List<PsiFile> getPSIModelDefinitions() {
        if (this.fResource == null) {
            Messages.showMessageDialog(
                    new String("В ресурсах отсутствует папка 'Models'".getBytes(), StandardCharsets.UTF_8),
                    new String("Невозможно создать модели".getBytes(), StandardCharsets.UTF_8),
                    Messages.getWarningIcon());
            return new ArrayList<>();
        }
        PsiFile[] files = this.fResource.getFiles();
        if (files.length == 0) {
            Messages.showMessageDialog(
                    new String("В ресурсах (папка 'Models') отсутствуют файлы".getBytes(), StandardCharsets.UTF_8),
                    new String("Невозможно создать модели".getBytes(), StandardCharsets.UTF_8),
                    Messages.getWarningIcon());
            return new ArrayList<>();
        }

        return Arrays.asList(files);
    }

    /**
     * Get model definition as JSON files.
     * @return Json definitions
     */
    private List<EntityModel> getJsonData() {
        List<PsiFile> files = this.getPSIModelDefinitions();
        if (files.isEmpty())
            return new LinkedList<>();

        List<EntityModel> fileData = new LinkedList<>();
        AtomicReference<Throwable> throwFlag = new AtomicReference<>();
        files.forEach(f -> {
            try {
                String data = FileUtils.readFileToString(new File(f.getVirtualFile().getPath()), StandardCharsets.UTF_8);
                fileData.add(ServicePluginUtils.fillModel(EntityModel.class, data));
            } catch (IOException | JSONException ex) {
                throwFlag.set(ex);
            }

        });
        if (throwFlag.get() != null) {
            Messages.showMessageDialog(
                    new String(("Модель не создана.\n Сообщение: \n" + throwFlag.get().getMessage()).getBytes(), StandardCharsets.UTF_8),
                    new String("Ошибка чтения файла модели".getBytes(), StandardCharsets.UTF_8),
                    Messages.getErrorIcon());
            return new LinkedList<>();
        }
        return fileData;
    }
}
