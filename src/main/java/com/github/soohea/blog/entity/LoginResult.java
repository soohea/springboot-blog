package com.github.soohea.blog.entity;

import lombok.Data;

@Data
public class LoginResult extends Result<User> {
    Boolean isLogin;

    protected LoginResult(String status, String msg, User user, boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public static Result success (String msg,boolean isLogin){
        return new LoginResult("ok", msg, null, isLogin);
    }
    public static Result success(User user) {
        return new LoginResult("ok",null,user,true);
    }
    public static Result success(String msg,User user) {
        return new LoginResult("ok",msg,user,true);
    }

    public static Result failure(String msg){

        return new LoginResult("fail",msg,null,false);
    }


}
