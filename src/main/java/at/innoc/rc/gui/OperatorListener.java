package at.innoc.rc.gui;

import at.innoc.rc.RobotTimer;
import at.innoc.rc.db.Bot;
import at.innoc.rc.db.Competition;
import at.innoc.rc.db.Dao;
import at.innoc.rc.db.JDBCDao;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
            case "btnReady": onReady(e.getSource()); break;
        }
    }

    private void onReady(Object source){
        JToggleButton btnReady = (JToggleButton) source;

        if(btnReady.isSelected()){
            btnReady.setBackground(null);
            //TODO: stuff
        }
        else{
            btnReady.setBackground(Color.GREEN);
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
        onBotSelection(e.getSource());
    }

    private void onBotSelection(Object source){
        JList<Bot> jlBots = (JList<Bot>) source;
        Bot selectedBot = jlBots.getSelectedValue();

        BufferedImage flag = getFlag(selectedBot.getCountry());
        displayFrame.updateFlag(flag);

        JLabel lblTries = opFrame.getLblTries();
//        lblTries.setText(selectedBot.get);
        //TODO: stuff
    }

    private BufferedImage getFlag(String country){
        BufferedImage flag = null;
        try {
            String str = getClass().getClassLoader().getResource("").getPath();
            flag = ImageIO.read(getClass().getClassLoader().getResourceAsStream("at/innoc/rc/gfx/" + country.toLowerCase() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
