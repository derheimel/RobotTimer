package at.innoc.rc.db;

/**
 * Created by Aaron on 19.02.2016.
 */
public class Competition {

    private int uid; //pk_comp
    private String name; //comp_name

    public static final Competition FIRST_ITEM = new Competition(-1, "SELECT");

    public Competition(int uid, String name) {
        this.uid = uid;
        setName(name);
    }

    @Override
    public String toString(){
        return this.name;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
