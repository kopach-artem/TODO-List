package src;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Model {
    private HashMap<String, Task> mapList = new HashMap<>();
    private String fName = "taskList.bin";

    
    /** 
     * @param name
     * @param text
     * @param done
     * @param priority
     * @param date
     * @param estimate
     */
    public void add(String name, String text, Boolean done, String priority, Date date, Duration estimate) {
        Task task = new Task(name, text, done, priority, date, estimate);
        mapList.put(name, task);
    }

    
    /** 
     * @return ArrayList<Task>
     */
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (String taskName : mapList.keySet()) {
            tasks.add(mapList.get(taskName));
        }
        tasks.sort(new TaskComparator());
        return tasks;
    }

    
    /** 
     * @throws IOException
     */
    public void save() throws IOException {
        FileOutputStream fOutputStream = new FileOutputStream(fName);
        ObjectOutputStream outputStream = new ObjectOutputStream(fOutputStream);
        outputStream.writeObject(mapList);
        fOutputStream.close();
        outputStream.close();
    }

    
    /** 
     * @return ArrayList<Task>
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ArrayList<Task> load() throws IOException, ClassNotFoundException {
        FileInputStream fInputStream = new FileInputStream(fName);
        ObjectInputStream oInputStream = new ObjectInputStream(fInputStream);
        HashMap<String, Task> tmp = (HashMap) oInputStream.readObject();
        mapList.clear();
        ArrayList<Task> tasks = new ArrayList<>();
        for (String tasksName : tmp.keySet()) {
            mapList.put(tasksName, tmp.get(tasksName));
            tasks.add(tmp.get(tasksName));
        }
        oInputStream.close();
        fInputStream.close();
        tasks.sort(new TaskComparator());
        return tasks;
    }

    
    /** 
     * @param value
     */
    public void delete(String value) {
        mapList.remove(value);
    }

    
    /** 
     * @param name
     * @return Task
     */
    public Task getTask(String name) {
        return mapList.get(name);
    }

    
    /** 
     * @param oldName
     * @param newName
     * @param text
     * @param done
     * @param priority
     * @param date
     * @param estimate
     * @return ArrayList<Task>
     */
    public ArrayList<Task> update(String oldName, String newName, String text, Boolean done, String priority, Date date,
            Duration estimate) {
        delete(oldName);
        add(newName, text, done, priority, date, estimate);
        return getTasks();
    }
}
