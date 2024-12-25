import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Task2 {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("=== Program Execution Started ===");

        // Рандомна послідовність - початкова
        CompletableFuture<int[]> randomArrayFuture = CompletableFuture.supplyAsync(() -> {
            long taskStartTime = System.currentTimeMillis();
            int[] randomArray = generateRandomArray(20);
            System.out.println("\nGenerated random array: " + Arrays.toString(randomArray));
            printElapsedTime("Random array generation", taskStartTime);
            return randomArray;
        });

        // Масив від користувача
        CompletableFuture<int[]> userArrayFuture = randomArrayFuture.thenApplyAsync(randomArray -> {
            long taskStartTime = System.currentTimeMillis();
            System.out.println("\n=== Now, please enter your array of numbers ===");
            int[] userArray = getUserGeneratedArray();
            System.out.println("User generated array: " + Arrays.toString(userArray));
            printElapsedTime("User array input", taskStartTime);
            return userArray;
        });

        // Обчислення результатів для обох масивів
        CompletableFuture<Void> resultsFuture = userArrayFuture.thenCombineAsync(randomArrayFuture,
                (userArray, randomArray) -> {
                    long taskStartTime = System.currentTimeMillis();

                    int minRandomSum = calculateMinConsecutiveSum(randomArray);
                    int minUserSum = calculateMinConsecutiveSum(userArray);

                    System.out.println("\n=== Results for Random Array ===");
                    System.out.println("Minimum sum of consecutive pairs: " + minRandomSum);

                    System.out.println("\n=== Results for User Array ===");
                    System.out.println("Minimum sum of consecutive pairs: " + minUserSum);

                    printElapsedTime("Results computation", taskStartTime);
                    return null;
                });

        // Кінець програми
        resultsFuture.thenRunAsync(() -> {
            printElapsedTime("\n=== Total Program Execution Time ===", startTime);
        }).join();
    }

    // Випадковий масив
    private static int[] generateRandomArray(int size) {
        return new java.util.Random().ints(size, 1, 100).toArray(); // Випадкові числа в діапазоні [1, 100)
    }

    // Створення масиву користувачем
    private static int[] getUserGeneratedArray() {
        System.out.println("Please input 20 natural numbers (separated by spaces):");
        Scanner scanner = new Scanner(System.in);
        int[] array = new int[20];
        for (int i = 0; i < 20; i++) {
            while (true) {
                if (scanner.hasNextInt()) {
                    int number = scanner.nextInt();
                    if (number > 0) {
                        array[i] = number;
                        break;
                    } else {
                        System.out.println("Please enter a positive natural number:");
                    }
                } else {
                    System.out.println("Invalid input. Try again:");
                    scanner.next();
                }
            }
        }
        return array;
    }

    // Обчислення мінімальної суми послідовних пар
    private static int calculateMinConsecutiveSum(int[] array) {
        int minSum = Integer.MAX_VALUE;
        for (int i = 0; i < array.length - 1; i++) {
            int sum = array[i] + array[i + 1];
            if (sum < minSum) {
                minSum = sum;
            }
        }
        return minSum;
    }

    // private static int calculateMinConsecutiveSum(int[] array) {
    // int minSum = Integer.MAX_VALUE;
    // for (int i = 0; i < array.length - 1; i++) {
    // int sum = array[i] + array[i + 1];
    // System.out.printf("Sum of elements at indices %d and %d (%d + %d): %d%n",
    // i, i + 1, array[i], array[i + 1], sum);
    // if (sum < minSum) {
    // minSum = sum;
    // }
    // }
    // System.out.printf("Minimum sum of consecutive pairs: %d%n", minSum);
    // return minSum;
    // }

    // Час, витрачений на виконання завдання
    private static void printElapsedTime(String taskName, long startTime) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.printf("%s completed in %d ms.%n", taskName, elapsedTime);
    }
}
