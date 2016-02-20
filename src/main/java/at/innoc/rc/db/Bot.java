package at.innoc.rc.db;

/**
 * Created by Aaron on 20.02.2016.
 */
public class Bot {

    private int uid; //pk_bot
    private int startNr; //bot_startnummer
    private int tries; //
    private String country; //via team_pk_team -> team_land
    private String name; //bot_name

    public static final Bot FIRST_ITEM = new Bot(-1, -1, "AT", "SELECT");

    public Bot(int uid, int startNr, String country, String name){
        this.uid = uid;
        this.startNr = startNr;
        this.country = country;
        this.name = name;
    }

    @Override
    public String toString(){
        return this.startNr + " " + this.name;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public int getStartNr() {
        return startNr;
    }
}
