package at.innoc.rc.db;

import at.innoc.rc.RobotTimer;

import java.io.FileInputStream;
import java.io.IOException;
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

    private final String COMP_UID = "pk_comp";
    private final String COMP_NAME = "comp_name";
    private final String COMP_COMPCLASS = "compclass_pk_compclass";
    private final String COMP_EVENT = "event_pk_event";

    public JDBCDao(){
        String file = "config.ini";
        try {
            this.props = getProperties(file);
        } catch (IOException e) {
            e.printStackTrace();
            RobotTimer.exit("Couldn't read " + file);
        }
        this.conn = getConnection();
    }

    private Properties getProperties(String file) throws IOException{
        Properties props = new Properties();
        props.load(new FileInputStream(file));
        return props;
    }

    private Connection getConnection(){
        String connString = "jdbc:mysql://" + props.getProperty("host") +
                "/" + props.getProperty("db") +
                "?user=" + props.getProperty("user") +
                "&password=" + props.getProperty("password") +
                "&useUnicode=true&characerEncoding=utf8";

        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(connString);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            RobotTimer.exit("Couldn't load JDBC driver");
        } catch (SQLException e) {
            e.printStackTrace();
            RobotTimer.exit("Couldn't connect to DB");
        }
        return conn;
    }

    @Override
    public boolean saveResult(Result entity) {
        int uid = entity.getUid();
        try {
            String query = "";
            if(uid == -1) {
                query = "insert into res(" + RESULT_STATUS + ", " + RESULT_BOT + ", " + RESULT_COMP + ", " + RESULT_TRIES + ") " +
                        "values(?, ?, ?, ?)";
            }
            else{
                query = "update res set " + RESULT_STATUS + " = ?, " + RESULT_BOT + " = ?, "
                        + RESULT_COMP + " = ?, " + RESULT_TRIES + " = ? where " + RESULT_UID + " = " + uid;
            }
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, entity.getStatus());
            pst.setInt(2, entity.getBot().getUid());
            pst.setInt(3, entity.getComp().getUid());
            pst.setInt(4, entity.getTries());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public Competition[] getLineFollowerComps() {
        Competition[] comps = null;

        try{
            PreparedStatement pst = conn.prepareStatement(
                    "select " + COMP_UID + ", " + COMP_NAME + " " +
                    "from comp where " + COMP_COMPCLASS + " = 1 " +
                    "and " + COMP_EVENT + " = " + props.getProperty("event") + " " +
                    "and " + COMP_NAME + " like '%Follower%' " +
                    "order by " + COMP_NAME);

            ResultSet rs = pst.executeQuery();

            List<Competition> tmpComps = new ArrayList<>();
            tmpComps.add(Competition.FIRST_ITEM);

            while(rs.next()){
                Competition t = new Competition(rs.getInt(COMP_UID), rs.getString(COMP_NAME));
                tmpComps.add(t);
            }

            comps = tmpComps.toArray(comps);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comps;
    }
}
