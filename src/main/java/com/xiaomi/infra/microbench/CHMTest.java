package com.xiaomi.infra.microbench;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.hbase.util.Bytes;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

/**
 * @author zhangduo
 */
@State(Scope.Benchmark)
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
  public void test(Blackhole bh) {
    TestData.KEYS.stream().forEach(k -> MAP.put(new ByteArray(k), TestData.VAL));
    TestData.KEYS.stream().forEach(k -> bh.consume(MAP.get(new ByteArray(k))));
  }
}
