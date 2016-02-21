package at.innoc.rc.gui;

import at.innoc.rc.db.Bot;
import at.innoc.rc.db.Competition;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Properties;

/**
 * The referee uses this frame to control the program
 */
public class OperatorFrame extends JFrame{

    private JComboBox<Competition> cbComps;
    private JList<Bot> jlBots;
    private JScrollPane spBots;
    private JToggleButton btnReady;
    private JButton btnInvalidate;
    private JButton btnConfirm;
    private JLabel lblTries;
    private JLabel lblStatus;

    private JPanel pnlButtons;
    private JPanel pnlButtonsWrapper;
    private JPanel pnlTries;
    private JPanel pnlStatus;
    private Container contentPane = getContentPane();
    private OperatorListener opListener;

    private DisplayFrame display;


    public OperatorFrame(DisplayFrame display){
        super("Robot Timer Operator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.opListener = new OperatorListener(this, display);
        this.display = display;
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

        spBots = new JScrollPane();
        spBots.setViewportView(jlBots);
        spBots.setPreferredSize(new Dimension(200, 400));

        btnReady = new JToggleButton("Ready");
        btnReady.setActionCommand("btnReady");
        btnReady.addActionListener(opListener);
        btnReady.setOpaque(true);

        btnInvalidate = new JButton("Invalidate");
        btnInvalidate.setActionCommand("btnInvalidate");
        btnInvalidate.addActionListener(opListener);

        btnConfirm = new JButton("Confirm");
        btnConfirm.setActionCommand("btnConfirm");
        btnConfirm.addActionListener(opListener);

        lblTries = new JLabel("-");
        lblTries.setFont(lblTries.getFont().deriveFont(Font.BOLD, 30));

        lblStatus = new JLabel("PAUSE");
        lblStatus.setFont(display.getDefaultFont().deriveFont(Font.BOLD, 70));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setOpaque(true);

        pnlButtons = new JPanel();

        pnlButtonsWrapper = new JPanel();

        pnlTries = new JPanel();
        Border b = BorderFactory.createEtchedBorder();
        pnlTries.setBorder(BorderFactory.createTitledBorder(b, "TRIES"));
        pnlTries.add(lblTries);
        pnlTries.setSize(150, 150);

        pnlStatus = new JPanel();
        pnlStatus.setBorder(BorderFactory.createLineBorder(Color.GREEN));

        opListener.initialize(cbComps);
    }

    private void addWidgets(){
        pnlButtons.setLayout(new GridLayout(2, 3));
        pnlButtons.add(btnReady);
        pnlButtons.add(btnInvalidate);
        pnlButtons.add(btnConfirm);
        pnlButtons.setMaximumSize(pnlButtons.getPreferredSize());

        pnlButtonsWrapper.setLayout(new BoxLayout(pnlButtonsWrapper, BoxLayout.PAGE_AXIS));
        pnlButtonsWrapper.add(pnlButtons);
        pnlButtonsWrapper.add(Box.createVerticalGlue());
        pnlButtonsWrapper.add(pnlTries);

        pnlStatus.setLayout(new BorderLayout());
        pnlStatus.add(lblStatus);

        contentPane.setLayout(new BorderLayout());
        contentPane.add(cbComps, BorderLayout.PAGE_START);
        contentPane.add(spBots, BorderLayout.LINE_START);
        contentPane.add(pnlStatus, BorderLayout.CENTER);
        contentPane.add(pnlButtonsWrapper, BorderLayout.LINE_END);
    }

    protected JList<Bot> getJlBots() {
        return jlBots;
    }

    protected JLabel getLblTries() {
        return lblTries;
    }

    protected JLabel getLblStatus() {
        return lblStatus;
    }
}
