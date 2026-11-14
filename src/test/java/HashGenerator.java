package kz.iitu.springlab;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {

    public static void main(String[] args) {
        var encoder = new BCryptPasswordEncoder();
        System.out.println("user123  -> " + encoder.encode("user123"));
        System.out.println("admin123 -> " + encoder.encode("admin123"));
    }
}
