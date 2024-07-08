package com.zerobase.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aes256UtilTest {

    @Test
    void encrypt() {
        String encrypt = Aes256Util.encrypt("Hello World");
        assertEquals(Aes256Util.decrypt(encrypt), "Hello World");

    }

    @Test
    void decrypt() {
    }
}