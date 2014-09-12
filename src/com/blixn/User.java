package com.blixn;

/**
 * Created by emini on 12/09/14.
 * Copyright and stuff, you know!
 */
public class User {

    private String alias;
    private String realName;

    public User(String alias, String realName) {

        if (realName.equals(""))
            realName = "Anonymous";

        this.alias = alias;
        this.realName = realName;
    }

    public String getAlias() {
        return alias;
    }

    public String getRealName() {
        return realName;
    }

    public String toString() {
        return realName.equals("Anonymous") ? alias + " (Anonymous)" : realName + ", also known as " + alias + ".";
    }
}
