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


    class FlagPanel extends JPanel{

        private BufferedImage flag;

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            if(flag == null) return;

            int x = (getWidth() - flag.getWidth()) / 2;
            int y = 0;
            g.drawImage(flag, x, y, null);
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
        Dimension topBottomDim = new Dimension(width, height / 4);
        Dimension flagDim = new Dimension(width / 3, (int)topBottomDim.getHeight());
        int borderSize = 5;
        Border border = BorderFactory.createLineBorder(Color.BLACK, borderSize);
        Border flagBorder = BorderFactory.createMatteBorder(0, borderSize * 2, 0, 0, Color.BLACK);

        lblBotName = new JLabel();
        lblBotName.setFont(headFont);
        lblBotName.setHorizontalAlignment(SwingConstants.CENTER);
//        lblBotName.setVerticalAlignment(SwingConstants.CENTER);
        lblBotName.setPreferredSize(topBottomDim);
        lblBotName.setBorder(border);

        lblStatus = new JLabel();
        lblStatus.setFont(defaultFont.deriveFont(Font.BOLD, 400));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setVerticalAlignment(SwingConstants.CENTER);
        lblStatus.setOpaque(true);

        lblTrackBestTime = new JLabel();
        lblTrackBestTime.setFont(defaultFont);

        lblBotBestTime = new JLabel();
        lblBotBestTime.setFont(defaultFont);

        lblCountryShort = new JLabel();
        lblCountryShort.setFont(defaultFont);
        lblCountryShort.setVerticalAlignment(SwingConstants.CENTER);

        pnlCenter = new JPanel();
        pnlCenter.setBorder(border);

        pnlInfo = new JPanel();

        pnlFlag = new FlagPanel();
        pnlFlag.setPreferredSize(flagDim);
        pnlFlag.setMaximumSize(flagDim);
        pnlFlag.setBorder(flagBorder);

        pnlBottom = new JPanel();
        pnlBottom.setBorder(border);

    }

    private void addWidgets(){
        contentPane.setLayout(new BorderLayout());

        pnlCenter.setLayout(new BorderLayout());
        pnlCenter.add(lblStatus, BorderLayout.CENTER);

        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.PAGE_AXIS));
        pnlInfo.add(lblTrackBestTime);
        pnlInfo.add(lblBotBestTime);

        pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.LINE_AXIS));
        pnlBottom.add(pnlInfo);
        pnlBottom.add(Box.createHorizontalGlue());
        pnlBottom.add(lblCountryShort);
        pnlBottom.add(pnlFlag);

        contentPane.add(lblBotName, BorderLayout.PAGE_START);
        contentPane.add(pnlCenter, BorderLayout.CENTER);
        contentPane.add(pnlBottom, BorderLayout.PAGE_END);
    }

    protected void updateFlag(BufferedImage flag){
        int height = pnlFlag.getHeight();
        int width = (int)(1f * flag.getWidth() / flag.getHeight() * height);

        Image tmp = flag.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage scaledFlag = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = scaledFlag.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        Dimension dim = new Dimension(width, height);
        pnlFlag.setPreferredSize(dim);
        pnlFlag.setMaximumSize(dim);
        pnlFlag.flag = scaledFlag;
        SwingUtilities.updateComponentTreeUI(this);
        pnlFlag.repaint();
    }

    public Font getDefaultFont(){
        return this.defaultFont;
    }

    protected JLabel getLblStatus(){
        return this.lblStatus;
    }

    protected JLabel getLblBotName(){
        return this.lblBotName;
    }

    protected JLabel getLblTrackBestTime(){
        return this.lblTrackBestTime;
    }

    protected JLabel getLblBotBestTime(){
        return this.lblBotBestTime;
    }

    protected JLabel getLblCountryShort(){
        return this.lblCountryShort;
    }
}
