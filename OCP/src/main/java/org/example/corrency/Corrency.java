package org.example.corrency;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Corrency {
    public static void main(String[] args) {
        noExecute();
    }

    public static void example1(){
        System.out.println("begin");
        (new ReadInventoryThread()).start();
        (new Thread(new PrintData())).start();
        (new ReadInventoryThread()).start();
        System.out.println("end");
    }

    public static void noExecute(){
        new PrintData().run();
        (new Thread(new PrintData())).run();
        (new ReadInventoryThread()).run();
    }

}

class PrintData implements Runnable{
    public void run() {
        for (int i = 0; i < 3; i++)
            System.out.println("Printing record: " + i );

    }

    public static void main(String[] args) {
        (new Thread(new PrintData())).start();
    }
}

class ReadInventoryThread extends Thread{
    public void run() {
        System.out.println("Printing zoo inventory");
    }

    public static void main(String[] args) {
        (new Thread(new ReadInventoryThread())).start();
    }
}

class CheckResults {
    private static int counter = 0;
    public static void mainCompare(String[] args) {
        new Thread(() -> {
            for(int i=0; i<500; i++) {
                CheckResults.counter++;
            }
        }).start();
        while(CheckResults.counter<100) {
            System.out.println("Not reached yet");
        }
        System.out.println("Reached!");
    }

    public static void main(String[] args)  throws InterruptedException,
            ExecutionException {
        schedule();
    }

    public static void executor() throws InterruptedException{
        ExecutorService service = null;
        try {
            service = Executors.newSingleThreadExecutor();
            System.out.println("begin");
            service.execute(() -> System.out.println("Printing zoo inventory"));

            service.execute(() -> {for(int i=0; i<3; i++)
                System.out.println("Printing record: "+i);}
            );

            service.execute(() -> System.out.println("Printing zoo inventory"));

            System.out.println("end");
        } finally {
            if(service != null) service.shutdown();
        }
    }

    public static void executor2()  throws InterruptedException,
            ExecutionException {
        ExecutorService service = null;
        try {
            service = Executors.newSingleThreadExecutor();
            Future<?> result = service.submit(() -> {
                for(int i=0; i<10000; i++) CheckResults.counter++;
                try {
                    Thread.sleep(70000); // 1 SECOND
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return 5;
            });
            System.out.println(result.get());
            result.get(1, TimeUnit.SECONDS);
            System.out.println("Reached!");
        } catch (TimeoutException e) {
            System.out.println("Not reached in time");
            System.err.println(e);
        } finally {
            if(service != null) service.shutdown();
        }
    }

    public static void threads() throws InterruptedException{
        new Thread(() -> {
            for(int i=0; i<500; i++) {
                CheckResults.counter++;
                //System.out.println("waiting");
            }
        }).start();
        while(CheckResults.counter<100) {
            System.out.println("Not reached yet");
            Thread.sleep(100); // 1 SECOND
        }
        System.out.println("Reached!");
    }

    public static void awaiting() throws InterruptedException {
        ExecutorService service = null;
        try {
            service = Executors.newSingleThreadExecutor();
            // Add tasks to the thread executor
            service.awaitTermination(1, TimeUnit.SECONDS);
            Thread.sleep(2000);

        } finally {
            System.out.println("shuting down");
            if(service != null) service.shutdown();
        }
        if(service != null) {
            System.out.println("waiting...");
            service.awaitTermination(1, TimeUnit.SECONDS);
            // Check whether all tasks are finished
            if(service.isTerminated())
                System.out.println("All tasks finished");
            else
                System.out.println("At least one task is still running");
        }
    }

    public static void schedule(){
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        Runnable task1 = () -> System.out.println("Hello Zoo " + LocalTime.now());
        Callable<String> task2 = () -> {
            System.out.println("Monkey " + LocalTime.now());
            return "Monkey";
        };
        Runnable task3 = () -> System.out.println("Hello Zoo part 3 " + LocalTime.now());
        Runnable task4 = () -> System.out.println("Hello Zoo part 4 " + LocalTime.now()) ;
        Runnable task5 = () -> System.out.println("Hello Zoo part 5 " + LocalTime.now()) ;

        /*Future<?> result1 = service.schedule(task1, 10, TimeUnit.SECONDS); //Hello Zoo " + LocalTime.now());
        Future<?> result2 = service.schedule(task2, 13, TimeUnit.SECONDS); //"Monkey " + LocalTime.now()

        service.scheduleAtFixedRate(task3,15,40,TimeUnit.SECONDS); //"Hello Zoo part 3 "

        service.scheduleWithFixedDelay(task4,0,60,TimeUnit.SECONDS); //"Hello Zoo part 4 "*/


        Runtime.getRuntime().availableProcessors();
        System.out.println(Runtime.getRuntime().availableProcessors()        );


        /*service = Executors.newScheduledThreadPool(3);
        service.scheduleAtFixedRate(task5,3,1,TimeUnit.SECONDS);*/
    }
}

class SheepManager {
    private int sheepCount = 0;
    private static AtomicInteger sheepCount2 = new AtomicInteger(0);


