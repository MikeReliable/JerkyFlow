package com.company.JerkyFlowDetecting;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JerkyFinder {

    private List<String> lines;
    private double deformationStart, deformationEnd, stressStart, stressBeforeStart, stressAfterStart, stressEnd;
    private double stressDrop, stressDropMax = 0, deformationDuration, actionTime;
    private int rows;
    private String drop = " ", dropRef = " ";

//    private double k = 0.01; // начальное значение итервала stressDrop
//    private double m = 50.0; // конечное значение итервала stressDrop

//    FileScanner fileScanner = new FileScanner();
//    private List<String> lines = fileScanner.fileScanner();

    public void jerkyFinder(List<String> lines) throws IOException {
        this.lines = lines;

        FileWriter fileWriter = new FileWriter("src/main/java/com/company/JerkyFlowDetecting/Answer.txt");
        fileWriter.append("time,s\tstrain,%\tstress,MPa\tstressDrop,MPa\tstrainDropLength\n");


        for (int i = 1; i < lines.size() - 1; i++) {
            String rowBeforeStart = lines.get(i - 1);
            String rowStart = lines.get(i);
            String rowAfterStart = lines.get(i + 1);
            // поиск точек максимумов и минимумов на диаграмме
            String[] valuesBeforeStart = rowBeforeStart.split("\t");
            String[] valuesStart = rowStart.split("\t");
            String[] valuesAfterStart = rowAfterStart.split("\t");
            actionTime = Double.parseDouble(valuesStart[0]);
            deformationStart = Double.parseDouble(valuesStart[1]);
            stressBeforeStart = Double.parseDouble(valuesBeforeStart[2]);
            stressStart = Double.parseDouble(valuesStart[2]);
            stressAfterStart = Double.parseDouble(valuesAfterStart[2]);

            if (stressStart - stressAfterStart > 0.1) {
                for (int j = i; j < lines.size() - 1; j++) {
                    String row = lines.get(j);
                    String rowthen = lines.get(j + 1);
                    String[] values = row.split("\t");
                    String[] valuesthen = rowthen.split("\t");
                    stressAfterStart = Double.parseDouble(values[2]);
                    double stressAfterStartThen = Double.parseDouble(valuesthen[2]);
                    if (stressAfterStart - stressAfterStartThen > 0) {
                        deformationEnd = Double.parseDouble(valuesthen[1]);
                        stressEnd = Double.parseDouble(valuesthen[2]);
                    } else {
                        if (stressStart - stressEnd > 0.3) {
                            stressDropMax = stressStart - stressEnd;
                            deformationDuration = deformationEnd - deformationStart;
                            String text = (actionTime + "\t" + deformationStart + "\t" + stressStart + "\t" + stressDropMax + "\t" + deformationDuration + "\n");
                            fileWriter.append(text);
                        }
                        i = j;
                        j = lines.size() - 1;
                    }
                }
            }
            fileWriter.flush();

//
//            if (stressBeforeStart < stressStart && stressStart > stressAfterStart ||
//                    stressBeforeStart > stressStart && stressStart < stressAfterStart) {
////            System.out.println("I:" + i + " " + deformationStart + " " + stressStart);
//
//                List<String> prelim = new ArrayList<>();
//                for (int j = i + 1; j < lines.size() - 1; j++) {
//                    String row = lines.get(j);
//                    String[] values = row.split("\t");
//                    deformationEnd = Double.parseDouble(values[1]);
//                    stressEnd = Double.parseDouble(values[2]);
////                System.out.println("J:" + j + " " + deformationEnd + " " + stressEnd);
//                    if ((deformationEnd - deformationStart) <= 0.01 && (deformationEnd - deformationStart) > 0) {
//                        stressDrop = Math.abs(stressEnd - stressStart);
//                        if (Math.abs(stressEnd - stressStart) <= m && Math.abs(stressEnd - stressStart) >= k && stressDrop > stressDropMax) {
//                            stressDropMax = stressDrop;
//                            deformationDuration = deformationEnd - deformationStart;
//                            prelim.add(actionTime + "\t" + deformationStart + "\t" + stressStart + "\t" + stressDropMax + "\t" + deformationDuration + "\n");
//                            drop = prelim.get(prelim.size() - 1);
//
//                        }
//                    } else break;
//
//                    stressDropMax = 0;
//                }
//                if (!drop.equals(dropRef)) {
//                    dropRef = drop;
//                    System.out.println(dropRef);
//                    String text = dropRef;
//                    fileWriter.append(text);
//                    fileWriter.flush();
//                }
//            }
        }
    }
}
