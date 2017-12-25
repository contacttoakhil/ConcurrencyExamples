package main.utilities.streams;

import main.vo.Item;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Akhil on 1/9/2016.
 */
public class StreamUtilityForList {
    /***
     * Input: List<Item> - a list having items in it.
     * Output: Map<Integer,Item> where key of the map is index of the item in the collection (input).
     * The input collection is assumed to be a List and moreover an ArrayList. It does not perform well for LinkedList.
     * Source: http://stackoverflow.com/questions/32859038/java-8-list-to-map-with-stream
     * http://stackoverflow.com/questions/4138364/java-how-to-convert-list-to-map
     */
    public static Map<Integer, Item> mappingListItemsToMap(List<Item> inputList) {
        Map<Integer,Item> map = inputList.stream().collect(Collectors.toMap(i -> inputList.indexOf(i), i -> i)); //BAD SOLUTION: As it will lead to O(n2) complexity and also may not work if item is duplicate in the list.
        Map<Integer,Item> outputMap = IntStream.range(0, inputList.size()).boxed().collect(Collectors.toMap(Function.identity(), i->inputList.get(i)));// Its better to use Function.identity() rather than i->i as shown below.
        return outputMap;
    }

    //http://stackoverflow.com/questions/4138364/java-how-to-convert-list-to-map
    public static Map<String, Integer> mappingListItemsToMapWithMerge(List<Integer> intList) {
        Map<String, Integer> map = intList.stream().collect(Collectors.toMap(i -> String.valueOf(i % 4), i -> i, Integer::sum));
        return map;
    }

    /**
     * Returns a map where each entry is an item of {@code list} mapped by the
     * key produced by applying {@code mapper} to the item.
     *
     * @param list the list to map
     * @param mapper the function to produce the key from a list item
     * @return the resulting map
     * @throws IllegalStateException on duplicate key
     */
    public static <K, T> Map<K, T> toMapBy(List<T> list, Function<? super T, ? extends K> mapper) {
        return list.stream().collect(Collectors.toMap(mapper, Function.identity()));
    }

    /***
     * Input:["a", "b", null, "c", null, "d", "e"]
     * Output: [["a", "b"], ["c"], ["d", "e"]]
     * Source: http://stackoverflow.com/questions/29095967/splitting-list-into-sublists-along-elements/29111023#29111023
     */
    public static List<List<String>> splitToSubLists(List<String> inputList) {
        List<List<String>> outputList = inputList.stream().collect(splitBySeparator(Objects::isNull));
        return outputList;
    }

    private static Collector<String, List<List<String>>, List<List<String>>> splitBySeparator(Predicate<String> sep) {
        return Collector.of(() -> new ArrayList<List<String>>(Arrays.asList(new ArrayList<>())),
                (l, elem) -> {
                    if (sep.test(elem)) {
                        l.add(new ArrayList<>());
                    } else l.get(l.size() - 1).add(elem);
                },
                (l1, l2) -> {
                    l1.get(l1.size() - 1).addAll(l2.remove(0));
                    l1.addAll(l2);
                    return l1;
                });
    }

}
