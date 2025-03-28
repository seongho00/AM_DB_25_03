package org.example;

import java.util.Scanner;

public class Container {
    static Scanner sc;

    static void init() {
        sc = new Scanner(System.in);
    }

    static void close() {
        sc.close();
    }

    public static Scanner getSc() {
        return sc;
    }
}
