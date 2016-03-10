package at.innoc.rc.gui;

import at.innoc.rc.db.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Aaron on 20.02.2016.
 */
public class OperatorListener extends MouseAdapter implements ActionListener, ListSelectionListener{

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
    private static final String TRACK_BEST_NORMAL = "Track record (normal): ";
    private static final String TRACK_BEST_ENHANCED = "Track record (enhanced): ";
    private static final String TRACK_BEST_LEGO = "Track record (lego): ";
    private static final String BOT_BEST_NORMAL = "Bot record (normal): ";
    private static final String BOT_BEST_ENHANCED = "Bot record (enhanced): ";
    private static final String BOT_BEST_LEGO = "Bot record (lego): ";

    private String trackLabel = TRACK_BEST_NORMAL;
    private String botLabel = BOT_BEST_NORMAL;
    private String mode = "";

    private volatile boolean running;
    private volatile boolean timerTerminated;
    private volatile long passed;

    public static final String DEFAULT_HEAD = "<html><font color=#4A83DE>ROBOT CHALLENGE " + Calendar.getInstance().get(Calendar.YEAR) + "</font></html>";

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
        displayFrame.getLblTrackBestTime().setText(TRACK_BEST_NORMAL);
        displayFrame.getLblBotBestTime().setText(BOT_BEST_NORMAL);
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

    private String toTimeString(int hundrethSeconds){
        return toTimeString((long)(hundrethSeconds * 10));
    }

    private String toTimeString(long milliSeconds){
        if(milliSeconds == 0) return "--";
        String timeString = "%02d.%02d:%02d";
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - minutes * 60;
        long hundredthSeconds = milliSeconds / 10 - seconds * 100 - minutes * 100 * 60;
        return String.format(timeString, minutes, seconds, hundredthSeconds);
    }

    private class TimerThread implements Runnable{

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            timerTerminated = false;

            while(running){
                long current = System.currentTimeMillis();
                passed = current - start;

                String status = toTimeString(passed);

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
        JButton btnStop = opFrame.getBtnStop();
        JRadioButton btnConfirm = opFrame.getBtnConfirm();
        JRadioButton btnAbort = opFrame.getBtnAbort();
        JRadioButton btnInvalidate = opFrame.getBtnInvalidate();
        JList<Bot> jlBots = opFrame.getJlBots();

        boolean isSelected = !jlBots.isSelectionEmpty() && jlBots.getSelectedValue().getUid() != -1;

        if(running){
            stopTimer();
            if(isSelected) btnConfirm.setEnabled(true);
            if(btnStop.isEnabled()){
                btnStop.setEnabled(false);
                blink(false);
            }
        }
        else if(opFrame.ready()){
            running = true;
            setStatusBackground(COLOR_RUNNING);
            btnReady.setEnabled(false);
            if(isSelected) btnInvalidate.setEnabled(true);
            btnAbort.setEnabled(true);
            new Thread(new TimerThread()).start();
        }
    }

    private void stopTimer(){
        running = false;
        setStatusBackground(COLOR_FINISHED);
        while(!timerTerminated){
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "cbComps": onCompetitionSelection(e.getSource()); break;
            case "btnReady": onReady(); break;
            case "btnAbort": onAbortSelection(); break;
            case "btnInvalidate": onInvalidateSelection(); break;
            case "btnConfirm": onConfirmSelection(); break;
            case "btnOK": onOK(); break;
            case "btnStop": onStop(); break;
        }
    }

    private void onStop(){
        stopTimer();
        JToggleButton btnReady = opFrame.getBtnReady();
        btnReady.setEnabled(true);
        btnReady.doClick();
        opFrame.getJlBots().setEnabled(true);
        opFrame.getCbComps().setEnabled(true);
        opFrame.getBtnStop().setEnabled(false);
    }

    private class BlinkThread implements Runnable{

        boolean confirmed;

        BlinkThread(boolean confirmed){
            this.confirmed = confirmed;
        }

        @Override
        public void run() {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    opFrame.getBtnInvalidate().setEnabled(false);
                    opFrame.getBtnConfirm().setEnabled(false);

                    opFrame.getBtnInvalidate().setEnabled(false);
                    opFrame.getBtnConfirm().setEnabled(false);
                }
            });

            final Color color = confirmed ? COLOR_CONFIRMED :  COLOR_INVALIDATED;

