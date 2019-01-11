package agh;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;

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
    private boolean ifHide = false;

    public noteForm(JFrame frame) {
        this.frame = frame;
        editorPane1.setContentType("text/html");
        buttonHide2.setVisible(false);

        zapiszButton.addActionListener(e -> {
            super.setFontSize(fontSize, editorPane1, comboBox2);
            this.text = this.editorPane1.getText();
            super.saveTextAndSetting(this.path, this.fileName, this.text, this.editorPane1, this.color, this.fontSize, this.frame);
        });

        dodajButton.addActionListener(e -> super.makeNewNote(this.path));

        usunButton.addActionListener(e -> {
            super.deleteThisNote(this.path, this.fileName);
            super.closeWindow(e);
        });

        zamknijButton.addActionListener(super::closeWindow);

        comboBox1.addActionListener(e -> {
            this.color = this.comboBox1.getSelectedIndex();
            super.setColor(this.color, this.editorPane1, this.comboBox1);
        });

        comboBox2.addActionListener(e -> {
            this.fontSize = Integer.parseInt((String) this.comboBox2.getSelectedItem());
            super.setFontSize(this.fontSize, this.editorPane1, this.comboBox2);
        });

        bButton.addActionListener(e -> super.addIndexToText('b', this.editorPane1));

        iButton.addActionListener(e -> super.addIndexToText('i', this.editorPane1));

        uButton.addActionListener(e -> super.addIndexToText('u', this.editorPane1));

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

    public void setFontSize(int fontSize) {
        super.setFontSize(fontSize, this.editorPane1, this.comboBox2);
    }

    public void setColor(int color) {
        super.setColor(color, this.editorPane1, this.comboBox1);
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