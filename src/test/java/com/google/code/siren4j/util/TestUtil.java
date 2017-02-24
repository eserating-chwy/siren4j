package com.google.code.siren4j.util;


import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class TestUtil {

    public static final String loadResource(String name) {
        try {
            return Files.toString(new File(TestUtil.class.getResource(name).getFile()), Charsets.UTF_8);
        } catch (IOException e) {
            Throwables.propagate(e);
            return null;
        }
    }
}