            try {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setStatusBackground(color);
                    }
                });
                Thread.sleep(500);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setStatusBackground(COLOR_FINISHED);
                    }
                });
                Thread.sleep(500);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setStatusBackground(color);
                    }
                });
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    opFrame.getBtnReady().setEnabled(true);
                    opFrame.getBtnReady().doClick();
                    opFrame.getJlBots().setEnabled(true);
                    opFrame.getCbComps().setEnabled(true);
                }
            });
        }
    }

    private void onOK(){
        JButton btnOK = opFrame.getBtnOK();
        btnOK.setEnabled(false);
        JRadioButton btnAbort = opFrame.getBtnAbort();
        JRadioButton btnInvalidate = opFrame.getBtnInvalidate();
        JRadioButton btnConfirm = opFrame.getBtnConfirm();

        JButton btnStop = opFrame.getBtnStop();

        boolean abort = btnAbort.isSelected();
        btnAbort.setEnabled(false);

        if(abort){
            onStop();
            btnInvalidate.setEnabled(false);
            btnConfirm.setEnabled(false);
        }
        else if(btnInvalidate.isSelected()){
            if(running){
                btnInvalidate.setEnabled(false);
                btnStop.setEnabled(true);
                setStatusBackground(COLOR_INVALIDATED);
            }
            else {
                blink(false);
            }
            saveResult(false);
            onBotSelection(false);
        }
        else if(btnConfirm.isSelected()){
            stopTimer();

            saveResult(true);
            onBotSelection(false);
            updateTrackRecord();
            blink(true);
        }

        opFrame.getBg().clearSelection();
    }

    private void blink(boolean confirmed){
        new Thread(new BlinkThread(confirmed)).start();
    }

    private void saveResult(boolean confirmed){
        Bot selectedBot = opFrame.getJlBots().getSelectedValue();
        Competition selectedComp = (Competition)opFrame.getCbComps().getSelectedItem();

        int tries = Integer.parseInt(opFrame.getLblTries().getText()) + 1;
        Result result = new Result(5, selectedBot, selectedComp, tries);
        if(confirmed) result.setTime((int) (passed / 10));
        db.saveResult(result);
    }

    private void onAbortSelection(){
        opFrame.getBtnOK().setEnabled(true);
        opFrame.getLblOK().setText("ABORT");
        opFrame.getBtnStop().setEnabled(false);
    }

    private void onInvalidateSelection(){
        opFrame.getBtnOK().setEnabled(true);
        opFrame.getLblOK().setText("INVALIDATE");
    }

    private void onConfirmSelection(){
        opFrame.getBtnOK().setEnabled(true);
        opFrame.getLblOK().setText("CONFIRM");
        opFrame.getBtnStop().setEnabled(false);
    }

    private void onReady(){
        JToggleButton btnReady = opFrame.getBtnReady();
        JList<Bot> jlBots = opFrame.getJlBots();
        JComboBox<Competition> cbComps = opFrame.getCbComps();

        if(btnReady.isSelected()){
            setStatus(READY, COLOR_READY);
            jlBots.setEnabled(false);
            cbComps.setEnabled(false);
        }
        else{
            setStatus(PAUSE, COLOR_PAUSE);
            jlBots.setEnabled(true);
            cbComps.setEnabled(true);
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

        String compNameLower = selectedComp.getName().toLowerCase();

        mode = "normal";
        botLabel = BOT_BEST_NORMAL;
        trackLabel = TRACK_BEST_NORMAL;

        if(compNameLower.contains("lego")){
            mode = "lego";
            trackLabel = TRACK_BEST_LEGO;
            botLabel = BOT_BEST_LEGO;
        }
        else if(compNameLower.contains("enhanced")){
            mode = "enhanced";
            trackLabel = TRACK_BEST_ENHANCED;
            botLabel = BOT_BEST_ENHANCED;
        }

        updateTrackRecord();
        displayFrame.getLblBotBestTime().setText(botLabel);
        displayFrame.getLblBotName().setText(DEFAULT_HEAD);
    }

    private void updateTrackRecord(){
        JComboBox<Competition> cbComps = opFrame.getCbComps();
        Competition selectedComp = (Competition) cbComps.getSelectedItem();
        if(selectedComp.getUid() == -1) return;
        int bestTrackTime = db.getBestTimeByMode(mode);
        String bestTimeText = "<html>" + trackLabel + "<font color=#006400>["
                + toTimeString(bestTrackTime) + "]</font></html>";
        displayFrame.getLblTrackBestTime().setText(bestTimeText);
    }

    private void updateBotRecord(){
        JList<Bot> jlBots = opFrame.getJlBots();
        if(jlBots.isSelectionEmpty()) return;
        int bestBotTime = db.getBestTimeByMode(mode, jlBots.getSelectedValue());
        String bestTimeText = "<html>" + botLabel +
                "<font color=#006400>[" + toTimeString(bestBotTime) + "]</font></html>";
        displayFrame.getLblBotBestTime().setText(bestTimeText);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) return;
        onBotSelection(true);
    }

    private void onBotSelection(boolean real){
        JList<Bot> jlBots = opFrame.getJlBots();
        Bot selectedBot = jlBots.getSelectedValue();

        JComboBox<Competition> cbComps = opFrame.getCbComps();
        Competition selectedComp = (Competition) cbComps.getSelectedItem();

        if(selectedBot == null) return;

        BufferedImage flag = getFlag(selectedBot.getCountry());
        displayFrame.updateFlag(flag);

        JLabel lblTries = opFrame.getLblTries();

        JLabel lblBotName = displayFrame.getLblBotName();
        JLabel lblBotBestTime = displayFrame.getLblBotBestTime();
        JLabel lblCountryShort = displayFrame.getLblCountryShort();

        if(selectedBot.getUid() == -1){
            lblTries.setText("-");
            lblBotName.setText(DEFAULT_HEAD);
            lblBotBestTime.setText(botLabel);
            lblCountryShort.setText("");

        }
        else{
            int tries = db.getTries(mode, selectedBot);
            lblTries.setText("" + tries);
            lblBotName.setText(selectedBot.getName());
            updateBotRecord();
            lblCountryShort.setText(selectedBot.getCountry());
            if(real) {
                Result result = new Result(6, selectedBot, selectedComp, tries);
                db.saveResult(result);
            }
        }




        //TODO: stuff
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if(!running) return;
        opFrame.getBg().clearSelection();
    }

    private BufferedImage getFlag(String country){
        BufferedImage flag = null;
        if(country == null) return null;
        try {
            flag = ImageIO.read(getClass().getClassLoader().getResourceAsStream("at/innoc/rc/gfx/" + country.toLowerCase() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch(IllegalArgumentException e){
            try {
                flag = ImageIO.read(getClass().getClassLoader().getResourceAsStream("at/innoc/rc/gfx/noflag.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return flag;
    }
}
