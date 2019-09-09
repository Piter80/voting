package ru.zimin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.zimin.AuthorizedUser;
import ru.zimin.model.User;
import ru.zimin.repository.CrudUserRepository;
import ru.zimin.to.UserTo;

import java.util.List;

import static ru.zimin.util.UserUtil.*;
import static ru.zimin.util.ValidationUtil.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private CrudUserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public User create(User user) {
        log.info("create new user");
        Assert.notNull(user, "user must not be null");
        return repository.save(prepareToSave(user, passwordEncoder));
    }

    public void delete(int id) {
        log.info("delete user with id {}", id);
        repository.deleteById(id);
    }

    public User get(int id) {
        log.info("get user with id {}", id);
        User user = repository.findById(id).orElse(null);
        checkNotFoundWithId(user, id, User.class);
        return user;
    }

    public User getByEmail(String email) {
        log.info("get user with email {}", email);
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.findByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        log.info("get all users");
        return repository.findAll();
    }

    public User update(User user) {
        log.info("update user with id {}", user.getId());
        Assert.notNull(user, "user must not be null");
        User updated = repository.save(prepareToSave(user, passwordEncoder));
        checkNotFoundWithId(updated, user.getId(), User.class);
        return updated;
    }

    public UserTo update(UserTo userTo, int userId) {
        log.info("update userTo with id {}", userId);
        assureIdConsistent(userTo, userId);
        User user = updateFromTo(get(userTo.getId()), userTo);
        return asTo(repository.save(prepareToSave(user, passwordEncoder)));
    }

    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername email {}", email);
        User user = repository.findByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}
