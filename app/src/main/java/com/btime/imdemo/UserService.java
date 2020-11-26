package com.btime.imdemo;

public class UserService implements IUserService {
    @Override
    public String search(int hashCode) {
        return "User:"+hashCode;
    }
}
