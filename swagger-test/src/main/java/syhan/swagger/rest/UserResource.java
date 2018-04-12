package syhan.swagger.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {
    //
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "saveUser", produces = {"application/json", "application/avro"})
    public User saveUser(@RequestBody UserSdo userSdo) {
        //
        logger.debug(userSdo.toString());
        return new User("홍길동", userSdo.getEmail(), userSdo.getAge());
    }

}
