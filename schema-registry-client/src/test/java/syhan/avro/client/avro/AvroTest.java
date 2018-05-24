package syhan.avro.client.avro;

import com.google.gson.Gson;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.junit.Assert;
import org.junit.Test;
import syhan.avro.client.rest.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AvroTest {
    //

    @Test
    public void testObjectToBinaryToObject() throws Exception {
        // Object -> Binary Data -> Object
        User user = new User("홍길동", "hong@mail.com", 24);
        user.setUserType(UserType.Teacher);
        user.setBirthDay(new Date());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Schema schema = ReflectData.AllowNull.get().getSchema(User.class);

        // encode
        AvroUtil.encode(out, user, schema);

        byte[] bytes = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        // decode
        User result = AvroUtil.decode(in, schema, new User());

        Assert.assertEquals(user.getName(), result.getName());
        System.out.println(new Gson().toJson(user));
    }

    @Test
    public void testObjectListToBinaryToObjectList() throws Exception {
        // Object -> Binary Data -> Object

        // Source Object
        List<User> sourceUsers = createUsers();
        // OutputStream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // Schema
        Schema schema = ReflectData.get().getSchema(User.class);

        // Avro encode --------------------------------------------
        AvroUtil.encode(out, sourceUsers, schema);
        //---------------------------------------------------------

        // InputStream
        byte[] bytes = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        // Avro decode --------------------------------------------
        List<User> targetUsers = AvroUtil.decodeList(in, schema, () -> new User());
        //---------------------------------------------------------

        Assert.assertEquals(sourceUsers.size(), targetUsers.size());
        Assert.assertEquals(sourceUsers.get(0).getName(), targetUsers.get(0).getName());
        Assert.assertEquals(sourceUsers.get(1).getName(), targetUsers.get(1).getName());
    }

    @Test
    public void testComplexObjectToBinaryToObject() throws Exception {
        //
        School school = createSchool();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Schema schema = ReflectData.AllowNull.get().getSchema(School.class);

        AvroUtil.encode(out, school, schema);

        byte[] bytes = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        School targetSchool = AvroUtil.decode(in, schema, new School());

        Gson gson = new Gson();
        System.out.println(gson.toJson(targetSchool));
    }

    @Test
    public void testComplexObjectToBinaryToObjectWithNull() throws Exception {
        //
        School school = createSchool();
        school.setAddress(null);
        school.setStudents(null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Schema schema = ReflectData.AllowNull.get().getSchema(School.class);

        AvroUtil.encode(out, school, schema);

        byte[] bytes = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        School targetSchool = AvroUtil.decode(in, schema, new School());

        Gson gson = new Gson();
        System.out.println(gson.toJson(targetSchool));
    }

    @Test
    public void testBinary() throws Exception {
        //
        UserSdo userSdo = new UserSdo();
        userSdo.setAge(17);
        userSdo.setEmail("test@app.com");

        Schema schema = ReflectData.get().getSchema(UserSdo.class);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        AvroUtil.encode(out, userSdo, schema);

        byte[] bytes = out.toByteArray();
        writeBytesToFileNio(bytes, "userSdo.avro");
    }

    private static void writeBytesToFileNio(byte[] bFile, String fileDest) {
        //
        try {
            Path path = Paths.get(fileDest);
            Files.write(path, bFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private School createSchool() {
        //
        School school = new School("코딩 스쿨", 2018);
        school.setAddress(new Address("123-678", "세종로 1가", "36번지"));
        school.setStudents(createUsers());
        return school;
    }

    private List<User> createUsers() {
        //
        List<User> users = new ArrayList<>();
        users.add(new User("홍길동", "hong@mail.com", 24, UserType.Student));
        users.add(new User("김유신", "kim@mail.com", 55, UserType.Teacher));
        return users;
    }
}
