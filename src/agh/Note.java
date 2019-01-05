package agh;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Note {
    public Note(String text, String path, String filename, LinkedList<Note> notes) throws FileNotFoundException {
        JFrame frame = new JFrame("noteForm");
        noteForm panel = new noteForm(frame);
        panel.setEditorPane1(text);

        panel.setListNotes(notes);
        panel.setPath(path);
        panel.setFileName(filename);

        File file = new File(path + filename.replaceFirst("[.][^.]+$", ".conf"));
        if (file.exists()) {
            Scanner scan = new Scanner(file);
            int i = 0;
            while (scan.hasNext()) {
                String line = scan.nextLine();
                if (!line.equals("")) {
                    if (i == 0)
                        panel.setColor(Integer.parseInt(line));
                    else if (i == 1) {
                        panel.setFontSize(Integer.parseInt(line));
                    }
                    else if (i == 2) {
                        panel.setLocationX(Integer.parseInt(line));
                    }
                    else if (i == 3) {
                        panel.setLocationY(Integer.parseInt(line));
                    }
                    else if (i == 4) {
                        String[] tab = line.split(",");
                        if(tab.length==2)
                            panel.setWidthAndHeight(Integer.parseInt(tab[0]), Integer.parseInt(tab[1]));
                    }
                }
                i++;
            }
            scan.close();
        } else {
            panel.setColor(0);
            panel.setFontSize(4);
            panel.setLocationX(0);
            panel.setLocationY(0);
            panel.setWidthAndHeight(490,200);
        }
        //frame.setUndecorated(false);
        frame.setContentPane(panel.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }
}
