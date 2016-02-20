package at.innoc.rc.gui;

import at.innoc.rc.db.Bot;
import at.innoc.rc.db.Competition;
import at.innoc.rc.db.Dao;

import javax.swing.*;
import java.awt.*;

/**
 * The referee uses this frame to control the program
 */
public class OperatorFrame extends JFrame{

    private DisplayFrame diplay;

    private JComboBox<Competition> cbComps;
    private JList<Bot> jlBots;
    private JToggleButton btnReady;
    private JButton btnInvalidate;
    private JButton btnConfirm;

    private JPanel pnlButtons;
    private Container cp = getContentPane();

    private Dao db;


    public OperatorFrame(DisplayFrame display, Dao db){
        super("Robot Timer Operator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.db = db;
        createWidgets();
        addWidgets();
    }

    private void createWidgets(){
        cbComps = new JComboBox<>(db.getLineFollowerComps());
    }

    private void addWidgets(){

    }

}
