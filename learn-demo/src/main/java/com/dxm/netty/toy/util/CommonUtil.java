package com.dxm.netty.toy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
    public static String getNowTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.Z").format(new Date());
    }
}
