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


    public OperatorFrame(DisplayFrame display){
        super("Robot Timer Operator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createWidgets();
        addWidgets();
    }

    private void createWidgets(){
        OperatorListener opListener = new OperatorListener(this);

        cbComps = new JComboBox<>();
        cbComps.setActionCommand("cbComps");
        cbComps.addActionListener(opListener);

        jlBots = new JList<>();
        jlBots.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jlBots.addListSelectionListener(opListener);
    }

    private void addWidgets(){

    }

}
