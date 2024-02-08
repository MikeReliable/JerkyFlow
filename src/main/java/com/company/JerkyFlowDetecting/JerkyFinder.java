package com.company.JerkyFlowDetecting;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JerkyFinder {

    private double deformationEnd;
    private double stressEnd;

    public JerkyFinder() {
    }

    public void jerkyFinder(List<String> lines) throws IOException {

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
            double actionTime = Double.parseDouble(valuesStart[0]);
            double deformationStart = Double.parseDouble(valuesStart[1]);
            double stressBeforeStart = Double.parseDouble(valuesBeforeStart[2]);
            double stressStart = Double.parseDouble(valuesStart[2]);
            double stressAfterStart = Double.parseDouble(valuesAfterStart[2]);

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
                            double stressDropMax = stressStart - stressEnd;
                            double deformationDuration = deformationEnd - deformationStart;
                            String text = (actionTime + "\t" + deformationStart + "\t" + stressStart + "\t" + stressDropMax + "\t" + deformationDuration + "\n");
                            fileWriter.append(text);
                        }
                        i = j;
                        j = lines.size() - 1;
                    }
                }
            }
            fileWriter.flush();
        }
    }
}
