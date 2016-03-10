package at.innoc.rc.db;

import javax.swing.*;
import java.util.List;

/**
 * Created by Aaron on 19.02.2016.
 */
public interface Dao {

    boolean saveResult(Result result);

    List<Competition> getLineFollowerComps();

    List<Bot> getBotsByCompetition(Competition comp);

    int getBestTimeByMode(String mode);

    int getBestTimeByMode(String mode, Bot bot);

    int getTries(String mode, Bot bot);

    void updateCurrentMatch(int resultUid, int compUid);

}
