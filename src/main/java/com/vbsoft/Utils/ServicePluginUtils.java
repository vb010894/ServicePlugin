package com.vbsoft.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.intellij.ide.util.DirectoryUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.util.ClassUtil;
import com.jgoodies.common.base.Strings;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * Plugin utils.
 *
 * @author vd.zinovev
 * @version 1.0
 * @since 1.0
 */
public final class ServicePluginUtils {

    /**
     * Hidden class u
     */
    private ServicePluginUtils() {
        // Do nothing
    }

    public static PsiDirectory createDirectory(PsiDirectory parent, String name) {
        return DirectoryUtil.createSubdirectories(name, parent, ".");
    }

    public static PsiDirectory findDirectory(PsiDirectory parent, String path) {
        if (Strings.isBlank(path))
            throw new IllegalArgumentException("Путь поиска дочерних директорий должен разделяться '/' и не должен быть пустым");

        AtomicReference<PsiDirectory> atomicDir = new AtomicReference<>();
        atomicDir.set(Objects.requireNonNull(parent));

        String[] paths = (path.contains("/")) ? path.split("/") : new String[]{path};

        Stream.of(paths).forEach(p -> {
            PsiDirectory temp;
            if ((temp = atomicDir.get().findSubdirectory(p)) == null) {
                return;
            }
            atomicDir.set(temp);
        });
        return Objects.requireNonNull(atomicDir.get());
    }


    public static PsiDirectory createPackages(PsiDirectory parent, String path) {
        if (Strings.isBlank(path))
            throw new IllegalArgumentException("Путь поиска дочерних директорий должен разделяться '.' и не должен быть пустым");

        AtomicReference<PsiDirectory> atomicDir = new AtomicReference<>();
        atomicDir.set(Objects.requireNonNull(parent));

        String[] paths = (path.contains(".")) ? path.split("\\.") : new String[]{path};

        Stream.of(paths).forEach(p -> {
            PsiDirectory temp;
            if ((temp = atomicDir.get().findSubdirectory(p)) == null) {
                temp = createDirectory(atomicDir.get(), p);
            }
            atomicDir.set(temp);
        });
        return Objects.requireNonNull(atomicDir.get());
    }

    public static <T> T fillModel(Class<T> modelClass, String JSON) throws JsonProcessingException {
        JsonMapper mapper = new JsonMapper();
        return mapper.readValue(JSON, modelClass);
    }

}
