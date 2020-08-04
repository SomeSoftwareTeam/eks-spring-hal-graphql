package com.somesoftwareteam.graphql.datasources.mysql.acl;

public class Entry {

    public String sid;
    public String permission;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
