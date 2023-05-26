const val TESTS = 1

fun test(numThreads: Int, arrSize: Int){
    var totalTimeSequential = 0L;
    var totalTimeParallelCoroutines = 0L;
    var totalTimeParallelThreads = 0L;

    for(count in 1..TESTS) {
        println("$arrSize elements")
        println("threads used $numThreads")

        val arr = IntArray(arrSize) { (Math.random() * arrSize).toInt() }
        val arrCopy1 = arr.copyOf()
        val arrCopy2 = arr.copyOf()

//        val startTimeSequential = System.nanoTime()
//        sequentialInsertionSort(arr)
//        val endTimeSequential = System.nanoTime()

//        println("Array sorted (sequential): ${isSorted(arr)}");

        val startTimeParallelCoroutines = System.nanoTime()
        parallelizedInsertionSortWithCoroutines(arrCopy1, numThreads)
        val endTimeParallelCoroutines = System.nanoTime()

        println("Array sorted (parallel/coroutines): ${isSorted(arrCopy1)}");

        val startTimeParallelThreads = System.nanoTime()
        parallelizedInsertionSortWithThreads(arrCopy2, numThreads)
        val endTimeParallelThreads = System.nanoTime()

        println("Array sorted (parallel/threads): ${isSorted(arrCopy2)}");

//        val timeSequential = endTimeSequential - startTimeSequential;
        val timeParallelCoroutines = endTimeParallelCoroutines - startTimeParallelCoroutines;
        val timeParallelThreads = endTimeParallelThreads - startTimeParallelThreads;

//        totalTimeSequential += timeSequential;
        totalTimeParallelCoroutines += timeParallelCoroutines;
        totalTimeParallelThreads += timeParallelThreads;


//        println("Sequential Time: ${timeSequential} ns, ${timeSequential / 1e6} ms")
        println("Parallel with Coroutines Time: ${timeParallelCoroutines / 1e6} ms")
        println("Parallel with Threads Time: ${timeParallelThreads / 1e6} ms")
    }
    println("==========================================================================")
    println("$TESTS tests conducted")
//    println("Average sequential Time: ${totalTimeSequential / TESTS} ns, ${(totalTimeSequential / 1e6) / TESTS} ms")
    println("Average Parallel with Coroutines Time: ${(totalTimeParallelCoroutines / 1e6) / TESTS} ms")
    println("Average Parallel with Threads Time: ${(totalTimeParallelThreads / 1e6) / TESTS} ms")
}




fun main() {

    test(2, 10_000)
    test(2, 25_000)
    test(2, 50_000)
    test(2, 100_000)
    test(2, 500_000)
    test(2, 1_000_000)

    test(4, 10_000)
    test(4, 25_000)
    test(4, 50_000)
    test(4, 100_000)
    test(4, 500_000)
    test(4, 1_000_000)

    test(6, 10_000)
    test(6, 25_000)
    test(6, 50_000)
    test(6, 100_000)
    test(6, 500_000)
    test(6, 1_000_000)

    test(8, 10_000)
    test(8, 25_000)
    test(8, 50_000)
    test(8, 100_000)
    test(8, 500_000)
    test(8, 1_000_000)

    test(10, 10_000)
    test(10, 25_000)
    test(10, 50_000)
    test(10, 100_000)
    test(10, 500_000)
    test(10, 1_000_000)

    test(12, 10_000)
    test(12, 25_000)
    test(12, 50_000)
    test(12, 100_000)
    test(12, 500_000)
    test(12, 1_000_000)

}

fun isSorted(a: IntArray): Boolean {
    for (i in 0 until a.size - 1)
    {
        if (a[i] > a[i + 1]) {
            return false
        }
    }
    return true
}