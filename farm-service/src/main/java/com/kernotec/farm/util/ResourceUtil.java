package com.kernotec.farm.util;

import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class ResourceUtil {

    public static String getAbsolutePath(String fileName) throws IOException {
        Resource resource = new FileSystemResource("/reports/" + fileName);

        if (!resource.exists()) {
            resource = new ClassPathResource(fileName);
        }

        return resource.getURL()
            .getPath();
    }
}
