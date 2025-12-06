package com.agrifarm.utils;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleUI implements AutoCloseable {
    private final Scanner scanner;
    private final PrintStream out;

    public ConsoleUI(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in);
        this.out = out;
    }

    @SuppressWarnings("java:S106")
    public ConsoleUI() {
        this(System.in, System.out);
    }

    public void println(String message) {
        out.println(message);
    }

    public void print(String message) {
        out.print(message);
    }

    public void println() {
        out.println();
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public int readIntSafe() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void close() {
        scanner.close();
    }
}
