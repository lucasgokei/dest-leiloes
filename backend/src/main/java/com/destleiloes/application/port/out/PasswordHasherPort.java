package com.destleiloes.application.port.out;

public interface PasswordHasherPort {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
