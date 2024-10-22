package org.example.exceptions;

public class Assertions {
    public static void main(String[] args) {
        int numGuests = -5;
        //assert numGuests > 0;
        System.out.println(numGuests);

        test(Seasons.WINTER);
    }

    public static void test(Seasons s) {
        switch (s) {
            case SPRING:
            case FALL:
                System.out.println("Shorter hours");
                break;
            case SUMMER:
                System.out.println("Longer hours");
                break;
            default:
                assert false: "Invalid season";
        }
    }
}

enum Seasons {
    SPRING, SUMMER, FALL, WINTER
}
