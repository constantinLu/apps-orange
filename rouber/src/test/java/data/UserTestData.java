package data;


import com.orange.rouber.model.User;

import java.util.List;
import java.util.Random;


public interface UserTestData {

    default User.UserBuilder aUser() {
        return User.builder()
                .id(new Random().nextLong())
                .address("Stefan cel Mare")
                .name("UserName")
                .email("user@gmail.com")
                .phoneNumber(75324234L)
                .trips(List.of());
    }
}
