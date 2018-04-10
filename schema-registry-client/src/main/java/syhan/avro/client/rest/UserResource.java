package syhan.avro.client.rest;

import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.web.bind.annotation.*;
import syhan.avro.client.avro.SchemaManager;

@RestController
public class UserResource {
    //
    private Logger logger = LoggerFactory.getLogger(getClass());

    private SchemaManager schemaManager;

    @Autowired
    public UserResource (SchemaRegistryClient schemaRegistryClient) {
        //
        this.schemaManager = new SchemaManager(schemaRegistryClient);
    }

    @RequestMapping(value = "saveUser", produces = {"application/json", "application/avro"})
    public User saveUser(@RequestBody UserSdo userSdo) {
        //
        logger.debug(userSdo.toString());
        return new User("홍길동", userSdo.getEmail(), userSdo.getAge());
    }

    @GetMapping("schema")
    public String getSchema(@RequestParam String className) throws Exception {
        //
        return schemaManager.findSchema(className);
    }

}
