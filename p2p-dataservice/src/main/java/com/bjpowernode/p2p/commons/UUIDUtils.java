package com.bjpowernode.p2p.commons;

import java.util.UUID;

/**
 *
 */
public class UUIDUtils {
    public static String getUUID(){
        String uuid=UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(uuid);

        return uuid;
    }
}
