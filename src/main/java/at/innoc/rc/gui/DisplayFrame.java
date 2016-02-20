package at.innoc.rc.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Displays the timer - is shown to the audience
 */
public class DisplayFrame extends JFrame{

    private JPanel pnlBottom;
    private JPanel pnlInfo;
    private JPanel pnlFlag;

    private JLabel lblBotName;
    private JLabel lblStatus; //The status (READY, PAUSE...) and the timer
    private JLabel lblTrackBestTime; //Best time on the track yet
    private JLabel lblBotBestTime; //Best time of the current bot
    private JLabel lblCountryShort; //Austria = AUT etc...

    private Font headFont;

    public static final Font DEFAULT_FONT = Font.getFont("")

    public static final String DEFAULT_HEAD = "<html><font color=#4A83DE>ROBOT CHALLENGE 2016</font></html>";
    public static final String PAUSE = "PAUSE";
    public static final String READY = "READY";

    public DisplayFrame(GraphicsDevice screen, Font headFont){
        super("Robot Timer"); //set title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.headFont = headFont;

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocation(screen.getDefaultConfiguration().getBounds().getLocation());

        createWidgets();
        addWidgets();
    }

    private void createWidgets(){
        lblBotName = new JLabel(DEFAULT_HEAD);
        lblBotName.setFont(headFont);

        lblStatus = new JLabel(PAUSE);

    }

    private void addWidgets(){
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(lblBotName, BorderLayout.CENTER);
    }
}
