import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

fun parallelizedInsertionSortWithCoroutines(arr: IntArray, numThreads: Int) = runBlocking {
    val subArrays = Array(numThreads) { i ->
        val start = i * (arr.size / numThreads)
        val end = if (i == numThreads - 1) arr.size else (i + 1) * (arr.size / numThreads)
        arr.slice(start until end).toIntArray()
    }

    val sortedSubArrays = subArrays.map { subArray ->
        async(Dispatchers.Default) {
            sequentialInsertionSort(subArray)
            subArray
        }
    }.awaitAll()

    val mergedArray = mergeSortedArrays(sortedSubArrays.toTypedArray())
    System.arraycopy(mergedArray, 0, arr, 0, arr.size)
}

fun parallelizedInsertionSortWithThreads(arr: IntArray, numThreads: Int) {
    val subArrays = Array(numThreads) { i ->
        val start = i * (arr.size / numThreads)
        val end = if (i == numThreads - 1) arr.size else (i + 1) * (arr.size / numThreads)
        arr.slice(start until end).toIntArray()
    }

    val latch = CountDownLatch(numThreads)

    for (i in 0 until numThreads) {
        val subArray = subArrays[i]
        thread {
            sequentialInsertionSort(subArray)
            subArrays[i] = subArray
            latch.countDown()
        }
    }

    latch.await()
    val mergedArray = mergeSortedArrays(subArrays)
    System.arraycopy(mergedArray, 0, arr, 0, arr.size)
}

fun mergeSortedArrays(arrays: Array<IntArray>): IntArray {
    var merged = arrays[0]
    for (i in 1 until arrays.size) {
        merged = mergeTwoSortedArrays(merged, arrays[i])
    }
    return merged
}

fun mergeTwoSortedArrays(arr1: IntArray, arr2: IntArray): IntArray {
    val merged = IntArray(arr1.size + arr2.size)
    var i = 0
    var j = 0
    var k = 0

    while (i < arr1.size && j < arr2.size) {
        if (arr1[i] < arr2[j]) {
            merged[k++] = arr1[i++]
        } else {
            merged[k++] = arr2[j++]
        }
    }

    while (i < arr1.size) {
        merged[k++] = arr1[i++]
    }

    while (j < arr2.size) {
        merged[k++] = arr2[j++]
    }

    return merged
}