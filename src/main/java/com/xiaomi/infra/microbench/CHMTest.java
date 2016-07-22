package com.xiaomi.infra.microbench;

import java.util.concurrent.ConcurrentHashMap;

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
public class CHMTest {

  private static final class ByteArray {

    private final byte[] b;

    public ByteArray(byte[] b) {
      this.b = b;
    }

    @Override
    public int hashCode() {
      return Bytes.hashCode(b);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != ByteArray.class) {
        return false;
      }
      return Bytes.equals(b, ((ByteArray) obj).b);
    }
  }

  private static final ConcurrentHashMap<ByteArray, Object> MAP = new ConcurrentHashMap<>();

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @Threads(40)
  public void test(Blackhole bh) {
    TestData.KEYS.stream().forEach(k -> MAP.put(new ByteArray(k), TestData.VAL));
    TestData.KEYS.stream().forEach(k -> bh.consume(MAP.get(new ByteArray(k))));
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder().include(CHMTest.class.getName()).forks(1).build();
    new Runner(opt).run();
  }
}
