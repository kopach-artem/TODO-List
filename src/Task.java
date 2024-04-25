package src;
import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

public class Task implements Serializable{
    public String name;
    public String text;
    public Boolean done;
    public String priority;
    public Date date;
    public Duration estimate;
    public Task(String name, String text, Boolean done, String priority, Date date, Duration estimate){
        this.name = name;
        this.text = text;
        this.done = done;
        this.priority = priority;
        this.date = date;
        this.estimate = estimate;
    }
    
}
