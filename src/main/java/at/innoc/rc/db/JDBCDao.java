package at.innoc.rc.db;

import at.innoc.rc.RobotTimer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Aaron on 20.02.2016.
 */
public class JDBCDao implements Dao {

    private Connection conn;
    private Properties props;

    private final String RESULT_UID = "pk_res";
    private final String RESULT_STATUS = "stat_pk_stat";
    private final String RESULT_BOT = "bot_comp_bot_pk_bot";
    private final String RESULT_COMP = "bot_comp_comp_pk_comp";
    private final String RESULT_TRIES = "res_id1";
    private final String RESULT_TIME = "res_result_short";

    private final String COMP_UID = "pk_comp";
    private final String COMP_NAME = "comp_name";
    private final String COMP_COMPCLASS = "compclass_pk_compclass";
    private final String COMP_EVENT = "event_pk_event";
    private final String COMP_CUR_MATCH = "comp_current_match";
    private final String COMP_CUR_MATCH_TIME = "comp_current_match_time";

    private final String BOT_UID = "pk_bot";
    private final String BOT_STARTNR = "bot_startnummer";
    private final String BOT_TEAM = "team_pk_team";
    private final String BOT_NAME = "bot_name";
    private final String BOT_SOFTSTORNO = "bot_softstorno";

    private final String BOTCOMP_BOT_UID = "bot_pk_bot";
    private final String BOTCOMP_COMP_UID = "comp_pk_comp";

    private final String TEAM_UID = "pk_team";
    private final String TEAM_COUNTRY = "team_land";

