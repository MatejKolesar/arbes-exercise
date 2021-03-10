package com.phonecompany.billing;

import sun.rmi.runtime.Log;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class BillCalculator implements TelephoneBillCalculator {
    private Map<Long, ArrayList<User>> log;
    public BillCalculator() {
        this.log = new HashMap<Long, ArrayList<User>>();
    }

    @Override
    public BigDecimal calculate(String phoneLog) {
        this.fillLog(phoneLog);
        BigDecimal sum = BigDecimal.ZERO;

        for(Long key : this.log.keySet()) {
            for (User user : this.log.get(key)) {
                sum = sum.add( new BigDecimal(calculateBill(user.startTime,user.endTime)));

            }
        }
        return sum;
    }


    private static long calculateBill(Date startTime,Date endTime) {

        // Zasekol som sa s parsovanim casu pre pripad ked ide call cez den napr zacal 1.1 a skoncil 2.1 a nestihol
        // som to osetrit uz mi stacilo vypocitat duration v minutach a vratit hodnotu

        // Moj napad bol oseuknut casti hovoru ktore boli v rozhasu 8 az 16 tam vypocit normalnu cenu a zvysok pocitat
        // ako zlavu

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(startTime);
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        int startMinute = calendar.get(Calendar.MINUTE);
        int startSecond = calendar.get(Calendar.SECOND);

        String time1 = String.format("%d-%d-%d 16:00:00", calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        Date start = formatDate(time1);

        String time2 = String.format("%d-%d-%d 16:00:00", calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        Date end = formatDate(time2);




        calendar.setTime(endTime);
        int endHour = calendar.get(Calendar.HOUR_OF_DAY);
        int endMinute = calendar.get(Calendar.MINUTE);
        int endSecond = calendar.get(Calendar.SECOND);



        long tmp =0;
        long tmpDiscount =0;
        if ( 8<startHour && startHour<16 ) {
            if (endHour<16)
                tmp = startTime.getTime() - endTime.getTime();
            else {
                tmpDiscount = end.getTime() - endTime.getTime();
            }

        }
        else if (8<endHour && endHour<16){
            if (startHour<16)
                tmpDiscount = startTime.getTime() - endTime.getTime();
            else {
                tmp = end.getTime() - endTime.getTime();
            }

        }

        return tmp;

    }

    private static Date formatDate(String time) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(time);
        } catch (ParseException e) {
            return null;
        }
    }

    private void fillLog(String log) {
        String[] rows = log.split("\n");

        for(String row:rows){
            String[] tmp = row.split(",");
            Long number;
            try {
                number = Long.parseLong(tmp[0].trim());
            }
            catch (NumberFormatException e){
                number = 0L;
            }

            if(this.log.containsKey(number)) {
                ArrayList<User> tmpList = this.log.get(number);
                tmpList.add(new User(
                                number,
                                formatDate(tmp[1]),
                                formatDate(tmp[2])
                        ));
                this.log.put(number,tmpList);
            } else {
                ArrayList<User> tmpList = new ArrayList<>();
                tmpList.add(new User(
                        number,
                        formatDate(tmp[1]),
                        formatDate(tmp[2])
                ));
                this.log.put(number,tmpList);
            }

        }

        //Remove free user from list
        this.CalculateFreeUser();
    }

    private void CalculateFreeUser() {
        int bestOccurence=0;
        Long phoneNumber=0L;
        for(Long key : this.log.keySet()) {
            if(log.get(key).size()>bestOccurence) {
                bestOccurence=log.get(key).size();
                phoneNumber=key;
            }
            else if (log.get(key).size()==bestOccurence) {
                phoneNumber = phoneNumber > key ? phoneNumber: key;
            }
        }

        this.log.remove(phoneNumber);
    }





}
