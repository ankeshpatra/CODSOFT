package Task_5;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Main extends JFrame {
    private StudentManagementSystem sms;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    public Main() {
        sms = new StudentManagementSystem();
        setTitle("Student Management System");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"Roll Number", "Name", "Grade", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // Buttons
        JButton addButton = new JButton("Add Student");
        JButton editButton = new JButton("Edit Student");
        JButton removeButton = new JButton("Remove Student");
        JButton searchButton = new JButton("Search Student");
        JButton refreshButton = new JButton("Refresh List");
        JButton exitButton = new JButton("Exit");

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exitButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data
        refreshStudentList();

        // Button actions
        addButton.addActionListener(e -> addStudent());
        editButton.addActionListener(e -> editStudent());
        removeButton.addActionListener(e -> removeStudent());
        searchButton.addActionListener(e -> searchStudent());
        refreshButton.addActionListener(e -> refreshStudentList());
        exitButton.addActionListener(e -> {
            sms.close();
            System.exit(0);
        });
    }

    private void refreshStudentList() {
        List<Student> students = sms.getAllStudents();
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{s.getRollNumber(), s.getName(), s.getGrade(), s.getEmail()});
        }
    }

    private void addStudent() {
        StudentForm form = new StudentForm(this, "Add Student", null);
        form.setVisible(true);
        Student student = form.getStudent();
        if (student != null) {
            if (sms.addStudent(student)) {
                JOptionPane.showMessageDialog(this, "Student added successfully.");
                refreshStudentList();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add student. Roll number might already exist.");
            }
        }
    }

    private void editStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.");
            return;
        }
        int rollNumber = (int) tableModel.getValueAt(selectedRow, 0);
        Student existingStudent = sms.searchStudent(rollNumber);
        if (existingStudent == null) {
            JOptionPane.showMessageDialog(this, "Selected student not found.");
            return;
        }
        StudentForm form = new StudentForm(this, "Edit Student", existingStudent);
        form.setVisible(true);
        Student updatedStudent = form.getStudent();
        if (updatedStudent != null) {
            if (sms.updateStudent(updatedStudent)) {
                JOptionPane.showMessageDialog(this, "Student updated successfully.");
                refreshStudentList();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update student.");
            }
        }
    }

    private void removeStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to remove.");
            return;
        }
        int rollNumber = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this student?", "Confirm Remove", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (sms.removeStudent(rollNumber)) {
                JOptionPane.showMessageDialog(this, "Student removed successfully.");
                refreshStudentList();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove student.");
            }
        }
    }

    private void searchStudent() {
        String input = JOptionPane.showInputDialog(this, "Enter roll number to search:");
        if (input == null || input.trim().isEmpty()) {
            return;
        }
        int rollNumber;
        try {
            rollNumber = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid roll number format.");
            return;
        }
        Student student = sms.searchStudent(rollNumber);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found.");
        } else {
            JOptionPane.showMessageDialog(this, student.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main mainFrame = new Main();
            mainFrame.setVisible(true);
        });
    }
}

class StudentForm extends JDialog {
    private JTextField rollNumberField;
    private JTextField nameField;
    private JTextField gradeField;
    private JTextField emailField;
    private Student student;

    public StudentForm(Frame owner, String title, Student student) {
        super(owner, title, true);
        this.student = student;

        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(400, 250);
        setLocationRelativeTo(owner);

        add(new JLabel("Roll Number:"));
        rollNumberField = new JTextField();
        add(rollNumberField);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Grade:"));
        gradeField = new JTextField();
        add(gradeField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        add(saveButton);
        add(cancelButton);

        if (student != null) {
            rollNumberField.setText(String.valueOf(student.getRollNumber()));
            rollNumberField.setEditable(false);
            nameField.setText(student.getName());
            gradeField.setText(student.getGrade());
            emailField.setText(student.getEmail());
        }

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                int rollNumber = Integer.parseInt(rollNumberField.getText().trim());
                String name = nameField.getText().trim();
                String grade = gradeField.getText().trim();
                String email = emailField.getText().trim();
                this.student = new Student(rollNumber, name, grade, email);
                setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            this.student = null;
            setVisible(false);
        });
    }

    private boolean validateInput() {
        if (rollNumberField.getText().trim().isEmpty() ||
            nameField.getText().trim().isEmpty() ||
            gradeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Roll Number, Name, and Grade are required fields.");
            return false;
        }
        try {
            Integer.parseInt(rollNumberField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Roll Number must be a valid integer.");
            return false;
        }
        return true;
    }

    public Student getStudent() {
        return student;
    }
}
