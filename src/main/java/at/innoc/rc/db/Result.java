package at.innoc.rc.db;

/**
 * Created by Aaron on 20.02.2016.
 */
public class Result {

    private int status; //stat_pk_stat
    private Bot bot; //bot_comp_bot_pk_bot
    private Competition comp; //bot_comp_comp_pk_comp
    private int tries; //res_id1
    private int time;

    public Result(int status, Bot bot, Competition comp, int tries, int time){
        this.status = status;
        this.bot = bot;
        this.comp = comp;
        this.tries = tries;
        this.time = time;
    }

    public Result(int status, Bot bot, Competition comp, int tries){
        this(status, bot, comp, tries, 0);
    }

    public int getStatus() {
        return status;
    }

    public Bot getBot() {
        return bot;
    }

    public Competition getComp() {
        return comp;
    }

    public int getTries() {
        return tries;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time){
        this.time = time;
    }
}
