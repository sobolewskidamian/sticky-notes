package agh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class noteForm extends Abstract {
    private JPanel panel1;
    private JButton zapiszButton;
    private JEditorPane editorPane1;
    private JComboBox comboBox1;
    private JButton dodajButton;
    private JButton usuńButton;
    private JButton zamknijButton;
    private JComboBox comboBox2;
    private JButton bButton;
    private JButton iButton;
    private JButton uButton;

    private Frame frame;

    private String text;
    private int color;
    private int fontSize = 4;
    private String path;
    private String fileName;
    private LinkedList<Note> notes = new LinkedList<>();

    public noteForm(JFrame frame) {
        this.frame = frame;
        editorPane1.setContentType("text/html");
        zapiszButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text = editorPane1.getText();
                try {
                    FileWriter file = new FileWriter(path + fileName, false);
                    BufferedWriter out = new BufferedWriter(file);
                    String updatedText = updateNewLines(text);
                    out.write(updatedText);
                    out.close();
                    settings(color, fontSize, frame.getLocation().x, frame.getLocation().y, frame.getWidth(), frame.getHeight(), path, fileName);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = "1.sn";
                DirectoryStream<Path> stream = null;
                try {
                    stream = Files.newDirectoryStream(Paths.get(path), "*.{sn}");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
                try {
                    new File(actPath).createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Note note = null;
                try {
                    note = new Note("", path, fileName, notes);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                notes.add(note);
                try {
                    settings(0, 4, 0, 0, 490,200, path, fileName);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        usuńButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new File(path + fileName).delete();
                new File(path + fileName.replaceFirst("[.][^.]+$", ".conf")).delete();
                closeWindow(e);
            }
        });
        zamknijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow(e);
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = comboBox1.getSelectedIndex();
                setColor(color);
            }
        });

        comboBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fontSize = Integer.parseInt((String) comboBox2.getSelectedItem());
                setFontSize(fontSize);
            }
        });
        bButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addIndexToText('b');
            }
        });
        iButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addIndexToText('i');
            }
        });
        uButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addIndexToText('u');
            }
        });
    }

    public void setColor(int color) {
        switch (color) {
            case 1:
                editorPane1.setBackground(Color.BLUE);
                break;
            case 2:
                editorPane1.setBackground(Color.RED);
                break;
            case 3:
                editorPane1.setBackground(Color.PINK);
                break;
            case 4:
                editorPane1.setBackground(new Color(12, 255, 92));
                break;
            default:
                editorPane1.setBackground(Color.WHITE);
                break;
        }
        comboBox1.setSelectedItem(nameOfColor(color));
    }

    private String nameOfColor(int color) {
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

    private void addIndexToText(char ch) {
        if (editorPane1.getSelectedText()!=null) {
            String a = editorPane1.getSelectedText();
            System.out.println(a);
            String actText = editorPane1.getText();
            String actTextAfterMod = actText.replaceAll(a, "<" + ch + ">" + a + "</" + ch + ">");
            editorPane1.setText(actTextAfterMod);
        }
        updateNewLines(editorPane1.getText());
    }

    public void setFontSize(int fontSize) {
        String txt = editorPane1.getText();
        String newTxt;
        if (txt.contains("<font size="))
            newTxt = txt.replaceAll("<font size=\"[0-9]\">", "<font size=\"" + fontSize + "\">");
        else
            newTxt = txt.replaceAll("<html>", "<font size=\"" + fontSize + "\">");
        editorPane1.setText(newTxt);
        comboBox2.setSelectedItem(Integer.toString(fontSize));
    }

    private void closeWindow(ActionEvent e) {
        JComponent comp = (JComponent) e.getSource();
        Window win = SwingUtilities.getWindowAncestor(comp);
        win.dispose();
    }

    private String updateNewLines(String text){
        String updatedText = text.replaceAll("&gt;", "<br>"); // ">" - nowa linia
        editorPane1.setText(updatedText);
        return updatedText;
    }

    public void setEditorPane1(String text) {
        this.editorPane1.setText(text);
    }

    public JPanel getPanel() {
        return this.panel1;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFileName(String path) {
        this.fileName = path;
    }

    public void setListNotes(LinkedList<Note> notes) {
        this.notes = notes;
    }

    public void setLocationX(int locationX) {
        this.frame.setLocation(locationX, this.frame.getLocation().y);
    }

    public void setLocationY(int locationY) {
        this.frame.setLocation(this.frame.getLocation().x, locationY);
    }

    public void setWidthAndHeight(int width, int height) {
        this.frame.setPreferredSize(new Dimension(width, height));
        System.out.println(this.frame.getHeight());
    }
}
