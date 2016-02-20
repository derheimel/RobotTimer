package at.innoc.rc.db;

/**
 * Created by Aaron on 20.02.2016.
 */
public class Result {

    private int uid; //pk_res
    private int status; //stat_pk_stat
    private Bot bot; //bot_comp_bot_pk_bot
    private Competition comp; //bot_comp_comp_pk_comp
    private int tries; //res_id1

    public Result(int status, Bot bot, Competition comp, int tries, int uid){
        this.uid = uid;
        this.status = status;
        this.bot = bot;
        this.comp = comp;
        this.tries = tries;
    }

    public Result(int status, Bot bot, Competition comp, int tries){
        this(status, bot, comp, tries, -1);
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUid() {
        return uid;
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
}
