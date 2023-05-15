/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ukolnicek;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class TodoListGUI extends JFrame implements ActionListener {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ArrayList<Task> tasks = new ArrayList<>();
    private JList<Task> taskList = new JList<>();
    private JButton addButton = new JButton("Add");
    private JButton removeButton = new JButton("Remove");
public static void main(String[] args) {
    TodoListGUI todoListGUI = new TodoListGUI();
    todoListGUI.setVisible(true);
}
    public TodoListGUI() {
        setTitle("To-Do List");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Load tasks from file
        tasks = loadTasksFromFile();

        // Sort tasks by date
        Collections.sort(tasks);

        // Set up task list
        taskList.setModel(new DefaultListModel<>());
        for (Task task : tasks) {
            ((DefaultListModel<Task>) taskList.getModel()).addElement(task);
        }

        // Set up add button
        addButton.addActionListener(this);

        // Set up remove button
        removeButton.addActionListener(this);

        // Set up layout
        setLayout(new BorderLayout());
        add(new JScrollPane(taskList), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            // Prompt user for task details
            JTextField nameField = new JTextField();
            JTextArea bodyArea = new JTextArea();
            bodyArea.setLineWrap(true);
            bodyArea.setWrapStyleWord(true);
            Object[] fields = {"Name:", nameField, "Body:", new JScrollPane(bodyArea)};
            int result = JOptionPane.showConfirmDialog(this, fields, "Add Task", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                // Add new task
                Task task = new Task(nameField.getText(), bodyArea.getText(), new Date());
                tasks.add(task);

                // Sort tasks by date
                Collections.sort(tasks);

                // Update task list
                ((DefaultListModel<Task>) taskList.getModel()).addElement(task);

                // Save tasks to file
                saveTasksToFile(tasks);
            }
        } else if (e.getSource() == removeButton) {
            // Remove selected task
            int index = taskList.getSelectedIndex();
            if (index >= 0) {
                tasks.remove(index);

                // Update task list
                ((DefaultListModel<Task>) taskList.getModel()).remove(index);

                // Save tasks to file
                saveTasksToFile(tasks);
            }
        }
    }

    private ArrayList<Task> loadTasksFromFile() {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String name = line;
                String body = reader.readLine();
                Date date = DATE_FORMAT.parse(reader.readLine());
                tasks.add(new Task(name, body, date));
            }
            reader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    private void saveTasksToFile(ArrayList<Task> tasks) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"));
            for (Task task : tasks) {
                writer.write(task.getName());
                writer.newLine();
                writer.write(task.getBody());
                writer.newLine();
                writer.write(DATE_FORMAT.format(task.getDate()));
                writer.newLine();         }
        writer.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private static class Task implements Comparable<Task> {
private String name;
    private String body;
    private Date date;

    public Task(String name, String body, Date date) {
        this.name = name;
        this.body = body;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return name + " - " + body + " (" + DATE_FORMAT.format(date) + ")";
    }

    @Override
    public int compareTo(Task other) {
        return date.compareTo(other.date);
}
}
}