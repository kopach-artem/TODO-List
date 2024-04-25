package src;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {
    
    /** 
     * @param a
     * @param b
     * @return int
     */
    @Override
    public int compare(Task a, Task b) {
        if (a.done.equals(b.done) == false) {
            return a.done.compareTo(b.done);
        } else {
            if (a.date.equals(b.date) == false) {
                return a.date.compareTo(b.date);
            } else {
                if (a.priority.equals(b.priority) == false) {
                    if (a.priority.equals("High")) {
                        return -1;
                    } else if (a.priority.equals("Low")) {
                        return 1;
                    } else {
                        if (b.priority.equals("High")) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                } else {
                    if (a.estimate.equals(b.estimate) == false) {
                        return a.estimate.compareTo(b.estimate);
                    } else {
                        return 0;
                    }
                }
            }
        }
    }
}
