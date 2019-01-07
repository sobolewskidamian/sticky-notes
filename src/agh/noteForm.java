package agh;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
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
    private JButton usunButton;
    private JButton zamknijButton;
    private JComboBox comboBox2;
    private JButton bButton;
    private JButton iButton;
    private JButton uButton;
    private JButton buttonHide2;
    private JButton buttonHide1;

    private Frame frame;
    private Point initialClick;

    private String text;
    private int color;
    private int fontSize = 4;
    private String path;
    private String fileName;
    private LinkedList<Note> notes = new LinkedList<>();
    private boolean ifHide = false;

    public noteForm(JFrame frame) {
        this.frame = frame;
        editorPane1.setContentType("text/html");
        buttonHide2.setVisible(false);

        zapiszButton.addActionListener(e -> saveTextAndSetting());

        dodajButton.addActionListener(e -> makeNewNote());

        usunButton.addActionListener(e -> {
            deleteThisNote();
            closeWindow(e);
        });

        zamknijButton.addActionListener(this::closeWindow);

        comboBox1.addActionListener(e -> {
            color = comboBox1.getSelectedIndex();
            setColor(color);
        });

        comboBox2.addActionListener(e -> {
            fontSize = Integer.parseInt((String) comboBox2.getSelectedItem());
            setFontSize(fontSize);
        });

        bButton.addActionListener(e -> addIndexToText('b'));

        iButton.addActionListener(e -> addIndexToText('i'));

        uButton.addActionListener(e -> addIndexToText('u'));

        buttonHide2.addActionListener(e -> actionHideOrShow());

        buttonHide1.addActionListener(e -> actionHideOrShow());

        panel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                frame.getComponentAt(initialClick);
            }
        });

        panel1.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = frame.getLocation().x;
                int thisY = frame.getLocation().y;
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                frame.setLocation(X, Y);
            }
        });

        editorPane1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        editorPane1.getDocument().insertString(editorPane1.getCaretPosition(), ">", null);
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void hideOrShow(boolean flag) {
        this.buttonHide2.setVisible(!flag);
        this.buttonHide1.setVisible(flag);
        this.dodajButton.setVisible(flag);
        this.usunButton.setVisible(flag);
        this.zamknijButton.setVisible(flag);
        this.bButton.setVisible(flag);
        this.uButton.setVisible(flag);
        this.iButton.setVisible(flag);
        this.comboBox1.setVisible(flag);
        this.comboBox2.setVisible(flag);
    }

    private void actionHideOrShow() {
        if (!this.ifHide)
            hideOrShow(false);
        else
            hideOrShow(true);
        this.ifHide = !this.ifHide;
    }

    public void setColor(int color) {
        switch (color) {
            case 1:
                this.editorPane1.setBackground(new Color(149, 179, 255));
                break;
            case 2:
                this.editorPane1.setBackground(new Color(255, 102, 102));
                break;
            case 3:
                this.editorPane1.setBackground(new Color(255, 152, 244));
                break;
            case 4:
                this.editorPane1.setBackground(new Color(180, 255, 146));
                break;
            default:
                this.editorPane1.setBackground(Color.WHITE);
                break;
        }
        this.comboBox1.setSelectedItem(nameOfColor(color));
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

    private void saveTextAndSetting() {
        this.text = this.editorPane1.getText();
        try {
            FileWriter file = new FileWriter(this.path + this.fileName, false);
            BufferedWriter out = new BufferedWriter(file);
            String updatedText = updateNewLines(this.text);
            out.write(updatedText);
            out.close();
            settings(this.color, this.fontSize, this.frame.getLocation().x, this.frame.getLocation().y, this.frame.getWidth(), this.frame.getHeight(), this.path, this.fileName);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void makeNewNote() {
        try {
            String fileName = "1.sn";
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(this.path), "*.{sn}");
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
            String actPath = this.path + "\\" + fileName;
            new File(actPath).createNewFile();
            Note note = new Note("", this.path, fileName, this.notes);
            this.notes.add(note);
            settings(0, 4, 0, 0, 490, 200, path, fileName);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void deleteThisNote() {
        new File(this.path + this.fileName).delete();
        new File(this.path + this.fileName.replaceFirst("[.][^.]+$", ".conf")).delete();
    }

    private void addIndexToText(char ch) {
        if (this.editorPane1.getSelectedText() != null) {
            String a = this.editorPane1.getSelectedText();
            String actText = this.editorPane1.getText();
            String actTextAfterMod = actText.replaceAll(a, "<" + ch + ">" + a + "</" + ch + ">");
            this.editorPane1.setText(actTextAfterMod);
        }
        updateNewLines(this.editorPane1.getText());
    }

    public void setFontSize(int fontSize) {
        String txt = this.editorPane1.getText();
        String newTxt;
        String txt1, txt2;
        txt1 = txt.replaceAll("<font size=\"[0-9]\">", "");
        txt2 = txt1.replaceAll("</font>", "");

        newTxt = txt2.replaceAll("<html>", "<font size=\"" + fontSize + "\">");
        this.editorPane1.setText(newTxt);
        this.comboBox2.setSelectedItem(Integer.toString(fontSize));
    }

    private void closeWindow(ActionEvent e) {
        JComponent comp = (JComponent) e.getSource();
        Window win = SwingUtilities.getWindowAncestor(comp);
        win.dispose();
    }

    private String updateNewLines(String text) {
        String updatedText = text.replaceAll("&gt;", "<br>"); // ">" - nowa linia
        this.editorPane1.setText(updatedText);
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
    }
}
