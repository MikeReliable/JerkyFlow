package com.company.JerkyFlowDetecting;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        JerkyFinder jerkyFinder = new JerkyFinder();
        FileScanner fileScanner = new FileScanner();
        List<String> rows = fileScanner.fileScanner();
//        for (String row : rows) {
//            System.out.println(row);
//        }
        jerkyFinder.jerkyFinder(rows);
    }
}
