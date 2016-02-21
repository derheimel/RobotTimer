package at.innoc.rc.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Displays the timer - is shown to the audience
 */
public class DisplayFrame extends JFrame{

    GraphicsDevice screen;

    private JPanel pnlCenter;
    private JPanel pnlBottom;
    private JPanel pnlInfo;
    private FlagPanel pnlFlag;

    private JLabel lblBotName;
    private JLabel lblStatus; //The status (READY, PAUSE...) and the timer
    private JLabel lblTrackBestTime; //Best time on the track yet
    private JLabel lblBotBestTime; //Best time of the current bot
    private JLabel lblCountryShort; //Austria = AUT etc...

    private Container contentPane = getContentPane();

    private Font headFont;
    private Font defaultFont;

    public static final String DEFAULT_HEAD = "<html><font color=#4A83DE>ROBOT CHALLENGE 2016</font></html>";
    public static final String PAUSE = "PAUSE";
    public static final String READY = "READY";
    public static final String TRACK_BEST = "Track record: ";
    public static final String BOT_BEST = "Current bot record: ";

    class FlagPanel extends JPanel{

        private BufferedImage flag;

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            g.drawImage(flag, 0, 0, null);
        }

    }

    public DisplayFrame(GraphicsDevice screen, Font headFont){
        super("Robot Timer"); //set title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.headFont = headFont;
        this.defaultFont = new Font("Calibri", Font.BOLD, 80);
        this.screen = screen;

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocation(screen.getDefaultConfiguration().getBounds().getLocation());

        createWidgets();
        addWidgets();
    }

    private void createWidgets(){
        int height = screen.getDisplayMode().getHeight();
        int width = screen.getDisplayMode().getWidth();

        lblBotName = new JLabel(DEFAULT_HEAD);
        lblBotName.setFont(headFont);
        lblBotName.setHorizontalAlignment(SwingConstants.CENTER);
        lblBotName.setVerticalAlignment(SwingConstants.CENTER);

        lblStatus = new JLabel(PAUSE);
        lblStatus.setFont(defaultFont.deriveFont(Font.BOLD, 400));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setVerticalAlignment(SwingConstants.CENTER);

        lblTrackBestTime = new JLabel();
        lblTrackBestTime.setFont(defaultFont);

        lblBotBestTime = new JLabel();
        lblBotBestTime.setFont(defaultFont);

        lblCountryShort = new JLabel();
        lblCountryShort.setFont(defaultFont);

        pnlCenter = new JPanel();
        pnlCenter.setMaximumSize(new Dimension(width, height / 2));

        pnlInfo = new JPanel();

        pnlFlag = new FlagPanel();
        pnlFlag.setPreferredSize(new Dimension(width / 3, height / 4));
        pnlFlag.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        pnlBottom = new JPanel();
        pnlBottom.setPreferredSize(new Dimension(width, height / 4));

    }

    private void addWidgets(){
        contentPane.setLayout(new BorderLayout());

        pnlCenter.setLayout(new BorderLayout());
        pnlCenter.add(lblStatus, BorderLayout.CENTER);

        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.PAGE_AXIS));
        pnlInfo.add(lblTrackBestTime);
        pnlInfo.add(lblBotBestTime);

        pnlBottom.setLayout(new GridLayout(1, 3));
        pnlBottom.add(pnlInfo);
        pnlBottom.add(pnlFlag);

//        JButton test = new JButton("TEST");
//        test.setFont(test.getFont().deriveFont(Font.BOLD, 100));
//
//        pnlBottom.add(test);

        contentPane.add(lblBotName, BorderLayout.PAGE_START);
        contentPane.add(pnlCenter, BorderLayout.CENTER);
        contentPane.add(pnlBottom, BorderLayout.PAGE_END);
    }

    protected void updateFlag(BufferedImage image){
        pnlFlag.flag = image;
        pnlFlag.repaint();
//        pnlFlag.paintComponent();
    }
}
