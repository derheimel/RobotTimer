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
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by Aaron on 20.02.2016.
 */
public class OperatorListener implements ActionListener, ListSelectionListener{

    private Dao db;
    private OperatorFrame opFrame;
    private DisplayFrame displayFrame;

    private static final Color COLOR_READY = new Color(80, 255, 50);
    private static final Color COLOR_PAUSE = new Color(255, 100, 100);
    private static final Color COLOR_RUNNING = new Color(255, 210, 0);
    private static final Color COLOR_FINISHED = new Color(0, 245, 255);
    private static final Color COLOR_INVALIDATED = COLOR_PAUSE;
    private static final Color COLOR_CONFIRMED = COLOR_READY;

    private static final String PAUSE = "PAUSE";
    private static final String READY = "READY";
    private static final String TRACK_BEST = "Track record: ";
    private static final String BOT_BEST = "Current bot record: ";

    public static final String DEFAULT_HEAD = "<html><font color=#4A83DE>ROBOT CHALLENGE 2016</font></html>";

    public OperatorListener(OperatorFrame opFrame, DisplayFrame displayFrame){
        this.opFrame = opFrame;
        this.displayFrame = displayFrame;
        this.db = new JDBCDao();
    }

    protected void initialize(JComboBox<Competition> cbComps){
        List<Competition> comps = db.getLineFollowerComps();
        for(Competition c : comps){
            cbComps.addItem(c);
        }

        opFrame.getLblStatus().setText(PAUSE);
        displayFrame.getLblStatus().setText(PAUSE);

        opFrame.getLblStatus().setBackground(COLOR_PAUSE);
        displayFrame.getLblStatus().setBackground(COLOR_PAUSE);

        displayFrame.getLblBotName().setText(DEFAULT_HEAD);
        displayFrame.getLblTrackBestTime().setText(TRACK_BEST);
        displayFrame.getLblBotBestTime().setText(BOT_BEST);
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

        JLabel opStatus = opFrame.getLblStatus();
        JLabel displayStatus = displayFrame.getLblStatus();

        if(btnReady.isSelected()){
            opStatus.setBackground(COLOR_READY);
            displayStatus.setBackground(COLOR_READY);

            opStatus.setText(READY);
            displayStatus.setText(READY);
        }
        else{
            opStatus.setBackground(COLOR_PAUSE);
            displayStatus.setBackground(COLOR_PAUSE);

            opStatus.setText(PAUSE);
            displayStatus.setText(PAUSE);
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
        if(e.getValueIsAdjusting()) return;
        onBotSelection(e.getSource());
    }

    private void onBotSelection(Object source){
        JList<Bot> jlBots = (JList<Bot>) source;
        Bot selectedBot = jlBots.getSelectedValue();

        if(selectedBot == null) return;

        BufferedImage flag = getFlag(selectedBot.getCountry());
        displayFrame.updateFlag(flag);

        JLabel lblTries = opFrame.getLblTries();
//        lblTries.setText(selectedBot.get);

        JLabel lblBotName = displayFrame.getLblBotName();
        JLabel lblTrackBestTime = displayFrame.getLblTrackBestTime();
        JLabel lblBotBestTime = displayFrame.getLblBotBestTime();
        JLabel lblCountryShort = displayFrame.getLblCountryShort();

        if(selectedBot.getUid() == -1){
            lblBotName.setText(DEFAULT_HEAD);
            lblTrackBestTime.setText(TRACK_BEST);
            lblBotBestTime.setText(BOT_BEST);
            lblCountryShort.setText("??");
        }
        else{
            lblBotName.setText(selectedBot.getName());
            //lblTrackBestTime.setText()
            //lblBotBestTime.setText()
            lblCountryShort.setText(selectedBot.getCountry());
        }




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
