import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Task1 {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        System.out.println("=== Program execution started ===");

        // Асинхронне створення масиву
        CompletableFuture<int[]> initialArrayFuture = CompletableFuture.supplyAsync(() -> {
            long taskStartTime = System.currentTimeMillis();
            int[] array = generateRandomArray(10);
            System.out.println("\nGenerated array: " + Arrays.toString(array));
            printElapsedTime("Array generation", taskStartTime);
            return array;
        });

        // Асинхронне збільшення кожного елемента масиву на 5
        CompletableFuture<int[]> updatedArrayFuture = initialArrayFuture.thenApplyAsync(array -> {
            long taskStartTime = System.currentTimeMillis();
            int[] updatedArray = Arrays.stream(array).map(x -> x + 5).toArray();
            System.out.println("\nUpdated array (+5 to each element): " + Arrays.toString(updatedArray));
            printElapsedTime("Array update", taskStartTime);
            return updatedArray;
        });

        // Асинхронне знаходження суми елементів двох масивів
        CompletableFuture<Integer> sumFuture = updatedArrayFuture.thenCombineAsync(initialArrayFuture,
                (updatedArray, initialArray) -> {
                    long taskStartTime = System.currentTimeMillis();
                    int sum = Arrays.stream(updatedArray).sum() + Arrays.stream(initialArray).sum();
                    System.out.printf(
                            "\nSum of elements from both arrays:\n" +
                                    "- Initial array: %d\n" +
                                    "- Updated array: %d\n" +
                                    "=> Total sum: %d\n",
                            Arrays.stream(initialArray).sum(),
                            Arrays.stream(updatedArray).sum(),
                            sum);
                    printElapsedTime("Sum calculation", taskStartTime);
                    return sum;
                });

        // Асинхронне знаходження факторіалу
        CompletableFuture<Void> factorialFuture = sumFuture.thenApplyAsync(sum -> {
            long taskStartTime = System.currentTimeMillis();
            long factorial = calculateFactorial(sum);
            System.out.printf("\nFactorial of the sum (%d): %d\n", sum, factorial);
            printElapsedTime("Factorial calculation", taskStartTime);
            return factorial;
        }).thenAcceptAsync(factorial -> {
            long taskStartTime = System.currentTimeMillis();
            System.out.printf("\nResult: Factorial = %d\n", factorial);
            printElapsedTime("Result display", taskStartTime);
        });

        // Асинхронне завершення програми
        factorialFuture.thenRunAsync(() -> {
            printElapsedTime("\n=== Total program execution time ===", startTime);
        }).join(); // Чекаємо завершення всіх операцій
    }

    // Метод для генерації випадкового масиву
    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        return random.ints(size, 1, 10).toArray(); // Випадкові числа в діапазоні [1, 10)
    }

    // Метод для обчислення факторіалу
    private static long calculateFactorial(int num) {
        long result = 1;
        for (int i = 2; i <= num; i++) {
            result *= i;
        }
        return result;
    }

    // Метод для виводу часу, витраченого на виконання завдання
    private static void printElapsedTime(String taskName, long startTime) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.printf("%s completed in %d ms.%n", taskName, elapsedTime);
    }
}
