package com.phonecompany.billing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvParser {
    public String parseCsv() throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader("src\\com\\phonecompany\\billing\\resources\\csvExample.csv"));
        StringBuilder sb = new StringBuilder();
        String row;
        while ((row = csvReader.readLine()) != null) {
            sb.append(row);
            sb.append("\n");
        }
        csvReader.close();
        return sb.toString();
    }
}
