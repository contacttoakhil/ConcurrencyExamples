package test;

import main.streams.taggedArray.TaggedArray;
import main.utilities.arrays.ArrayUtils;
import main.utilities.streams.StreamUtilityForList;
import main.utilities.string.RandomStringGenerator;
import main.vo.Item;
import main.vo.Student;
import org.junit.Test;
import main.streams.primeNumber.PrimeNumbersService;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CodeTester {
    ArrayUtils arrayUtils = new ArrayUtils();

    @Test
    public void testSliceArray() {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final int n = 3;
        int[][] slicedArrayOne = arrayUtils.slice(array,3);
        int[][] slicedArrayTwo = arrayUtils.sliceUsingJavaStreams(array,3);
        System.out.println(Arrays.deepToString(slicedArrayOne));
        System.out.println(Arrays.deepToString(slicedArrayTwo));
    }

    @Test
    public void testPrimesServiceSequential() {
        int maxPrimeToTry = 9999999;
        long startTime = System.currentTimeMillis();
        Set<Integer> result = PrimeNumbersService.findPrimesUsingStreams(maxPrimeToTry);
        long timeTaken = System.currentTimeMillis() - startTime;
        result.stream().sorted().forEach(System.out::println);
        System.out.println("Time Taken: "+timeTaken);
    }

    @Test
    public void testPrimesServiceParallel() {
        int maxPrimeToTry = 9999999;
        long startTime = System.currentTimeMillis();
        Set<Integer> result = PrimeNumbersService.findPrimesUsingParallelStreams(maxPrimeToTry);
        long timeTaken = System.currentTimeMillis() - startTime;
        result.stream().sorted().forEach(System.out::println);
        System.out.println("Time Taken: "+timeTaken);
    }

    @Test
    public void testTaggedArray() throws Exception{
        Object[] objects = new Object[100];
        Object[] tags = new Object[100];

        populateRandomIntegers(objects);
        populateRandomStrings(tags);

        TaggedArray array = new TaggedArray(objects,tags);
        TaggedArray.parEach(array, o -> {
            System.out.println(((Integer) o).intValue());
        });
    }
    private void populateRandomStrings(Object[] tags) throws Exception {
        for(int i =0; i<tags.length; i++) {
            tags[i] = RandomStringGenerator.generateRandomString(5,RandomStringGenerator.Mode.ALPHA);
        }
    }

    private void populateRandomIntegers(Object[] objects) {
        Random random = new Random();
        for(int i =0; i<objects.length; i++) {
            objects[i] = random.nextInt();
        }
    }

    @Test
    public void testMappingListItemsToMap() {
        Map<Integer, Item> outputMap =  StreamUtilityForList.mappingListItemsToMap(Item.getItems());
        System.out.println(outputMap);
    }

    @Test
    public void mappingListItemsToMapWithMerge() {
        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        Map<String, Integer> map = StreamUtilityForList.mappingListItemsToMapWithMerge(intList);
        System.out.println(map);
    }

    @Test
    //http://stackoverflow.com/questions/4138364/java-how-to-convert-list-to-map
    public void mappingListItemsToMapUsingUtility() {
        List<Student> students = Student.getStudents();
        Map<Integer, Student> studentsById = StreamUtilityForList.toMapBy(students, Student::getStudentID);
        System.out.println(studentsById);
    }

    @Test
    public void testSplitToSubLists() {
        List<String> inputList = new ArrayList<>();
        inputList.add("a");inputList.add("b");inputList.add(null);inputList.add("c");inputList.add(null);inputList.add("d");inputList.add("e");
        List<List<String>> outputList = StreamUtilityForList.splitToSubLists(inputList);
        System.out.println(outputList);
    }

    @Test
    public void testBasicStreamsOperations() {
        List<String> myList = Arrays.asList("a1", "a2", "b1", "c2", "c1");
        myList.stream()                             //Calling the method stream() on a bunch of objects returns a regular object stream. But we don't need collection to work with streams.
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);

        Stream.of("a1", "a2", "a3").findFirst().ifPresent(System.out::println);  // a1. We can just use Stream.of()

        IntStream.range(1, 4).forEach(System.out::println); //IntStreams can replace the regular for-loop using range().

        /* Primitive streams use specialized lambda expressions, e.g. IntFunction instead of Function or IntPredicate instead of Predicate. And primitive streams support the
        additional terminal aggregate operations sum() and average():
         */
        Arrays.stream(new int[]{1, 2, 3}).map(n -> 2 * n + 1)
                .average()
                .ifPresent(System.out::println);  // 5.0

        /*Sometimes it's useful to transform a regular object stream to a primitive stream or vice versa. For that purpose object streams support the special mapping operations
        mapToInt(), mapToLong() and mapToDouble*/
        Stream.of("a1", "a2", "a3").map(s -> s.substring(1)).mapToInt(Integer::parseInt).max().ifPresent(System.out::println);  // 3

        Stream.of(1.0, 2.0, 3.0).mapToInt(Double::intValue).mapToObj(i -> "a" + i).forEach(System.out::println);

        // What will happen now? Hint: Terminal operation is missing.
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                });

    }
}
