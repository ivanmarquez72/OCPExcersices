package org.example;

import javax.swing.text.DateFormatter;
import java.time.*;
import java.time.chrono.ChronoPeriod;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DateTimeLocalization {
    public static void main(String[] args) {
        dateTimeFormartter();

        String s = Duration.of(24, ChronoUnit.HOURS).toString();
        System.out.println("s = " + s);
        
        Instant instant = Instant.now();
        System.out.println("instant = " + instant);

    }


    private static void dateTimeFormartter(){
        LocalDate date = LocalDate.of(2024,Month.NOVEMBER, 20);
        LocalTime time = LocalTime.of(12,23);
        LocalDateTime dateTime = LocalDateTime.of(date,time);

        DateTimeFormatter shortF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        DateTimeFormatter mediumF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

        System.out.println(shortF.format(dateTime));
        System.out.println(mediumF.format(dateTime));

        DateTimeFormatter f = DateTimeFormatter.ofPattern("MMMM yyyy mm:ss");
        System.out.println(f.format(dateTime));

        System.out.println("################");
        f = DateTimeFormatter.ofPattern("yyyy dd MM");
        date = LocalDate.parse("2024 02 11", f);
        time = LocalTime.parse("11:22");
        System.out.println(date); // 2015–01–02
        System.out.println(time); // 11:22




    }

    private static void ResourceBundles() {

        Locale us = new Locale("en", "US");
        Locale france = new Locale("fr","FR");
        Locale englishCanada = new Locale("en","CA");
        Locale frenchCanada = new Locale("fr","CA");

        System.out.println("####################");
        printProperties(france);
        System.out.println("--------------------");
        printProperties(us);
    }

    public static void printProperties(Locale locale){
        System.out.println("locale = " + locale);
        ResourceBundle rb = ResourceBundle.getBundle("Zoo", locale);

        System.out.println();
        System.out.println(rb.getString("hello"));
        System.out.println(rb.getString("open"));

        System.out.println("#############");

        Set<String> keys =  rb.keySet();
        keys.stream().map(k -> k + " " + rb.getString(k))
                .forEach(System.out::println);

        System.out.println("///////////////////////////////");
        Properties props = new Properties();
        rb.keySet().stream()
                .forEach(k -> props.put(k, rb.getString(k)));

        System.out.println(props.getProperty("notReallyAProperty"));
        System.out.println(props.getProperty("notReallyAProperty", "123"));


    }



    private static void internationalization() {
        Locale locale = Locale.getDefault();
        System.out.println(locale);

        System.out.println(Locale.GERMAN);
        System.out.println(Locale.GERMANY);

        System.out.println("################");

        System.out.println(Locale.getDefault());
        Locale l1 = new Locale("fr");
        Locale.setDefault(l1);
        System.out.println(Locale.getDefault());

    }

    private static void dayLight() {
        LocalDate date = LocalDate.of(2016, Month.NOVEMBER, 6);
        LocalTime time = LocalTime.of(1, 30);
        ZoneId zone = ZoneId.of("US/Eastern");
        ZonedDateTime dateTime = ZonedDateTime.of(date, time, zone);
        System.out.println(dateTime); // 2016–11–06T01:30–04:00[US/Eastern]
        System.out.println(dateTime.toInstant());
        dateTime = dateTime.plusHours(1);
        System.out.println(dateTime); // 2016–11–06T01:30–05:00[US/Eastern]
        System.out.println(dateTime.toInstant());
        dateTime = dateTime.plusHours(1);
        System.out.println(dateTime); // 2016–11–06T02:30–05:00[US/Eastern]
        System.out.println(dateTime.toInstant());

        System.out.println("#############################");
        date = LocalDate.of(2016, Month.MARCH, 13);
        time = LocalTime.of(2, 30);
        zone = ZoneId.of("US/Eastern");
        dateTime = ZonedDateTime.of(date, time, zone);
        System.out.println(dateTime); // 2016–03–13T03:30–04:00[US/Eastern]

    }
    private static void instants(){
        Instant now = Instant.now();
        System.out.println("now = " + now);
        durations();
        Instant later = Instant.now();
        System.out.println("later = " + later);
        Duration duration = Duration.between(now,later);
        System.out.println(duration.toMillis());

        System.out.println("##################################");
        LocalDate date = LocalDate.of(2024,11,20);
        LocalTime time = LocalTime.of(12,23);
        ZoneId zoneId = ZoneId.of("America/Mexico_City");
        ZonedDateTime zonedDateTime =  ZonedDateTime.of(date,time,zoneId);
        Instant instant = zonedDateTime.toInstant();
        System.out.println(zonedDateTime);
        System.out.println(instant);

        System.out.println("##################################");
        instant = Instant.ofEpochSecond(1234);
        System.out.println(instant);

        System.out.println("##################################");
        Instant nextDay = instant.plus(1,ChronoUnit.DAYS);
        System.out.println("nextDay = " + nextDay);

        Instant nextHour = instant.plus(13,ChronoUnit.HOURS);
        System.out.println("nextHour = " + nextHour);

        Instant nextWeek = instant.plus(7,ChronoUnit.YEARS);
        System.out.println("nextWeek = " + nextWeek);
    }

    private static void durations(){
        LocalDate date = LocalDate.of(1999,11,20);
        LocalTime time = LocalTime.of(16,30);
        LocalDateTime dateTime = LocalDateTime.of(date,time);

        Duration duration = Duration.ofHours(6);

        System.out.println(dateTime.plus(4, ChronoUnit.HOURS));
        System.out.println(dateTime.plus(duration));    
        System.out.println(time.plus(duration));
        System.out.println(dateTime.plus(1, ChronoUnit.MONTHS));
        
        duration = Duration.ofDays(2 );
        System.out.println("duration = " + duration);
        System.out.println(time);
        System.out.println(time.plus(duration));


        date = LocalDate.of(2015, 5, 25);
        Period period = Period.ofDays(1);
        Duration days = Duration.ofDays(1);
        System.out.println(date.plus(period)); // 2015–05–26
        System.out.println(days);
        //System.out.println(date.plus(days)); // Unsupported unit: Seconds
    }

    public static void date(){
        System.out.println(LocalDate.now());
        System.out.println(LocalTime.now());
        System.out.println(LocalDateTime.now());
        System.out.println(ZonedDateTime.now());

        System.out.println(ZoneId.systemDefault());

        System.out.println("\n*************");
        ZoneId.getAvailableZoneIds().stream()
                .filter(z -> z.contains("US") || z.contains("America"))
                .sorted().forEach(System.out::println);
    }

    private static void periods(){
        LocalDate start = LocalDate.of(2015, Month.JANUARY, 1);
        LocalDate end = LocalDate.of(2015, Month.JUNE, 1);
        performAnimalEnrichment(start, end);
        System.out.println("############## PERIODS ##############");
        Period period = Period.ofMonths(1);
        performAnimalEnrichment(start,end,period);

        System.out.println("############################");

        Period wrong = Period.ofYears(1).ofWeeks(1); // every week
        System.out.println(wrong);

        wrong = Period.ofYears(1);
        System.out.println(wrong);
        wrong = Period.ofWeeks(1);
        System.out.println(wrong);

        wrong = Period.of(1,0,8);
        System.out.println(wrong);

        System.out.println("############################");
        LocalDate date = LocalDate.of(1999,11,20);
        LocalTime time = LocalTime.now();
        System.out.println(date);
        LocalDateTime dateTime = LocalDateTime.of(date,time);
        Period period1 = Period.ofMonths(2);
        System.out.println(date.plus(period1));
        System.out.println(dateTime.plus(period));
        System.out.println(time.plus(period));


    }

    private static void performAnimalEnrichment(LocalDate start, LocalDate end) {
        LocalDate upTo = start;
        while (upTo.isBefore(end)) { // check if still before end
            System.out.println("give new toy: " + upTo);
            //upTo = upTo.plusMonths(1); // add a month
            upTo = upTo.plusDays(28); // add a month
        }
    }

    private static void performAnimalEnrichment(LocalDate start, LocalDate end,
                                                Period period) { // uses the generic period
        LocalDate upTo = start;
        while (upTo.isBefore(end)) {
            System.out.println("give new toy: " + upTo);
            upTo = upTo.plus(period); // adds the period
        }
    }

    public static void transactionCode() {
        String prueba = UUID.randomUUID().toString();
        System.out.println(prueba);
        prueba = prueba.replace("-", "");
        System.out.println(prueba);
        prueba = prueba.substring(0, 6);
        System.out.println("prueba = " + prueba);

        System.out.println("####################");

        System.out.println(UUID.randomUUID());
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
        System.out.println(UUID.randomUUID().toString().replace("-", "").substring(0, 6));
    }

}






