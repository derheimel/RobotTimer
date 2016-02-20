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

    private JComboBox<Competition> cbComps;
    private JList<Bot> jlBots;
    private JToggleButton btnReady;
    private JButton btnInvalidate;
    private JButton btnConfirm;

    private JPanel pnlButtons;
    private Container cp = getContentPane();
    private OperatorListener opListener;


    public OperatorFrame(DisplayFrame display){
        super("Robot Timer Operator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.opListener = new OperatorListener(this, display);
        createWidgets();
        addWidgets();
        pack();
        setLocationRelativeTo(null);
    }

    private void createWidgets(){
        cbComps = new JComboBox<>();
        cbComps.setActionCommand("cbComps");
        cbComps.addActionListener(opListener);

        jlBots = new JList<>();
        jlBots.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jlBots.addListSelectionListener(opListener);

        btnReady = new JToggleButton("Ready");
        btnReady.setActionCommand("btnReady");
        btnReady.addActionListener(opListener);

        btnInvalidate = new JButton("Invalidate");
        btnInvalidate.setActionCommand("btnInvalidate");
        btnInvalidate.addActionListener(opListener);

        btnConfirm = new JButton("Confirm");
        btnConfirm.setActionCommand("btnConfirm");
        btnConfirm.addActionListener(opListener);

        opListener.initialize(cbComps);
    }

    private void addWidgets(){

    }
}
