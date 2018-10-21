package server.controller.util;

import lombok.*;

public class Path {

    // The @Getter methods are needed in order to access
    // the variables from Velocity Templates
    public static final class Web {
        @Getter public static final String LOGIN = "/login/";
        @Getter public static final String LOGOUT = "/logout/";
        @Getter public static final String REGISTER = "/register/";
        @Getter public static final String REQUESTALL = "/reqall/";
        @Getter public static final String REQUESTONE = "/reqone/";
        @Getter public static final String UPLOAD = "/upload/";
        @Getter public static final String UPDATE = "/update/";
        public static final String GETSTAT = "/getstat/";
    }
}
