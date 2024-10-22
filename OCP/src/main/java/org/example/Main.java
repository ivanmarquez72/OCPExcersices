package org.example;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        exam4();

    }

    private static void threeDigit(Optional<Integer> optional) {
        if (optional.isPresent()) { // outer if
            Integer num = optional.get();
            String string = "" + num;
            if (string.length() == 3) // inner if
                System.out.println(string);
        }
    }

    private static void threeDigit2(Optional<Integer> optional) {
        optional.map(n -> "" + n) // part 1
                .filter(s -> s.length() == 3) // part 2
                .ifPresent(System.out::println); // part 3
    }

    private static void exam4(){
        Stream<String> streamp = Stream.iterate("", (s) -> s + "1").limit(2).map(x -> x +"2");
        Stream.iterate("", (s) -> s + "*").
                //peek(System.out::println).
                limit(2).
                //peek(System.out::println).
                map(x -> x +"2").
                forEach(System.out::print);

        Predicate<? super String> predicate = s -> s.length() > 3;
        Stream<String> stream = Stream.iterate("-", (s) -> s + s);
        //boolean b1 = stream.noneMatch(predicate);
        boolean b2 = stream.anyMatch(predicate);
        System.out.println(
                " " + b2);

        //Stream.iterate(1, x -> x++).limit(5).map(x -> x).collect(Collectors.
        //        joining());

        System.out.println(Stream.
                iterate(1, x -> x++).
                limit(5).
                map(x -> "" + x).
                collect(Collectors.joining()));

        System.out.println(Stream.
                iterate(1, x -> ++x).
                limit(5).
                map(x -> "" + x).
                collect(Collectors.joining()));

        System.out.println("**********");

        List<Integer> l1 = Arrays.asList(1, 2, 3);
        List<Integer> l2 = Arrays.asList(4, 5, 6);
        List<Integer> l3 = Arrays.asList();
        Stream.of(l1, l2, l3)
                //.peek(System.out::println)
                //.map(x -> x.add(123))
                .flatMap(x -> x.stream())
                .forEach(System.out::print);


        Stream<Integer> s1 = Stream.of(1);
        DoubleStream ds = s1.mapToDouble(x -> x);
    }

    private static void collectExample(){
        Stream<String> ohMy = Stream.of("tigers","lions","bears");
        //String result = ohMy.collect(Collectors.joining(","));
        //System.out.println("result = " + result);

        Double result1 = ohMy.collect(Collectors.averagingInt(String::length));
        System.out.println("result1 = " + result1);


        // Creating an IntStream
        IntStream stream = IntStream.of(4, 5, 6, 7);

        // Using IntStream summaryStatistics()
        IntSummaryStatistics summary_data =
                stream.summaryStatistics();

        // Displaying the various summary data
        // about the elements of the stream
        System.out.println(summary_data);

        Stream<String> stream4 = Stream.iterate("", (s) -> s + "1");
        System.out.println(stream4.limit(2).map(x -> x + "2"));
    }

}

