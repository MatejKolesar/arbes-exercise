package com.phonecompany.billing;

import java.io.IOException;

public class Example {

    public static void main(String[] args){
        String csv = "";
        CsvParser parser = new CsvParser();

        try {
            csv = parser.parseCsv();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BillCalculator bill = new BillCalculator();
        bill.calculate(csv);

    }
}
