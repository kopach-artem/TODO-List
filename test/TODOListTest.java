package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import src.*;

public class TODOListTest {
    private Model model;
    private SimpleDateFormat dateFormat;
    private TaskComparator taskComparator;

    @Before
    public void TestBefore1() {
        model = new Model();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        taskComparator = new TaskComparator();
    }

    
    /** 
     * @throws ParseException
     */
    @Test
    public void TestAdd1() throws ParseException {
        String name = "Task #1";
        String text = "Go to hospital";
        Boolean done = false;
        String priority = "Low";
        Date date = dateFormat.parse("2022/12/12");
        Duration estimate = Duration.ofHours(Long.parseLong("2"));
        Duration estimateWrong = Duration.ofHours(Long.parseLong("3"));
        model.add(name, text, done, priority, date, estimate);
        Task task = model.getTask("Task #1");
        assertEquals(name, task.name);
        assertEquals(text, task.text);
        assertEquals(done, task.done);
        assertEquals(priority, task.priority);
        assertEquals(date, task.date);
        assertEquals(estimate, task.estimate);
        assertNotEquals(estimateWrong, task.estimate);
    }

    
    /** 
     * @throws ParseException
     */
    @Test
    public void TestAdd2() throws ParseException {
        String name = "Task #10";
        String fakeName = "Java HF";
        String text = "Go to lear Java 11 Version";
        Boolean done = false;
        String priority = "High";
        Date date = dateFormat.parse("2022/10/11");
        Duration estimate = Duration.ofHours(Long.parseLong("200"));
        Duration estimateWrong = Duration.ofHours(Long.parseLong("100"));
        model.add(name, text, done, priority, date, estimate);
        Task task = model.getTask("Task #10");
        assertEquals(name, task.name);
        assertNotEquals(fakeName, task.name);
        assertEquals(text, task.text);
        assertEquals(done, task.done);
        assertEquals(priority, task.priority);
        assertEquals(date, task.date);
        assertEquals(estimate, task.estimate);
        assertNotEquals(estimateWrong, task.estimate);
    }

    
    /** 
     * @throws ParseException
     */
    @Test
    public void TestRemove() throws ParseException {
        String name = "Task #15";
        String text = "Go to club";
        Boolean done = false;
        String priority = "Low";
        Date date = dateFormat.parse("2022/12/30");
        Duration estimate = Duration.ofHours(Long.parseLong("6"));
        model.add(name, text, done, priority, date, estimate);
        model.delete(name);
        Task task = model.getTask("Task #15");
        assertEquals(null, task);
    }

    
    /** 
     * @throws ParseException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void TestSaveLoad() throws ParseException, IOException, ClassNotFoundException {
        String name1 = "Task #6";
        String text1 = "Go to beach";
        Boolean done1 = false;
        String priority1 = "Medium";
        Date date1 = dateFormat.parse("2023/07/10");
        Duration estimate1 = Duration.ofHours(Long.parseLong("80"));
        String name2 = "Task #4";
        String text2 = "Go to school";
        Boolean done2 = true;
        String priority2 = "Low";
        Date date2 = dateFormat.parse("2022/10/01");
        Duration estimate2 = Duration.ofHours(Long.parseLong("6"));
        model.add(name2, text2, done2, priority2, date2, estimate2);
        model.add(name1, text1, done1, priority1, date1, estimate1);
        model.save();
        ArrayList<Task> tasks = model.load();
        assertEquals(name2, tasks.get(1).name);
        assertEquals(name1, tasks.get(0).name);
        assertNotEquals(estimate2, tasks.get(0).estimate);
        assertEquals(text2, tasks.get(1).text);
        assertEquals(text1, tasks.get(0).text);
        assertNotEquals(priority1, tasks.get(1).priority);
    }

    
    /** 
     * @throws ParseException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void TestGetTasks1() throws ParseException, IOException, ClassNotFoundException {
        ArrayList<Task> tasks = new ArrayList<>();
        model.add("Task #1", "Go to school", false, "Low", dateFormat.parse("2022/12/12"),
                Duration.ofHours(Long.parseLong("2")));
        model.add("Task #2", "Go to hospital", false, "High", dateFormat.parse("2022/12/12"),
                Duration.ofHours(Long.parseLong("3")));
        model.add("Task #3", "Go home", false, "Low", dateFormat.parse("2022/10/12"),
                Duration.ofHours(Long.parseLong("2")));
        model.add("Task #4", "Go to MOL", true, "Medium", dateFormat.parse("2022/12/12"),
                Duration.ofHours(Long.parseLong("5")));
        model.add("Task #5", "Pay utility bills", true, "High", dateFormat.parse("2023/02/12"),
                Duration.ofHours(Long.parseLong("2")));
        tasks = model.getTasks();
        assertEquals("Task #3", tasks.get(0).name);
        assertEquals("Task #5", tasks.get(4).name);
        assertNotEquals("Task #1", tasks.get(1).name);
    }

    
    /** 
     * @throws ParseException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void TestGetTasks2() throws ParseException, IOException, ClassNotFoundException {
        ArrayList<Task> tasks = new ArrayList<>();
        model.add("Task #6", "Learn SS", false, "Medium", dateFormat.parse("2022/12/12"),
                Duration.ofHours(Long.parseLong("2")));
        model.add("Task #7", "Should do HF", true, "High", dateFormat.parse("2021/11/12"),
                Duration.ofHours(Long.parseLong("3")));
        model.add("Task #8", "Go back to library until 8PM", false, "Low", dateFormat.parse("2022/10/12"),
                Duration.ofHours(Long.parseLong("2")));
        model.add("Task #9", "Go to cafe", true, "Medium", dateFormat.parse("2022/12/12"),
                Duration.ofHours(Long.parseLong("5")));
        model.add("Task #10", "Work with student 7 th Grade Math", true, "High", dateFormat.parse("2023/01/12"),
                Duration.ofHours(Long.parseLong("2")));
        tasks = model.getTasks();
        assertEquals("Task #8", tasks.get(0).name);
        assertNotEquals("Task #7", tasks.get(4).name); // We assume that dates, which are new, they collect upper!!!
                                                       // This is so important for NOT done case, because we would like
                                                       // to see the newest date at he top!
        assertNotEquals("Task #1", tasks.get(1).name);
    }

    
    /** 
     * @throws ParseException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void TestUpdate1() throws ParseException, IOException, ClassNotFoundException {
        ArrayList<Task> tasks = new ArrayList<>();
        model.add("Task #7", "Go to school", false, "Low", dateFormat.parse("2022/12/30"),
                Duration.ofHours(Long.parseLong("8")));
        model.update("Task #7", "Task #1", "Go to school", true, "Low", dateFormat.parse("2022/12/12"),
                Duration.ofHours(Long.parseLong("2")));
        tasks = model.getTasks();
        assertEquals("Task #1", tasks.get(0).name);
        assertEquals(dateFormat.parse("2022/12/12"), tasks.get(0).date);
        assertEquals(Duration.ofHours(Long.parseLong("2")), tasks.get(0).estimate);
    }

    
    /** 
     * @throws ParseException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void TestUpdate2() throws ParseException, IOException, ClassNotFoundException {
        ArrayList<Task> tasks = new ArrayList<>();
        model.add("Task #18", "Go to BME", true, "High", dateFormat.parse("2022/12/11"),
                Duration.ofHours(Long.parseLong("8")));
        model.update("Task #7", "Task #6", "Go to doctor", false, "Low", dateFormat.parse("2022/12/10"),
                Duration.ofHours(Long.parseLong("2")));
        tasks = model.getTasks();
        assertEquals("Task #6", tasks.get(0).name);
        assertEquals("Go to doctor", tasks.get(0).text);
        assertEquals(dateFormat.parse("2022/12/10"), tasks.get(0).date);
        assertEquals(Duration.ofHours(Long.parseLong("2")), tasks.get(0).estimate);
    }

    
    /** 
     * @throws ParseException
     */
    @Test
    public void TestTaskComparator() throws ParseException {
        Task first = new Task("Task #1", "Go to university", false, "High", dateFormat.parse("2022/12/12"),
                Duration.ofHours(Long.parseLong("2")));
        Task second = new Task("Task #2", "Learn Java", false, "Low", dateFormat.parse("2022/12/12"),
                Duration.ofHours(Long.parseLong("20")));
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(first);
        taskList.add(second);
        taskList.sort(taskComparator);// sort get (a,b) , but a is last element at ArrayList, check this all GOOD!
        int result = taskComparator.compare(second, first);
        assertEquals(first.name, taskList.get(0).name);
        assertEquals(1, result);
    }

    
    /** 
     * @throws ParseException
     */
    @Test
    public void TestTask() throws ParseException {
        Task first = new Task("Task #10", "Run to university, write exam", true, "Medium",
                dateFormat.parse("2022/12/01"), Duration.ofHours(Long.parseLong("4")));
        assertNotNull(first);
        assertEquals("Task #10", first.name);
        assertEquals("Run to university, write exam", first.text);
        assertEquals(true, first.done);
        assertEquals("Medium", first.priority);
    }

    
    /** 
     * @throws ParseException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @Test(expected = FileNotFoundException.class)
    public void TestException() throws ParseException, ClassNotFoundException, IOException {
        FileInputStream fInputStream = new FileInputStream("f.txt");
        model.load();
    }

    
    /** 
     * @throws ParseException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @Test(timeout = 150)
    public void TestExceptn() throws ParseException, ClassNotFoundException, IOException {
        Task first = new Task("Task #10", "Go to university", false, "High", dateFormat.parse("2022/12/12"),
                Duration.ofHours(Long.parseLong("2")));
        model.add(first.name, first.text, first.done, first.priority, first.date, first.estimate);
    }
}
