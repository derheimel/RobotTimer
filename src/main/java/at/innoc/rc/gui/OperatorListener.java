package at.innoc.rc.gui;

import at.innoc.rc.db.Dao;

import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Aaron on 20.02.2016.
 */
public class OperatorListener implements ActionListener, ListSelectionListener{

    private Dao db;
    private OperatorFrame opFrame;

    public OperatorListener(OperatorFrame opFrame){
        this.opFrame = opFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
