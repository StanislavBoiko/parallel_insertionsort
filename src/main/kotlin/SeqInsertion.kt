
fun <T> sequentialInsertionSort(arr: MutableList<T>, comparator: Comparator<T>) {
    if (arr.size < 2) {
        return
    }

    for (i in 1 until arr.size) {
        val key = arr[i]
        var j = i - 1
        while (j >= 0 && comparator.compare(arr[j], key) > 0) {
            arr[j + 1] = arr[j]
            j--
        }
        arr[j + 1] = key
    }
}

