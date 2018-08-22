Sorting algorithms implemented in **SortHelper.java**

Sorting based on groups, sequences or both with minimum card left not in groups or sequences (SmartSort).

Group and Sequence sorts use BucketSort with InsertionSort for each bucket.

3 algorithm implemented for SmartSort. 2nd is the best.

smartSort1: Greedy algorithm. Faster but does not provide optimal solution for every case.
smartSort2: Kinda bruteforce. O(n^2) average.
smartSort3: Experimental algorithm. O(n!) time complexity.

Test cases are in **SortHelperTest.java**.
