package com.yigitkagankartal.jobtracker;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashTest {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String raw = "1234";

        String newHash = encoder.encode(raw);

        System.out.println("YENİ HASH → " + newHash);
    }
}