    private void incrementAndReport() {
        System.out.print((++sheepCount) + " ");
    }

    public static void main(String[] args) {
        /*System.out.println("Atomic");
        atomic();

        System.out.println("no Atomic");
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        Runnable task3 = () -> noAtomic();
        System.out.println("\n");
        service.scheduleWithFixedDelay(task3,3,60,TimeUnit.SECONDS);*/

        syncronized();
    }

    public static void noAtomic() {
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(3);
            SheepManager manager = new SheepManager();
            for (int i = 0; i < 10; i++)
                service.submit(manager::incrementAndReport);
        } finally {
            if (service != null) service.shutdown();
        }
    }

    public static void atomic(){
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(3);
            SheepManager manager = new SheepManager();
            for (int i = 0; i < 10; i++)
                service.submit(manager::incrementAndReportAtomic);
        } finally {
            if (service != null) service.shutdown();
        }
    }

    public void incrementAndReportAtomic(){
        System.out.print(sheepCount2.incrementAndGet()+" ");
    }

    public static void syncronized(){
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(3);
            SheepManager manager = new SheepManager();
            for(int i=0; i<10; i++) {
                synchronized(manager) {
                    service.submit(() -> manager.incrementAndReport());
                }
            }
        } finally {
            if (service != null) service.shutdown();
        }
    }
}

class SheepManager2 {
    private int sheepCount = 0;

    private void incrementAndReport() {
        synchronized(this) {
            System.out.print((++sheepCount)+" ");
        }
    }

    private synchronized void incrementAndReport2() {
        System.out.print((++sheepCount)+" ");
    }
    public static void main(String[] args) {
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(20);
            SheepManager2 manager = new SheepManager2();
            for(int i=0; i<10; i++)
                service.submit(() -> manager.incrementAndReport());
        } finally {
            if(service != null) service.shutdown();
        }
    }
}

class ZooManager {
    private Map<String,Object> foodData = new ConcurrentHashMap<String,Object>();

    public void put(String key, String value) {
        foodData.put(key, value);
    }
    public Object get(String key) {
        return foodData.get(key);
    }

    public static void test(){
        //Map<String, Object> foodData = new HashMap<String, Object>();
        Map<String, Object> foodData = new ConcurrentHashMap<String, Object>();

        foodData.put("penguin", 1);
        foodData.put("flamingo", 2);
        for(String key: foodData.keySet())
            foodData.remove(key);
    }

    public static void main(String[] args) {
        parallelStream();
    }

    public  static void copy(){
        List<Integer> list = new CopyOnWriteArrayList<>(Arrays.asList(4,3,52));
        for(Integer item: list) {
            System.out.print(item+" ");
            list.add(9);
        }
        System.out.println();
        System.out.println("Size: "+list.size());
    }

