package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User passwordCoder(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(long id) {
        User user = null;
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            user = optional.get();
        }
        return user;
    }

    @Override
    public void save(User user) {
        userRepository.save(passwordCoder(user));
    }


public void saveOrUpdate(long id, User user) {
    String oldPassword = userRepository.getById(id).getPassword();
    if (oldPassword.equals(user.getPassword())) {
        user.setPassword(oldPassword);
        userRepository.save(user);
    } else {
        userRepository.save(user);
    }
}

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @PostConstruct
    public void addDefaultUser() {
        Set<Role> roles1 = new HashSet<>();
        roles1.add(roleRepository.findById(1L).orElse(null));
        Set<Role> roles2 = new HashSet<>();
        roles2.add(roleRepository.findById(1L).orElse(null));
        roles2.add(roleRepository.findById(2L).orElse(null));

        User user1 = new User( "admin", (byte) 75, "admin@gmail.com", "admin", "admin", roles2);
        User user2 = new User( "user", (byte) 25, "user@gmail.com", "user", "user", roles1);
        User user3 = new User( "user2", (byte) 44, "user2@gmail.com", "user2", "user", roles1);
        User user4 = new User( "user3", (byte) 36, "user3@gmail.com", "user3", "user", roles1);
        save(user1);
        save(user2);
        save(user3);
        save(user4);
    }
}

