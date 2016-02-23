package at.innoc.rc.gui;

import at.innoc.rc.db.Bot;
import at.innoc.rc.db.Competition;
import at.innoc.rc.db.Dao;
import at.innoc.rc.db.JDBCDao;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private volatile boolean running;
    private volatile boolean timerTerminated;
    private volatile long passed;

    public static final String DEFAULT_HEAD = "<html><font color=#4A83DE>ROBOT CHALLENGE 2016</font></html>";

    public OperatorListener(DisplayFrame displayFrame, OperatorFrame opFrame){
        this.displayFrame = displayFrame;
        this.opFrame = opFrame;
        this.db = new JDBCDao();
    }

    protected void initialize(JComboBox<Competition> cbComps){
        List<Competition> comps = db.getLineFollowerComps();
        for(Competition c : comps){
            cbComps.addItem(c);
        }

        setStatus(PAUSE, COLOR_PAUSE);

        displayFrame.getLblBotName().setText(DEFAULT_HEAD);
        displayFrame.getLblTrackBestTime().setText(TRACK_BEST);
        displayFrame.getLblBotBestTime().setText(BOT_BEST);
    }

    private void setStatusText(String status){
        JLabel opStatus = opFrame.getLblStatus();
        JLabel displayStatus = displayFrame.getLblStatus();

        opStatus.setText(status);
        displayStatus.setText(status);
    }

    private void setStatusBackground(Color bg){
        JLabel opStatus = opFrame.getLblStatus();
        JLabel displayStatus = displayFrame.getLblStatus();

        opStatus.setBackground(bg);
        displayStatus.setBackground(bg);
    }

    private void setStatus(String status, Color bg){
        setStatusText(status);
        setStatusBackground(bg);
    }

    private class TimerThread implements Runnable{

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            timerTerminated = false;

            while(running){
                long current = System.currentTimeMillis();
                passed = current - start;

                String status = "%02d.%02d:%02d";
                long minutes = TimeUnit.MILLISECONDS.toMinutes(passed);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(passed) - minutes * 60;
                long hundredthSeconds = passed / 10 - seconds * 100 - minutes * 100 * 60;
                status = String.format(status, minutes, seconds, hundredthSeconds);

                setStatusText(status);

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            timerTerminated = true;
        }
    }

    /**
     * Called when something passes through the light barrier
     */
    public void onTrigger(){
        JToggleButton btnReady = opFrame.getBtnReady();
        JRadioButton btnConfirm = opFrame.getBtnConfirm();
        JRadioButton btnAbort = opFrame.getBtnAbort();
        JRadioButton btnInvalidate = opFrame.getBtnInvalidate();

        if(running){
            running = false;
            setStatusBackground(COLOR_FINISHED);
            btnConfirm.setEnabled(true);
            while(!timerTerminated){
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //TODO: stuff
        }
        else if(opFrame.ready()){
            running = true;
            setStatusBackground(COLOR_RUNNING);
            btnReady.setEnabled(false);
            btnInvalidate.setEnabled(true);
            btnAbort.setEnabled(true);
            new Thread(new TimerThread()).start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "cbComps": onCompetitionSelection(e.getSource()); break;
            case "btnReady": onReady(e.getSource()); break;
            case "btnConfirm": onConfirm(e.getSource()); break;
            case "btnAbort": onAbort(e.getSource()); break;
            case "btnInvalidate": onInvalidate(e.getSource()); break;
        }
    }

    private void onAbort(Object source){
        //TODO: asdf
    }

    private void onInvalidate(Object source){
        //TODO: sadf
    }

    private void onConfirm(Object source){
        JButton btnConfirm = (JButton) source;

        //TODO: sdfa
    }

    private void onReady(Object source){
        JToggleButton btnReady = (JToggleButton) source;

        if(btnReady.isSelected()){
            setStatus(READY, COLOR_READY);
        }
        else{
            setStatus(PAUSE, COLOR_PAUSE);
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
            lblCountryShort.setText("");
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
