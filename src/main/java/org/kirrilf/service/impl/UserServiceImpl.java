package org.kirrilf.service.impl;

import org.kirrilf.model.Role;
import org.kirrilf.model.Status;
import org.kirrilf.model.User;
import org.kirrilf.repository.RoleRepository;
import org.kirrilf.repository.UserRepository;
import org.kirrilf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);
        user.setCreated(new Date());
        user.setUpdated(new Date());

        try {
            User registeredUser = userRepository.save(user);
            return registeredUser;
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        return result;
    }

    @Override
    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username);
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null) {
            return null;
        }

        return result;
    }

    @Override
    public boolean delete(Long id) {
        User result = userRepository.findById(id).orElse(null);
        if (result == null) {
            return false;
        }
        result.setStatus(Status.DELETED);
        result.setUpdated(new Date());
        userRepository.save(result);
        return true;
    }
}