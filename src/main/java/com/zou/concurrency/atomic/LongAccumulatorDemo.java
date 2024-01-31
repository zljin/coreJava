package com.zou.concurrency.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;

/**
 * 描述：     演示LongAccumulator的用法
 *
 * 适合大量的并行计算
 * 对计算顺序不能有要求，不影响最终结果
 */
public class LongAccumulatorDemo {

    public static void main(String[] args) {
        //identity为最开始的值
        LongAccumulator accumulator = new LongAccumulator((x, y) -> 2+ x + y, 0);
        LongAccumulator accumulator2 = new LongAccumulator((x, y) -> 2 + x * y, 0);
        ExecutorService executor = Executors.newFixedThreadPool(8);
        //进行累加,开始x为上一次identity的值，即0
        //输入1之后，即y为1然后累加，identity则变为1，为下次累加的x
        IntStream.range(1, 10).forEach(i -> executor.submit(() -> accumulator.accumulate(i)));
        IntStream.range(1, 10).forEach(i -> executor.submit(() -> accumulator2.accumulate(i)));

        executor.shutdown();
        while (!executor.isTerminated()) {

        }
        System.out.println(accumulator.getThenReset());
        System.out.println(accumulator2.getThenReset());
    }
}
