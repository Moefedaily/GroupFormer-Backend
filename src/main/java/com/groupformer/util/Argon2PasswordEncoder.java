package com.groupformer.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Component;

@Component
public class Argon2PasswordEncoder {

    private final Argon2 argon2;

    public Argon2PasswordEncoder() {
        this.argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    }


    public String encode(String password) {
        try {

            return argon2.hash(3, 65536, 1, password.toCharArray());
        } finally {
            argon2.wipeArray(password.toCharArray());
        }
    }

    public boolean matches(String password, String hashedPassword) {
        try {
            return argon2.verify(hashedPassword, password.toCharArray());
        } finally {
            argon2.wipeArray(password.toCharArray());
        }
    }
}