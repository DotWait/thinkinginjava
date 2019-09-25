package com.dotwait.constitute;

import com.dotwait.log.TraceUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Stack;

/**
 * 用以生成排列组合
 */
public class Constitute {
    private List<List<Integer>> permutations;
    private List<List<Integer>> combinations;

    public Constitute() {
        permutations = new ArrayList<>();
        combinations = new ArrayList<>();
    }

    /**
     * 生成顺序的数字
     * @param num 数字上限
     * @return 顺序的数字集合
     */
    public List<Integer> generateNumbers(int num){
        List<Integer> numbers = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            numbers.add(i);
        }
        return numbers;
    }

    /**
     * 生成所有组合情况的集合
     * @param numbers 顺序的数字
     * @return 所有数字的组合情况
     */
    public List<List<Integer>> allCombination(List<Integer> numbers){
        for (int i = 1; i <= numbers.size(); i++) {
            combination(numbers, i);
        }
        return combinations;
    }

    /**
     * 从numbers中选出num个数组合
     * @param numbers 数字序列
     * @param num 个数
     * @return 组合
     */
    public List<List<Integer>> combination(List<Integer> numbers, int num) {
        if (num > numbers.size()) {
            return combinations;
        }
        BitSet bitSet = new BitSet(numbers.size());
        bitSet.set(0, num);
        combinations.add(bitSetToList(bitSet));
        while (true){
            /*找到第一个10，改为01*/
            int first10 = findFirst10(bitSet, numbers.size());
            if (first10 < 0){
                break;
            }
            exchange10To01(bitSet, first10);
            /*将左边的1移动到最左端*/
            moveToLeft(bitSet, first10);
            combinations.add(bitSetToList(bitSet));
        }
        return combinations;
    }

    /**
     * 寻找第一个10
     *
     * @param bitSet 位图
     * @param max    位的总个数
     * @return 第一个10的index
     */
    private int findFirst10(BitSet bitSet, int max) {
        for (int i = 0; i < max; i++) {
            int nextSetBit = bitSet.nextSetBit(i);
            int nextClearBit = bitSet.nextClearBit(i);
            if (nextSetBit == -1) {
                return -1;
            }
            if (nextSetBit == i && nextClearBit == i + 1 && i < max -1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 交换指定位置的10为01
     * @param bitSet 位图
     * @param index 指定位置
     */
    private void exchange10To01(BitSet bitSet, int index) {
        bitSet.clear(index);
        bitSet.set(index + 1);
    }

    /**
     * 移动指定位置左边的所有1至最左端
     * @param bitSet 位图
     * @param index 指定位置
     */
    private void moveToLeft(BitSet bitSet, int index){
        int left = 0;
        int right = index - 1;
        while (left < right){
            while (bitSet.get(left) && left < right){
                left++;
            }
            while (!bitSet.get(right) && left < right){
                right--;
            }
            if (left < right){
                bitSet.set(left);
                bitSet.clear(right);
            }
        }
    }

    /**
     * 位图中的1存入List集合
     * @param bitSet 位图
     * @return list
     */
    private List<Integer> bitSetToList(BitSet bitSet){
        List<Integer> result = new ArrayList<>();
        int i = bitSet.nextSetBit(0);
        if (i != -1) {
            result.add(i);
            while (true) {
                if (++i < 0) break;
                if ((i = bitSet.nextSetBit(i)) < 0) break;
                int endOfRun = bitSet.nextClearBit(i);
                do { result.add(i); }
                while (++i != endOfRun);
            }
        }
        return result;
    }

    private void showIndex(BitSet bitSet, int index){
        System.out.println("get the index bit ==> "+ bitSet.get(index));
    }

    public List<List<Integer>> permutation(List<Integer> numbers, int num) {
        permutate(new Stack<>(), numbers, 0, num);
        return permutations;
    }

    private void permutate(Stack<Integer> combination, List<Integer> numbers, int layer, int num) {
        if (layer >= num) {
            permutations.add(new ArrayList<>(combination));
            return;
        }
        for (int i = 0; i < numbers.size(); i++) {
            Integer remove = numbers.remove(i);
            combination.push(remove);
            permutate(combination, numbers, layer + 1, num);
            Integer pop = combination.pop();
            numbers.add(i, pop);
        }
    }

    @Test
    public void listTest() {
        List<Integer> intList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            intList.add(i);
        }
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < intList.size(); i++) {
            Integer remove = intList.remove(i);
            stack.push(remove);
            intList.forEach(System.out::println);
            Integer pop = stack.pop();
            intList.add(i, pop);
            System.out.println("=================");
        }
    }

    @Test
    public void permutationTest() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        Constitute constitute = new Constitute();
        long start = System.currentTimeMillis();
        List<List<Integer>> lists = constitute.permutation(numbers, 2);
        long end = System.currentTimeMillis();
        System.out.println(end - start + "ms");
        lists.forEach(list -> {
            list.forEach(System.out::println);
            System.out.println("==========");
        });
        System.out.println(lists.size());
    }

    @Test
    public void combinationTest() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        BitSet bitSet = new BitSet(numbers.size());
        bitSet.set(0);
        bitSet.set(2);
        bitSet.set(3);
        exchange10To01(bitSet, 3);
        moveToLeft(bitSet, 5);
        for (int j = 0; j < numbers.size(); j++) {
            int nextIndex = bitSet.nextSetBit(j);
            int nextClear = bitSet.nextClearBit(j);
            System.out.println("nextIndex ==> " + nextIndex + ", nextClear ==> " + nextClear);
        }
        System.out.println(bitSet.cardinality());
        System.out.println(bitSet.length());
        System.out.println(bitSet.toString());
    }

    @Test
    public void combinationsTest(){
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            numbers.add(i);
        }
        for (int i = 1; i <= 5; i++) {
            List<List<Integer>> combinationList = combination(numbers, i);
            System.out.println(combinationList.size());
            combinationList.forEach(System.out::println);
            System.out.println("=============="+i);
        }
    }


}