    public static void deque(){
        try {
            BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
            blockingQueue.offer(39);
            blockingQueue.offer(3, 4, TimeUnit.SECONDS);
            System.out.println(blockingQueue.poll());
            System.out.println("##");
            System.out.println(blockingQueue.poll(40, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            // Handle interruption
            e.printStackTrace();
        }


        System.out.println("\n\n##################\n\n");


        try {
            BlockingDeque<Integer> blockingDeque = new LinkedBlockingDeque<>();
            blockingDeque.offer(91);
            blockingDeque.offerFirst(5, 2, TimeUnit.MINUTES);
            blockingDeque.offerLast(47, 100, TimeUnit.MICROSECONDS);
            blockingDeque.offer(3, 4, TimeUnit.SECONDS);
            System.out.println(blockingDeque.poll());
            System.out.println(blockingDeque.poll(950, TimeUnit.MILLISECONDS));
            System.out.println(blockingDeque.pollFirst(200, TimeUnit.NANOSECONDS));
            System.out.println(blockingDeque.pollLast(1, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            // Handle interruption
        }
    }

    public static void parallelStream(){
        Stream<Integer> stream = Arrays.asList(1,2,3,4,5,6).stream();
        Stream<Integer> streamParallel = stream.parallel();

        Stream<Integer> streamParallel2 = Arrays.asList(1,2,3,4,5,6).parallelStream();

        Arrays.asList(1,2,3,4,5,6)
                //.stream()
                .parallelStream()
                .forEachOrdered(s -> System.out.print(s + " "));
    }
}

class WhaleDataCalculator {
    public int processRecord(int input) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Handle interrupted exception
        }
        return input+1;
    }
    public void processAllData(List<Integer> data) {
        //data.stream().map(a -> processRecord(a)).count();
        data.parallelStream().map(this::processRecord);

    }
    public static void main(String[] args) {
        //independientOperations();
        /*System.out.print(Arrays.asList(1,2,3,4,5,6).stream().findAny().get());
        System.out.println("\n######");
        System.out.print(Arrays.asList(1,2,3,4,5,6).parallelStream().findAny().get());
        System.out.println("\n");
        Arrays.asList(1,2,3,4,5,6).stream().unordered().parallel().forEach(System.out::print);*/

        /*System.out.println(Arrays.asList('w', 'o', 'l', 'f',' ','o','h',' ','m','y',' ','g','o','s','h')
                .stream()
                .reduce("as",(c,s1) -> c + s1,
                        (s2,s3) -> s2 + s3));

        System.out.println(Arrays.asList(1,2,3,4,5,6)
                .parallelStream()
                .reduce(0,(a,b) -> (a-b))); // NOT AN ASSOCIATIVE ACCUMULATOR

        System.out.println(Arrays.asList("w","o","l","f")
                .parallelStream()
                .reduce("X",String::concat)); //XwXoXlXf
*/

        Stream<String> ohMy = Stream.of("lions", "tigers", "bears").parallel();
        ConcurrentMap<Integer, String> map = ohMy
                .collect(Collectors.toConcurrentMap(String::length, k -> k,
                        (s1, s2) -> s1 + "," + s2));
        System.out.println(map); // {5=lions,bears, 6=tigers}
        System.out.println(map.getClass()); // java.util.concurrent.ConcurrentHashMap


    }


    private static void parallel(){
        WhaleDataCalculator calculator = new WhaleDataCalculator();

        // Define the data
        List<Integer> data = new ArrayList<Integer>();
        for(int i=0; i<4000; i++) data.add(i);

        System.out.println(data.toArray().length);
        // Process the data
        long start = System.currentTimeMillis();
        calculator.processAllData(data);
        double time = (System.currentTimeMillis() - start) / 1000;

        // Report results
        System.out.println("\nTasks completed in: " + time + " seconds");
    }

    private static void independientOperations(){
        Arrays.asList("jackal","kangaroo","lemur")
                .parallelStream()
                .map(s -> s.toUpperCase())
                .forEach(System.out::println);

        System.out.println(" \n ######### \n");

        Arrays.asList("jackal","kangaroo","lemur")
                .parallelStream()
                .map(s -> {System.out.println(s); return s.toUpperCase();})
                .forEach(System.out::println);

        System.out.println(" \n ######### \n");

        List<Integer> data = Collections.synchronizedList(new ArrayList<>());
        Arrays.asList(1,2,3,4,5,6).parallelStream()
                .map(i -> {data.add(i); return i;}) // AVOID STATEFUL LAMBDA EXPRESSIONS!
                .forEachOrdered(i -> System.out.print(i+" "));

        System.out.println();
        for(Integer e: data) {
            System.out.print(e+" ");
        }


    }
}


class LionPenManager {
    private void removeAnimals() { System.out.println("Removing animals"); }
    private void cleanPen() { System.out.println("Cleaning the pen"); }
    private void addAnimals() { System.out.println("Adding animals"); }
    public void performTask() {
        removeAnimals();
        cleanPen();
        addAnimals();
    }

    public void performTask(CyclicBarrier c1, CyclicBarrier c2) {
        try {
            removeAnimals();
            c1.await();
            cleanPen();
            c2.await();
            addAnimals();
        } catch (InterruptedException | BrokenBarrierException e) {
            // Handle checked exceptions here
        }
    }

    public static void main(String[] args) {

    }

    public static void cleanPenThread(){
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(4);
            LionPenManager manager = new LionPenManager();
            CyclicBarrier c1 = new CyclicBarrier(4, () -> System.out.println("Aguantaaaaaaaaaa"));
            CyclicBarrier c2 = new CyclicBarrier(4,
                    () -> System.out.println("*** Pen Cleaned!"));
            for(int i=0; i<4; i++)
                service.submit(() -> manager.performTask(c1,c2));
        } finally {
            if(service != null) service.shutdown();
        }
    }
}

class WeighAnimalAction extends RecursiveAction {
    private int start;
    private int end;
    private Double[] weights;

