package com.edu.feicui.news.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2016/12/2.
 */
@Entity(nameInDb = "news_users")
public class User {
    @NotNull
    @Property(nameInDb = "n_username")
    private String username;
    @NotNull
    @Property(nameInDb = "n_password")
    private String password;
    @NotNull
    private String email;

    @Generated(hash = 1546766585)
    public User(@NotNull String username, @NotNull String password,
            @NotNull String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
