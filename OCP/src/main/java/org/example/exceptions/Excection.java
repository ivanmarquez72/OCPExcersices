package org.example.exceptions;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Excection {
    public static void main(String[] args) throws CannotSwimException{
        Dolphin.swim();
    }
}

class CannotSwimException extends Exception{
    public CannotSwimException() {
        super();
    }
    public CannotSwimException(Exception e) {
        super(e);
    }
    public CannotSwimException(String message) {
        super(message);
    }
}

class DangerInTheWater extends RuntimeException{

}

class SharkInTheWaterException extends DangerInTheWater{
//284 -294
}

class Dolphin{
    public static void swim() throws CannotSwimException {
        try {
            throw new CannotSwimException();
        } catch (CannotSwimException e) {
            e.printStackTrace();
        }
        try (Scanner s = new Scanner(System.in)) {
            s.nextLine();
        } catch(Exception e) {
            //s.nextInt(); // DOES NOT COMPILE
        } finally{
            //s.nextInt(); // DOES NOT COMPILE
        }
    }
}

class JammedTurkeyCage implements AutoCloseable {
    public void close() throws IllegalStateException {
        throw new IllegalStateException("Cage door does not close");
    }
    public static void main(String[] args) {
        try (JammedTurkeyCage t = new JammedTurkeyCage()) {
            System.out.println("put turkeys in");
        } catch (IllegalStateException e) {
            System.out.println("caught: " + e.getMessage());
        }

        System.out.println("\n###################\n");

        try (JammedTurkeyCage t = new JammedTurkeyCage()) {
            throw new IllegalStateException("turkeys ran off");
        } catch (IllegalStateException e) {
            System.out.println("caught: " + e.getMessage());
            for (Throwable t: e.getSuppressed())
                System.out.println(t.getMessage());
        }

        System.out.println("\n###################\n");

        try (JammedTurkeyCage t = new JammedTurkeyCage()) {
            //throw new RuntimeException("turkeys ran off");
        } catch (IllegalStateException e) {
            System.out.println("caught: " + e.getMessage());
        }

        System.out.println("\n###################\n");

        try (JammedTurkeyCage t1 = new JammedTurkeyCage();
                JammedTurkeyCage t2 = new JammedTurkeyCage()){
            System.out.println("turkeys entered cages");
        }catch (IllegalStateException e){
            System.out.println("caught: " + e.getMessage());
            for (Throwable t: e.getSuppressed()){
                System.out.println(t.getMessage());
            }
        }

        System.out.println("\n###################\n");

        try (JammedTurkeyCage t1 = new JammedTurkeyCage()){
            throw new IllegalStateException("turkey ran off");
        }finally{
            throw new RuntimeException("and we couldn´t find them");
        }
    }
}


class Auto implements AutoCloseable {
    int num;

    Auto(int num) {
        this.num = num;
    }

    public void close() {
        System.out.println("Close: " + num);
    }

    public static void main(String[] args) throws SQLException, IOException {
        try (Auto a1 = new Auto(1); Auto a2 = new Auto(2)) {
            throw new IllegalStateException("throw");
        } catch (IllegalStateException e) {
            System.out.println("ex");
        } finally {
            System.out.println("finally");
        }

        rethrowing();
    }

    static void rethrowing() throws SQLException,
            DateTimeParseException {
        try {
            parseData();
        }
        //catch (SQLException | DateTimeParseException e) //-> do not catch the NullPointerException
        catch (Exception e)
        {
            System.err.println(e);
            throw e;
        }
    }

    static void parseData() throws SQLException, DateTimeParseException{
        throw new NullPointerException();
    }
}

//Adding IOException
class ReThrowing{
    //public void rethrowing() throws SQLException, DateTimeParseException {
    public void rethrowing() throws IOException , SQLException, DateTimeParseException {
        try {
            parseData();
        } catch (Exception e) {
            System.err.println(e);
            throw e;
        }
    }

    //public void parseData() throws IOException, SQLException, DateTimeParseException {
    public void parseData() throws IOException, DateTimeParseException {
        throw new NullPointerException();
    }

    public void multiCatch() throws IOException, SQLException, DateTimeParseException {
        try {
            parseData();
        } catch ( IOException | DateTimeParseException e) {
            System.err.println(e);
            throw e;
        } }
}
