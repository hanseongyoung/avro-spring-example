package syhan.avro.client.rest;

import java.util.ArrayList;
import java.util.List;

public class School {
    //
    private String name;
    private long year;
    private Address address;
    private List<User> students;

    public School() {
    }

    public School(String name, long year) {
        this.name = name;
        this.year = year;
        this.students = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
