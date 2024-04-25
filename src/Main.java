package src;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

class Main {
    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("TODOList");
        frame.setSize(950, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Model model = new Model();
        final DefaultListModel<String> taskListModel = new DefaultListModel<>();
        final JList<String> taskList = new JList<>(taskListModel);
        taskList.setPreferredSize(new Dimension(200, 500));
        JButton addButton = new JButton("Add");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        JButton deleteListButton = new JButton("Delete List");
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        JButton clearButton = new JButton("Clear");
        JButton helpButton = new JButton("Help");
        JPanel panel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel panel1 = new JPanel();
        panel1.setSize(30, 30);
        JTextField taskName = new JTextField();
        JTextArea textArea = new JTextArea(60, 20);
        textArea.setLineWrap(true);
        JCheckBox checkBox = new JCheckBox("Done:");
        checkBox.setHorizontalTextPosition(SwingConstants.LEFT);
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        JTextField textDate = new JTextField();
        textDate.setText(dateFormat.format(new Date()));
        JLabel textDateTitle = new JLabel("Deadline");
        JLabel textEstimateTitle = new JLabel("Estimate hours:");
        textDate.setPreferredSize(new Dimension(120, 25));
        JTextField textEstimate = new JTextField();
        textEstimate.setPreferredSize(new Dimension(120, 25));
        String[] priorities = { "High", "Medium", "Low" };
        JComboBox<String> comboBoxPriority = new JComboBox<>(priorities);
        comboBoxPriority.setSelectedItem("Medium");
        textArea.setPreferredSize(new Dimension(300, 300));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (taskName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Task name can NOT be EMPTY");
                    return;
                }
                Date date;
                try {
                    date = dateFormat.parse(textDate.getText());
                    if (!dateFormat.format(date).equals(textDate.getText())) {
                        date = null;
                    }
                } catch (ParseException p) {
                    date = null;
                }
                if (date == null) {
                    JOptionPane.showMessageDialog(null, "Invalid date");
                    return;
                }
                Duration estimate;
                try {
                    estimate = Duration.ofHours(Long.parseLong(textEstimate.getText()));
                } catch (NumberFormatException f) {
                    JOptionPane.showMessageDialog(null, "Invalid estimate");
                    return;
                }
                String priority = String.valueOf(comboBoxPriority.getSelectedItem());
                model.add(taskName.getText(), textArea.getText(), checkBox.isSelected(), priority, date, estimate);
                ArrayList<Task> tasks = model.getTasks();
                taskListModel.clear();
                for (Task task : tasks) {
                    taskListModel.addElement(task.name);
                }
                textArea.setText("");
                taskName.setText("");
                checkBox.setSelected(false);
                comboBoxPriority.setSelectedItem("Medium");
                textEstimate.setText("");
                textDate.setText(dateFormat.format(new Date()));
                taskList.clearSelection();
                panel.revalidate();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                taskName.setText("");
                checkBox.setSelected(false);
                comboBoxPriority.setSelectedItem("Medium");
                textEstimate.setText("");
                textDate.setText(dateFormat.format(new Date()));
                taskList.clearSelection();
                panel.revalidate();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    model.save();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<Task> tasks = model.load();
                    taskListModel.clear();
                    for (Task task : tasks) {
                        taskListModel.addElement(task.name);
                    }
                    panel.revalidate();
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null, ioe.toString());
                } catch (ClassNotFoundException c) {
                    JOptionPane.showMessageDialog(null, c.toString());
                }
            }
        });
        deleteListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Are u sure that u would like to delete all list?",
                        "confirm", JOptionPane.YES_NO_CANCEL_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    taskListModel.clear();
                    // taskList.setModel(taskListModel);
                    checkBox.setSelected(false);
                    panel.revalidate();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idx = taskList.getSelectedIndex();
                String value = taskList.getSelectedValue();
                taskListModel.removeElementAt(idx);
                model.delete(value);
                panel.revalidate();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (taskList.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "You should select a task");
                    return;
                }
                if (taskName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Task name can NOT be EMPTY");
                    return;
                }
                Date date;
                try {
                    date = dateFormat.parse(textDate.getText());
                    if (!dateFormat.format(date).equals(textDate.getText())) {
                        date = null;
                    }
                } catch (ParseException p) {
                    date = null;
                }
                if (date == null) {
                    JOptionPane.showMessageDialog(null, "Invalid date");
                    textDate.setText(dateFormat.format(model.getTask(taskList.getSelectedValue()).date));
                    return;
                }
                Duration estimate;
                try {
                    estimate = Duration.ofHours(Long.parseLong(textEstimate.getText()));
                } catch (NumberFormatException f) {
                    JOptionPane.showMessageDialog(null, "Invalid estimate");
                    return;
                }
                int idx = taskList.getSelectedIndex();
                String oldName = taskList.getSelectedValue();
                taskListModel.setElementAt(taskName.getText(), idx);
                String priority = String.valueOf(comboBoxPriority.getSelectedItem());
                ArrayList<Task> tasks = model.update(oldName, taskName.getText(), textArea.getText(),
                        checkBox.isSelected(), priority, date,
                        estimate);
                taskListModel.clear();
                for (Task task : tasks) {
                    taskListModel.addElement(task.name);
                }
                panel.revalidate();
            }
        });
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String information = "Main information about buttons: \n-Add: add new task with all possibilities \n-Save: save to taskList.bin file all your task in List \n-Load: load your task from .bin file \n-Delete List: delete task List (,but your tasks in file save) \n-Delete: delete selected task \n-Update: with this button you can update your done, priority or date \n-Clear: clear your display \nPlease click OK to continue";
                JOptionPane.showMessageDialog(null, information);
            }
        });
        taskList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                try {
                    if (!event.getValueIsAdjusting()) {
                        String key = taskList.getSelectedValue();
                        Task task = model.getTask(key);
                        textArea.setText(task.text);
                        taskName.setText(key);
                        checkBox.setSelected(task.done);
                        textDate.setText(dateFormat.format(task.date));
                        comboBoxPriority.setSelectedItem(task.priority);
                        textEstimate.setText(Long.toString(task.estimate.toHours()));
                    }
                } catch (NullPointerException e) {
                    taskName.setText("");
                    textArea.setText("");
                }
            }       
        });
        // checkBox.addItemListener(new ItemListener() {
        // public void itemStateChanged(ItemEvent e) {
        // if(mapList.containsKey(taskList.getSelectedValue())){
        // mapList.get(taskList.getSelectedValue()).done = checkBox.isSelected();
        // }
        // }
        // });
        // comboBoxPriority.addItemListener(new ItemListener() {
        // public void itemStateChanged(ItemEvent e) {
        // if(mapList.containsKey(taskList.getSelectedValue())){
        // String priority = String.valueOf(comboBoxPriority.getSelectedItem());
        // mapList.get(taskList.getSelectedValue()).priority = priority;
        // }
        // }
        // });
        panel.setLayout(new BorderLayout());
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel.add(taskName, BorderLayout.NORTH);
        buttonPanel.add(checkBox);
        buttonPanel.add(textDateTitle);
        buttonPanel.add(textDate);
        buttonPanel.add(textEstimateTitle);
        buttonPanel.add(textEstimate);
        buttonPanel.add(comboBoxPriority);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        panel1.add(new JLabel(" "));
        panel1.add(clearButton);
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1.add(new JLabel(" "));
        panel1.add(deleteButton);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1.add(new JLabel(" "));
        panel1.add(deleteListButton);
        deleteListButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1.add(new JLabel("  "));
        panel1.add(helpButton);
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(taskList, BorderLayout.EAST);
        panel.add(areaScrollPane, BorderLayout.CENTER);
        panel.add(panel1, BorderLayout.WEST);
        frame.add(panel);
        frame.setVisible(true);
    }
}
