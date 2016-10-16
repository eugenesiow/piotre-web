package uk.ac.soton.ldanalytics.piotre.server.user;

import com.google.common.collect.*;
import java.util.*;
import java.util.stream.*;

public class UserDao {

    private final List<User> users = ImmutableList.of(
            //        Username    Salt for hash                    Hashed password (the password is "password" for all users)
            new User("admin", "$2a$10$h.dl5J86rGH7I8bD9bZeZe", "$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO", UUID.randomUUID().toString())
    );

    public User getUserByUsername(String username) {
        return users.stream().filter(b -> b.getUsername().equals(username)).findFirst().orElse(null);
    }

    public Iterable<String> getAllUserNames() {
        return users.stream().map(User::getUsername).collect(Collectors.toList());
    }

}
