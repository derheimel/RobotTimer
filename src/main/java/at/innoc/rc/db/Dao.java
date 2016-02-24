package at.innoc.rc.db;

import javax.swing.*;
import java.util.List;

/**
 * Created by Aaron on 19.02.2016.
 */
public interface Dao {

    public boolean saveResult(Result result);

    public List<Competition> getLineFollowerComps();

    public List<Bot> getBotsByCompetition(Competition comp);

    public int getBestTimeByCompetitions(JComboBox<Competition> cbComps, String filter);

    public int getBestTimeByCompetitions(JComboBox<Competition> cbComps, String filter, Bot bot);

}
