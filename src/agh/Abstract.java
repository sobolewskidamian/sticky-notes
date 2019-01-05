package agh;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Abstract {
    public void settings(int backgroundColor, int fontSize, String path, String fileName) throws IOException {
        String fileNameWithOutExt = fileName.replaceFirst("[.][^.]+$", "");
        String confPath = path + fileNameWithOutExt + ".conf";
        FileWriter filewritter = new FileWriter(confPath, false);
        BufferedWriter out = new BufferedWriter(filewritter);
        out.write(backgroundColor + System.lineSeparator() + fontSize);
        out.close();
    }
}
