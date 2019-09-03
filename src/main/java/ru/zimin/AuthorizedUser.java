package ru.zimin;

import org.springframework.security.core.GrantedAuthority;
import ru.zimin.model.User;
import ru.zimin.to.UserTo;
import ru.zimin.util.UserUtil;

import java.util.Collection;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private UserTo userTo;

    public AuthorizedUser(User user){
        super(user.getEmail(), user.getPassword(), true, true, true, true, user.getRoles());
        this.userTo = UserUtil.asTo(user);
    }

    public int getId() {
        return userTo.getId();
    }

    public UserTo get() {
        return userTo;
    }

    public void update(UserTo userTo) {
        this.userTo = userTo;
    }

    @Override
    public String toString() {
        return userTo.toString();
    }
}
