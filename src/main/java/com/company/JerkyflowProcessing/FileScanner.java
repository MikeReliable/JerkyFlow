package com.company.JerkyflowProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {

    private List<String> lines;
    BufferedReader reader;

    public List<String> fileScanner() {
        try {
            reader = new BufferedReader(new FileReader("src/main/java/com/company/JerkyFlowDetecting/Question.txt"));
            lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace(',', '.');
                if (!line.contains("--")) {
                    lines.add(line);
                    lines.removeIf(item -> item == null || "".equals(item));
//                        System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
