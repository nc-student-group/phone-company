package com.phonecompany.util;

import java.io.File;

public class FileUtil {

    public static File[] getFilesWithExtensionFromPath(String extension, String path) {
        File targetDirectory = new File(path);
        return targetDirectory.listFiles(
                (dir, filename) -> filename.endsWith("." + extension));
    }
}
