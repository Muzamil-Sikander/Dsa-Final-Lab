import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Queue {
    private int maxSize;
    private int[] queueArray;
    private int front;
    private int rear;
    private int nItems;

    public Queue(int size) {
        this.maxSize = size;
        this.queueArray = new int[maxSize];
        this.front = 0;
        this.rear = -1;
        this.nItems = 0;
    }

    public void insert(int value) {
        if (rear == maxSize - 1) {
            rear = -1;
        }
        queueArray[++rear] = value;
        nItems++;
    }

    public int remove() {
        int temp = queueArray[front++];
        if (front == maxSize) {
            front = 0;
        }
        nItems--;
        return temp;
    }

    public int peekFront() {
        return queueArray[front];
    }

    public boolean isEmpty() {
        return (nItems == 0);
    }

    public boolean isFull() {
        return (nItems == maxSize);
    }
}
public class Scheduler {
    private Queue queue;

    public Scheduler(int size) {
        this.queue = new Queue(size);
    }

    public void executeProcessesFromFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            int quantum = scanner.nextInt(); // Number of instructions to execute at a time
            int numProcesses = scanner.nextInt(); // Total number of processes

            for (int i = 1; i <= numProcesses; i++) {
                queue.insert(i);
            }

            while (!queue.isEmpty()) {
                int currentProcess = queue.remove();
                System.out.println("Executing " + quantum + " instructions of process p" + currentProcess);

                // Decrease the total instructions of the current process
                quantum--;
                if (quantum == 0) {
                    System.out.println("Process p" + currentProcess + " has finished execution");
                    if (!queue.isEmpty()) {
                        quantum = scanner.nextInt(); // Get the next quantum from the file
                    }
                } else {
                    queue.insert(currentProcess); // Put the process back into the queue
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler(10); // Initialize the scheduler with a maximum size

        if (args.length > 0) {
            scheduler.executeProcessesFromFile(args[0]); // Use the provided file name from command-line argument
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the file name: ");
            String fileName = scanner.nextLine();
            scheduler.executeProcessesFromFile(fileName);
            scanner.close();
        }
    }
}