package red.man10.man10mysqlapi;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by sho-pc on 2017/06/14.
 */
public class QueryExecutedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String querry;



    public QueryExecutedEvent(String query) {
        querry = query;
    }

    public String getQuery(){
        return querry;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
