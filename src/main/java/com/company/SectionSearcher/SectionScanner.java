package com.company.SectionSearcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SectionScanner {

    private int count = 0;
    private double stressMax = 0, strainMax = 0;
    private double result, x1, x2, x3;
    private String row1, row2, row3;
    private String[] values1, values2, values3;

    BufferedReader reader;
    FileWriter fw;
    ArrayList<String> lines, buffer;

    public void sectionScanner() {

        try {
            reader = new BufferedReader(new FileReader("src/main/java/com/company/SectionSearcher/QuestionForSection.txt"));
            fw = new FileWriter("src/main/java/com/company/SectionSearcher/AnswerForSection.txt");
            lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            for (int i = 0; i < lines.size() - 1; i++) {
                row1 = lines.get(i);
                row2 = lines.get(i + 1);
                values1 = row1.split("\t");
                values2 = row2.split("\t");

//                x1 = Double.parseDouble(values1[1]); // division by strain
//                x2 = Double.parseDouble(values2[1]);
//                result = x2 - x1;
//                if (x1 > strainMax) {
//                    if (result > 0) {
//                        fw.append(row1 + "\n");
//                    } else {
//                        strainMax = x1;
//                        fw.append("\t" + "\n");
//                        fw.append("\t" + "\n");
//                        fw.append("\t" + "\n");
//                        fw.append("\t" + "\n");
//                        fw.append("\t" + "\n");
//                        fw.append("\t" + "\n");
//                        fw.append("\t" + "\n");
//                        fw.append("\t" + "\n");
//                        fw.append("\t" + "\n");
//                        fw.append("\t" + "\n");
//                    }
//                } // end division by strain

                x1 = Double.parseDouble(values1[2]); // division by stress
                x2 = Double.parseDouble(values2[2]);
                result = x2 - x1;
//                System.out.println(result + "\t" + x1 + "\t" + count + "\t" + stressMax);
                if (x1 > stressMax) {
                    if (result < 0) {
                        fw.append(row1 + "\n");
//                        buffer.add(row1);
                        count++;
                        row3 = lines.get(i - count);
                        values3 = row3.split("\t");
                        x3 = Double.parseDouble(values3[2]);
                        if (count > 5 && x3 - x1 > 5) {
                            stressMax = x3;
//                            fw.append("\t" + "\n");
//                            fw.append("\t" + "\n");
//                            fw.append("\t" + "\n");
//                            fw.append("\t" + "\n");
//                            fw.append("\t" + "\n");
//                            fw.append("\t" + "\n");
//                            fw.append("\t" + "\n");
//                            fw.append("\t" + "\n");
//                            fw.append("\t" + "\n");
//                            fw.append("\t" + "\n");
//                            System.out.println(buffer);
//                            buffer.clear();
                        }
                    } else {
//                        if (buffer != null) {
//                            for (String b : buffer) {
//                                fw.append(b);
//                            }
//                            buffer.clear();
//                        }
                        fw.append(row1 + "\n");
                        count = 0;
                        stressMax = 0;
                    }
                } // end division by stress


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
