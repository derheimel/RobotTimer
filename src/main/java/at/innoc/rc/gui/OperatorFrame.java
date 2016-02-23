package at.innoc.rc.gui;

import at.innoc.rc.db.Bot;
import at.innoc.rc.db.Competition;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * The referee uses this frame to control the program
 */
public class OperatorFrame extends JFrame{

    private JComboBox<Competition> cbComps;
    private JList<Bot> jlBots;
    private JScrollPane spBots;
    private JToggleButton btnReady;
    private JButton btnStop;
    private JRadioButton btnAbort;
    private JRadioButton btnInvalidate;
    private JRadioButton btnConfirm;
    private JButton btnOK;
    private JLabel lblTries;
    private JLabel lblStatus;
    private JLabel lblCheck;

    private JPanel pnlButtons;
    private JPanel pnlNormalButtons;
    private JPanel pnlRadioButtons;
    private JPanel pnlCheck;
    private JPanel pnlRight;
    private JPanel pnlTries;
    private JPanel pnlStatus;
    private Container contentPane = getContentPane();
    private OperatorListener opListener;

    private DisplayFrame display;


    public OperatorFrame(DisplayFrame display){
        super("Robot Timer Operator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.display = display;
        this.opListener = new OperatorListener(display, this);
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

        btnStop = new JButton("Stop");
        btnStop.setActionCommand("btnStop");
        btnStop.addActionListener(opListener);
        btnStop.setSize(btnReady.getMinimumSize());
        btnStop.setEnabled(false);

        btnAbort = new JRadioButton("Abort");
        btnAbort.setActionCommand("btnAbort");
        btnAbort.addActionListener(opListener);
        btnAbort.setEnabled(false);

        btnInvalidate = new JRadioButton("Invalidate");
        btnInvalidate.setActionCommand("btnInvalidate");
        btnInvalidate.addActionListener(opListener);
        btnInvalidate.setEnabled(false);

        btnConfirm = new JRadioButton("Confirm");
        btnConfirm.setActionCommand("btnConfirm");
        btnConfirm.addActionListener(opListener);
        btnConfirm.setEnabled(false);

        ButtonGroup bg = new ButtonGroup();
        bg.add(btnAbort);
        bg.add(btnInvalidate);
        bg.add(btnConfirm);

        btnOK = new JButton("OK");
        btnOK.setEnabled(false);

        lblTries = new JLabel("-");
        lblTries.setFont(lblTries.getFont().deriveFont(Font.BOLD, 30));

        lblStatus = new JLabel("PAUSE");
        lblStatus.setFont(display.getDefaultFont().deriveFont(Font.BOLD, 70));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setOpaque(true);
        lblStatus.setMinimumSize(lblStatus.getPreferredSize());

        lblCheck = new JLabel();
        lblCheck.setHorizontalAlignment(SwingConstants.CENTER);
        lblCheck.setVerticalAlignment(SwingConstants.BOTTOM);

        pnlButtons = new JPanel();

        pnlNormalButtons = new JPanel();

        pnlRadioButtons = new JPanel();

        pnlCheck = new JPanel();

        pnlRight = new JPanel();

        pnlTries = new JPanel();
        Border b = BorderFactory.createEtchedBorder();
        pnlTries.setBorder(BorderFactory.createTitledBorder(b, "TRIES"));
        pnlTries.setSize(150, 150);

        pnlStatus = new JPanel();
        pnlStatus.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK));
        pnlStatus.setPreferredSize(new Dimension(500, (int) pnlStatus.getPreferredSize().getHeight()));

        opListener.initialize(cbComps);
    }

    private void addWidgets(){
        pnlNormalButtons.setLayout(new GridLayout(2, 1));
        pnlNormalButtons.add(btnReady);
        pnlNormalButtons.add(btnStop);

        pnlRadioButtons.setLayout(new BoxLayout(pnlRadioButtons, BoxLayout.PAGE_AXIS));
        pnlRadioButtons.add(btnAbort);
        pnlRadioButtons.add(btnInvalidate);
        pnlRadioButtons.add(btnConfirm);

        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.LINE_AXIS));
        pnlButtons.add(pnlNormalButtons);
        pnlButtons.add(pnlRadioButtons);
        pnlButtons.setMaximumSize(pnlButtons.getPreferredSize());

        pnlCheck.setLayout(new GridLayout(2, 1));
        pnlCheck.add(lblCheck);
        pnlCheck.add(btnOK);

        pnlTries.add(lblTries);

        pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.PAGE_AXIS));
        pnlRight.add(pnlButtons);
        pnlRight.add(Box.createVerticalGlue());
        pnlRight.add(pnlCheck);
        pnlRight.add(pnlTries);

        pnlStatus.setLayout(new BorderLayout());
        pnlStatus.add(lblStatus);

        contentPane.setLayout(new BorderLayout());
        contentPane.add(cbComps, BorderLayout.PAGE_START);
        contentPane.add(spBots, BorderLayout.LINE_START);
        contentPane.add(pnlStatus, BorderLayout.CENTER);
        contentPane.add(pnlRight, BorderLayout.LINE_END);
    }

    protected boolean ready(){
        return this.btnReady.isSelected();
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

    protected JToggleButton getBtnReady(){
        return this.btnReady;
    }

    protected JRadioButton getBtnConfirm() {
        return btnConfirm;
    }

    protected JRadioButton getBtnInvalidate() {
        return btnInvalidate;
    }

    protected JButton getBtnOK() {
        return btnOK;
    }

    protected JLabel getLblCheck() {
        return lblCheck;
    }

    protected JRadioButton getBtnAbort() {
        return btnAbort;
    }

    protected JButton getBtnStop() {
        return btnStop;
    }

    public OperatorListener getOpListener(){
        return this.opListener;
    }
}
