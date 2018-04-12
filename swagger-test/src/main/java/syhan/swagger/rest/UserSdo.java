package syhan.swagger.rest;

public class UserSdo {
    private int age;
    private String email;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserSdo{" +
                "age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}
