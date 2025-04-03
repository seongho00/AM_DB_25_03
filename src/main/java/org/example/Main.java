package org.example;

import org.example.exception.SQLErrorException;

public class Main {
    public static void main(String[] args) {

        try {
            new App().run();
        } catch (SQLErrorException e) {
            System.err.println(e.getOrigin());
        }
    }
}