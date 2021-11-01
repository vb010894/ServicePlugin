package com.vbsoft.menu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.vbsoft.Utils.ServicePluginUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Structure creation action class.
 * @author vd.zinovev
 * @since 1.0
 * @version 1.0
 */
public class CreateStructure extends AnAction {

    /**
     * Create structure.
     *
     * @param e Carries information on the invocation place
     */
    @SuppressWarnings("deprecation")
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiDirectory root = PsiDirectoryFactory.getInstance(Objects.requireNonNull(e.getProject())).createDirectory(e.getProject().getBaseDir());
        PsiDirectory target = ServicePluginUtils.findDirectory(root, "src/main/java");
        Properties structureMap = new Properties();
        try {
            structureMap.load(this.getClass().getResourceAsStream("/Properties/Structure.properties"));
        } catch (IOException ex) {
            Messages.showMessageDialog(
                    new String(("Структура не создана.\n Сообщение: \n" + ex.getMessage()).getBytes(), StandardCharsets.UTF_8),
                    new String("Создвние структуры".getBytes(), StandardCharsets.UTF_8),
                    Messages.getErrorIcon());
            return;
        }

        structureMap.forEach((key, value) -> ServicePluginUtils.createPackages(target, (String) value));
        PsiDirectory classDir = ServicePluginUtils.findDirectory(target, "org/severstal/infocom/exceptions");

        JavaDirectoryService.getInstance().createClass(classDir, "Test","TestClassTemplate.java", false,  Map.of("TYPE", "Integer",
                "VARNAME", "t"));
        Messages.showMessageDialog(
                new String("Структура успешно создана".getBytes(), StandardCharsets.UTF_8),
                new String("Создвние структуры".getBytes(),StandardCharsets.UTF_8),
                Messages.getInformationIcon());
    }
}
