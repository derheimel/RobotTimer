package at.innoc.rc.gui;

import at.innoc.rc.db.Bot;
import at.innoc.rc.db.Competition;
import at.innoc.rc.db.Dao;
import at.innoc.rc.db.JDBCDao;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Aaron on 20.02.2016.
 */
public class OperatorListener implements ActionListener, ListSelectionListener{

    private Dao db = new JDBCDao();
    private OperatorFrame opFrame;
    private DisplayFrame displayFrame;

    public OperatorListener(OperatorFrame opFrame, DisplayFrame displayFrame){
        this.opFrame = opFrame;
        this.displayFrame = displayFrame;
    }

    protected void initialize(JComboBox<Competition> cbComps){
        List<Competition> comps = db.getLineFollowerComps();
        for(Competition c : comps){
            cbComps.addItem(c);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "cbComps": onCompetitionSelection(e.getSource()); break;
        }
    }

    private void onCompetitionSelection(Object source){
        JComboBox<Competition> cbComps = (JComboBox<Competition>) source;
        Competition selectedComp = (Competition) cbComps.getSelectedItem();
        List<Bot> botList = db.getBotsByCompetition(selectedComp);
        Bot[] bots = new Bot[botList.size()];
        botList.toArray(bots);
        JList<Bot> jlBots = opFrame.getJlBots();

        jlBots.setListData(bots);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
