import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

fun <T> parallelizedInsertionSortWithCoroutines(arr: MutableList<T>, numThreads: Int, comparator: Comparator<T>) = runBlocking {
    val subArrays = List(numThreads) { i ->
        val start = i * (arr.size / numThreads)
        val end = if (i == numThreads - 1) arr.size else (i + 1) * (arr.size / numThreads)
        arr.slice(start until end).toMutableList()
    }

    val sortedSubArrays = subArrays.map { subArray ->
        async(Dispatchers.Default) {
            sequentialInsertionSort(subArray, comparator)
            subArray
        }
    }.awaitAll()

    val mergedArray = mergeSortedArrays(sortedSubArrays, comparator)
    arr.clear()
    arr.addAll(mergedArray)
}


fun <T> parallelizedInsertionSortWithThreads(arr: MutableList<T>, numThreads: Int, comparator: Comparator<T>) {
    val subArrays = List(numThreads) { i ->
        val start = i * (arr.size / numThreads)
        val end = if (i == numThreads - 1) arr.size else (i + 1) * (arr.size / numThreads)
        arr.slice(start until end).toMutableList()
    }

    val latch = CountDownLatch(numThreads)

    for (i in 0 until numThreads) {
        val subArray = subArrays[i]
        thread {
            sequentialInsertionSort(subArray, comparator)
            synchronized(arr) {
                subArrays[i].indices.forEach { index ->
                    arr[i * (arr.size / numThreads) + index] = subArray[index]
                }
            }
            latch.countDown()
        }
    }

    latch.await()
    val mergedArray = mergeSortedArrays(subArrays, comparator)
    arr.clear()
    arr.addAll(mergedArray)
}


fun <T> mergeSortedArrays(arrays: List<List<T>>, comparator: Comparator<T>): List<T> {
    val mergedArray = ArrayList<T>(arrays.sumBy { it.size })
    val indices = IntArray(arrays.size)

    var mergedIndex = 0
    while (mergedIndex < mergedArray.size) {
        var minValue = arrays.firstOrNull { it.isNotEmpty() }?.get(indices[0])

        var minIndex = -1

        for (i in arrays.indices) {
            val array = arrays[i]
            val index = indices[i]

            if (index < array.size) {
                val value = array[index]
                if (minValue == null || comparator.compare(value, minValue) < 0) {
                    minValue = value
                    minIndex = i
                }
            }
        }

        if (minIndex != -1) {
            mergedArray.add(minValue!!)
            indices[minIndex]++
            mergedIndex++
        }
    }

    return mergedArray
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