    public JDBCDao(){
        this.props = RobotTimer.getProperties();
        try {
            this.conn = getConnection();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            RobotTimer.exit("Couldn't load JDBC driver");
        } catch (SQLException e) {
            e.printStackTrace();
            RobotTimer.exit("Couldn't connect to DB");
        }
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException{
        String connString = "jdbc:mysql://" + props.getProperty("host") +
                "/" + props.getProperty("db") +
                "?user=" + props.getProperty("user") +
                "&password=" + props.getProperty("password") +
                "&useUnicode=true&characerEncoding=utf8";

        Connection conn = null;

        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(connString);

        return conn;
    }

    @Override
    public boolean saveResult(Result entity) {
        try {
            String query;
            int time = entity.getTime();
            if(time == 0) {
                query = "insert into res(" + RESULT_STATUS + ", " + RESULT_BOT + ", " + RESULT_COMP + ", " + RESULT_TRIES + ") " +
                        "values(?, ?, ?, ?)";
            }
            else{
                query = "insert into res(" + RESULT_STATUS + ", " + RESULT_BOT + ", " + RESULT_COMP + ", " + RESULT_TRIES + ", " + RESULT_TIME + ") " +
                        "values(?, ?, ?, ?, " + time + ")";
            }
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, entity.getStatus());
            pst.setInt(2, entity.getBot().getUid());
            pst.setInt(3, entity.getComp().getUid());
            pst.setInt(4, entity.getTries());
            pst.executeUpdate();
            ResultSet rs = pst.executeQuery("select max(" + RESULT_UID + ") as id from res");
            rs.next();
            int resultUid = rs.getInt("id");
            pst.close();
            updateCurrentMatch(resultUid, entity.getComp().getUid());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public List<Competition> getLineFollowerComps() {
        List<Competition> comps = new ArrayList<>();

        try{
            PreparedStatement pst = conn.prepareStatement(
                    "select " + COMP_UID + ", " + COMP_NAME + " " +
                            "from comp where " + COMP_COMPCLASS + " = 1 " +
                            "and " + COMP_EVENT + " = " + props.getProperty("event") + " " +
                            "and " + COMP_NAME + " like '%Follower%' " +
                            "order by " + COMP_NAME);

            ResultSet rs = pst.executeQuery();

            comps.add(Competition.FIRST_ITEM);

            while(rs.next()){
                Competition t = new Competition(rs.getInt(COMP_UID), rs.getString(COMP_NAME));
                comps.add(t);
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comps;
    }

    @Override
    public List<Bot> getBotsByCompetition(Competition comp) {
        List<Bot> bots = new ArrayList<>();

        try{
            PreparedStatement pst = conn.prepareStatement(
                    "select " + BOT_UID + ", " + BOT_STARTNR + ", " + TEAM_COUNTRY + ", " + BOT_NAME + " " +
                            "from bot_comp join bot join team " +
                            "on team." + TEAM_UID + " = bot." + BOT_TEAM + " " +
                            "and bot." + BOT_UID + " = bot_comp." + BOTCOMP_BOT_UID + " " +
                            "where " + BOTCOMP_COMP_UID + " = " + comp.getUid() + " " +
                            "and " + BOT_SOFTSTORNO + " = 0 " +
                            "order by " + BOT_STARTNR
            );

            ResultSet rs = pst.executeQuery();

            bots.add(Bot.FIRST_ITEM);

            while(rs.next()){
                Bot b = new Bot(rs.getInt(BOT_UID), rs.getInt(BOT_STARTNR), rs.getString(TEAM_COUNTRY), rs.getString(BOT_NAME));
                bots.add(b);
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bots;
    }

    private List<Competition> getCompsByMode(String modus){
        List<Competition> comps = new ArrayList<>();
        List<Competition> allComps = getLineFollowerComps();
        boolean normal = false;
        if(modus.equals("normal")) normal = true;
        for(Competition c : allComps){
            if(normal){
                String compName = c.getName().toLowerCase();
                if(!(compName.contains("enhanced") ||
                        compName.contains("lego"))){
                    comps.add(c);
                }
            }
            else if(c.getName().toLowerCase().contains(modus)){
                comps.add(c);
            }
        }
        return comps;
    }

    @Override
    public int getBestTimeByMode(String mode) {
        return getBestTimeByMode(mode, null);
    }

    @Override
    public int getBestTimeByMode(String mode, Bot bot) {
        int bestTime = Integer.MAX_VALUE;

        List<Competition> comps = getCompsByMode(mode);

        for(Competition c : comps) {
            int cUid = c.getUid();
            try {
                String botStr = "";
                if(bot != null) botStr = "and " + RESULT_BOT + " = " + bot.getUid();
                PreparedStatement pst = conn.prepareStatement(
                        "select min(" + RESULT_TIME + ") as result " +
                                "from res " +
                                "where " + RESULT_COMP + " = " + cUid + " " +
                                botStr
                );
                ResultSet rs = pst.executeQuery();
                rs.next();
                int tmpBestTime = rs.getInt("result");
                if(tmpBestTime != 0 && tmpBestTime < bestTime){
                    bestTime = tmpBestTime;
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bestTime == Integer.MAX_VALUE ? 0 : bestTime;
    }

    @Override
    public int getTries(String mode, Bot bot) {
        int tries = 0;

        List<Competition> comps = getCompsByMode(mode);

        for(Competition c : comps) {
            try {
                PreparedStatement pst = conn.prepareStatement(
                        "select max(" + RESULT_TRIES + ") as tries " +
                                "from res " +
                                "where " + RESULT_COMP + " = " + c.getUid() + " " +
                                "and " + RESULT_BOT + " = " + bot.getUid()
                );

                ResultSet rs = pst.executeQuery();
                rs.next();
                tries += rs.getInt("tries");
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return tries;
    }

    @Override
    public void updateCurrentMatch(int resultUid, int compUid) {
        try {
            PreparedStatement pst = conn.prepareStatement(
                    "update comp " +
                            "set " + COMP_CUR_MATCH + "= ?, " +
                            COMP_CUR_MATCH_TIME + " = now() " +
                            "where " + COMP_UID + " = ?"
            );

            pst.setInt(1, resultUid);
            pst.setInt(2, compUid);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
