package agh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public abstract class Abstract {
    protected void settings(int backgroundColor, int fontSize, int locationX, int locationY, int width, int height, String path, String fileName) throws IOException {
        String fileNameWithOutExt = fileName.replaceFirst("[.][^.]+$", "");
        String confPath = path + fileNameWithOutExt + ".conf";
        FileWriter filewritter = new FileWriter(confPath, false);
        BufferedWriter out = new BufferedWriter(filewritter);
        out.write(backgroundColor + System.lineSeparator() + fontSize + System.lineSeparator() + locationX + System.lineSeparator() + locationY + System.lineSeparator() + width + "," + height);
        out.close();
    }

    protected String updateNewLines(String text, JEditorPane editorPane1) {
        String updatedText = text.replaceAll("&gt;", "<br>"); // ">" - nowa linia
        editorPane1.setText(updatedText);
        return updatedText;
    }

    protected String nameOfColor(int color) {
        switch (color) {
            case 1:
                return "niebieski";
            case 2:
                return "czerwony";
            case 3:
                return "różowy";
            case 4:
                return "zielony";
            default:
                return "biały";
        }
    }

    protected void setColor(int color, JEditorPane editorPane1, JComboBox comboBox1) {
        switch (color) {
            case 1:
                editorPane1.setBackground(new Color(149, 179, 255));
                break;
            case 2:
                editorPane1.setBackground(new Color(255, 102, 102));
                break;
            case 3:
                editorPane1.setBackground(new Color(255, 152, 244));
                break;
            case 4:
                editorPane1.setBackground(new Color(180, 255, 146));
                break;
            default:
                editorPane1.setBackground(Color.WHITE);
                break;
        }
        comboBox1.setSelectedItem(nameOfColor(color));
    }

    protected void makeNewNote(String path) {
        try {
            String fileName = "1.sn";
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path), "*.{sn}");
            LinkedList<Integer> fileNumbers = new LinkedList<>();
            for (Path it : stream) {
                String fileNameWithOutExt = it.getFileName().toString().replaceFirst("[.][^.]+$", "");
                Integer numberOfFile = Integer.parseInt(fileNameWithOutExt);
                fileNumbers.add(numberOfFile);
            }
            for (int i = 1; i <= fileNumbers.size() + 1; i++) {
                if (!fileNumbers.contains(i))
                    fileName = i + ".sn";
            }
            String actPath = path + "\\" + fileName;
            new File(actPath).createNewFile();
            Note note = new Note("", path, fileName);
            settings(0, 4, 0, 0, 490, 200, path, fileName);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    protected void deleteThisNote(String path, String fileName) {
        new File(path + fileName).delete();
        new File(path + fileName.replaceFirst("[.][^.]+$", ".conf")).delete();
    }

    protected void closeWindow(ActionEvent e) {
        JComponent comp = (JComponent) e.getSource();
        Window win = SwingUtilities.getWindowAncestor(comp);
        win.dispose();
    }

    protected void addIndexToText(char ch, JEditorPane editorPane1) {
        if (editorPane1.getSelectedText() != null) {
            String a = editorPane1.getSelectedText();
            String actText = editorPane1.getText();
            String actTextAfterMod = actText.replaceAll(a, "<" + ch + ">" + a + "</" + ch + ">");
            editorPane1.setText(actTextAfterMod);
        }
        updateNewLines(editorPane1.getText(), editorPane1);
    }

    protected void setFontSize(int fontSize, JEditorPane editorPane1, JComboBox comboBox2) {
        String txt = editorPane1.getText();
        String newTxt;
        String txt1, txt2;
        txt1 = txt.replaceAll("<font size=\"[0-9]\">", "");
        txt2 = txt1.replaceAll("</font>", "");

        newTxt = txt2.replaceAll("<html>", "<font size=\"" + fontSize + "\">");
        editorPane1.setText(newTxt);
        comboBox2.setSelectedItem(Integer.toString(fontSize));
    }

    protected void saveTextAndSetting(String path, String fileName, String text, JEditorPane editorPane1, int color, int fontSize, Frame frame) {
        try {
            FileWriter file = new FileWriter(path + fileName, false);
            BufferedWriter out = new BufferedWriter(file);
            String updatedText = updateNewLines(text, editorPane1);
            out.write(updatedText);
            out.close();
            settings(color, fontSize, frame.getLocation().x, frame.getLocation().y, frame.getWidth(), frame.getHeight(), path, fileName);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
