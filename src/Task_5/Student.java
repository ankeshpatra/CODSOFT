package Task_5;

public class Student {
    private int rollNumber;
    private String name;
    private String grade;
    private String email; // additional relevant detail

    public Student(int rollNumber, String name, String grade, String email) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.grade = grade;
        this.email = email;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "rollNumber=" + rollNumber +
                ", name='" + name + '\'' +
                ", grade='" + grade + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
