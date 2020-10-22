package org.kirrilf.service;

import org.kirrilf.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    boolean delete(Long id);

    String getAccessToken(User user);

    String getRefreshToken(User user, HttpServletRequest req);
}
