/**
 * 
 */
package com.xiaomi.infra.microbench;

import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.hadoop.hbase.util.Bytes;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author zhangduo
 */
@Measurement(iterations = 10)
public class CSLMTest {

  private static final ConcurrentSkipListMap<byte[], Object> MAP =
      new ConcurrentSkipListMap<>(Bytes::compareTo);

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @Threads(40)
  public void test(Blackhole bh) {
    TestData.KEYS.stream().forEach(k -> MAP.put(k, TestData.VAL));
    TestData.KEYS.stream().forEach(k -> bh.consume(MAP.get(k)));
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder().include(CSLMTest.class.getName()).forks(1).build();
    new Runner(opt).run();
  }
}