    public WeighAnimalAction(Double[] weights, int start, int end) {
        this.start = start;
        this.end = end;
        this.weights = weights;
    }

    protected void compute() {
        if(end-start <= 3)
            for(int i=start; i<end; i++) {
                weights[i] = (double)new Random().nextInt(100);
                System.out.println("Animal Weighed: "+i);
            }
        else {
            int middle = start+((end-start)/2);
            System.out.println("[start="+start+",middle="+middle+",end="+end+"]");
            invokeAll(new WeighAnimalAction(weights,start,middle),
                    new WeighAnimalAction(weights,middle,end));
        }
    }

    public static void main(String[] args) {
        Double[] weights = new Double[10];
        ForkJoinTask<?> task = new WeighAnimalAction(weights,0,weights.length);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(task);
        // Print results
        System.out.println();
        System.out.print("Weights: ");
        Arrays.asList(weights).stream().forEach(
                d -> System.out.print(d.intValue()+" "));
    }
}

class WeighAnimalTask extends RecursiveTask<Double> {
    private int start;
    private int end;
    private Double[] weights;
    public WeighAnimalTask(Double[] weights, int start, int end) {
        this.start = start;
        this.end = end;
        this.weights = weights;
    }
    protected Double compute() {
        if(end-start <= 3) {
            double sum = 0;
            for(int i=start; i<end; i++) {
                weights[i] = (double)new Random().nextInt(100);
                System.out.println("Animal Weighed: "+i);
                sum += weights[i];
            }
            return sum;
        } else {
            int middle = start+((end-start)/2);
            System.out.println("[start="+start+",middle="+middle+",end="+end+"]");
            RecursiveTask<Double> otherTask = new WeighAnimalTask(weights,start,middle);
            otherTask.fork();
            return new WeighAnimalTask(weights,middle,end).compute() + otherTask.join();
        }
    }

    public static void main(String[] args) {
        Double[] weights = new Double[10];
        ForkJoinTask<Double> task = new WeighAnimalTask(weights,0,weights.length);
        ForkJoinPool pool  = new ForkJoinPool();
        Double sum = pool.invoke(task);
        System.out.println("Sum: "+sum);

    }
}

 class Food {}
 class Water {}
 class Fox {
    public void eatAndDrink(Food food, Water water) {
        synchronized(food) {
            System.out.println("Got Food! Foxy");
            move();
            synchronized(water) {
                System.out.println("Got Water! Foxy");
            }
        }
    }
    public void drinkAndEat(Food food, Water water) {
        synchronized(water) {
            System.out.println("Got Water! Tails");
            move();
            synchronized(food) {
                System.out.println("Got Food! Tails");
            }
        }
    }
    public void move() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Handle exception
        }
    }
    public static void main(String[] args) {
        // Create participants and resources
        Fox foxy = new Fox();
        Fox tails = new Fox();
        Food food = new Food();
        Water water = new Water();
        // Process data
        ExecutorService service = null;
        try {
            service = Executors.newScheduledThreadPool(10);
            service.submit(() -> foxy.eatAndDrink(food,water));
            service.submit(() -> tails.drinkAndEat(food,water));
        } finally {
            if(service != null) service.shutdown();
        }
    }
}