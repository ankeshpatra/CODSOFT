package Task_5;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystem {
    private Connection connection;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";

    private static final String DB_USER = "root";
   
    private static final String DB_PASSWORD = "root123";

    public StudentManagementSystem() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "rollNumber INT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "grade VARCHAR(10) NOT NULL," +
                "email VARCHAR(100)" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (rollNumber, name, grade, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, student.getRollNumber());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getGrade());
            pstmt.setString(4, student.getEmail());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeStudent(int rollNumber) {
        String sql = "DELETE FROM students WHERE rollNumber = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, rollNumber);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Student searchStudent(int rollNumber) {
        String sql = "SELECT * FROM students WHERE rollNumber = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, rollNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("rollNumber"),
                        rs.getString("name"),
                        rs.getString("grade"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("rollNumber"),
                        rs.getString("name"),
                        rs.getString("grade"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, grade = ?, email = ? WHERE rollNumber = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getGrade());
            pstmt.setString(3, student.getEmail());
            pstmt.setInt(4, student.getRollNumber());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
