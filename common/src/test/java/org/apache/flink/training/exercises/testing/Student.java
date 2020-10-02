package org.apache.flink.training.exercises.testing;

public class Student {

    private String name;

    private int classNo;

    private int age;

    public Student() {

    }

    public Student(String name, int classNo, int age) {
        this.name = name;
        this.classNo = classNo;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassNo() {
        return classNo;
    }

    public void setClassNo(int classNo) {
        this.classNo = classNo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
