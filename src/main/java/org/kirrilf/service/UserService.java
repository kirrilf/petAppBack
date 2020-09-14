package org.kirrilf.service;

import org.kirrilf.model.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    boolean delete(Long id);

    String getToken(User user);
}
