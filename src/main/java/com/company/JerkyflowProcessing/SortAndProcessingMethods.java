package com.company.JerkyflowProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

class SortAndProcessingMethods {

    private String line;
    private List<String> lines;
    double stressDrop, stress;
    double stressResult = 0, meanStress = 0;
    int count = 0;
    double k = 0.1; // начальное значение интервала stressDrop
    double m = 10.0; // конечное значение интервала stressDrop
    double n = 0.1; // шаг сканирования интервала stressDrop

    BufferedReader reader;
    FileWriter fileWriter, fileWriterProcessing;
    Map<String, Double> initialMap = new HashMap<>();
    Map<String, Double> sortedMap;
    Map<Double, Integer> processingMap = new TreeMap<>();


    void sorting() {
        try {
            fileWriter = new FileWriter("src/main/java/com/company/JerkyflowProcessing/SortedAnswer.txt");
            fileWriter.append("time,s\tstrain,%\tstress,MPa\tstressDrop,MPa\tstrainDropLength\n");
            reader = new BufferedReader(new FileReader("src/main/java/com/company/JerkyflowProcessing/Answer.txt"));
            lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            for (int i = 1; i < lines.size(); i++) {
                String row = lines.get(i);
                String[] values = row.split("\t");
                stressDrop = Double.parseDouble(values[3]);
                initialMap.put(row, stressDrop);
            }
            sortedMap = initialMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors
                    .toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            for (String key : sortedMap.keySet()) {
//                System.out.println(key);
                fileWriter.write(key + "\n");
            }
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TreeMap<Double, Integer> processing() throws ParseException {
        try {
            fileWriterProcessing = new FileWriter("src/main/java/com/company/JerkyflowProcessing/Processing.txt");
            reader = new BufferedReader(new FileReader("src/main/java/com/company/JerkyflowProcessing/SortedAnswer.txt"));
            lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            while (k < m) {
                for (int i = 1; i < lines.size(); i++) {
                    String row = lines.get(i);
                    String[] values = row.split("\t");
                    stress = Double.parseDouble(values[2]);
                    stressDrop = Double.parseDouble(values[3]);
//                System.out.println(stress + " " + stressDrop);
                    if (stressDrop < k + n && stressDrop >= k) {
                        count++;
                        stressResult += stress;
                        System.out.println(count + " " + stressDrop + " " + stressResult);
                    }
                }
                if (stressResult != 0) {
                    meanStress = stressResult / count;
                    DecimalFormat df = new DecimalFormat("###.#####");
                    fileWriterProcessing.write((df.format(k)).replace(",", ".") + "\t" + count
                            + "\t" + (df.format(meanStress)).replace(",", ".") + "\n");
                    processingMap.put(k, count);
                    count = 0;
                    stressResult = 0;
                }
                k += n;
            }
            fileWriterProcessing.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return (TreeMap<Double, Integer>) processingMap;
    }
}
