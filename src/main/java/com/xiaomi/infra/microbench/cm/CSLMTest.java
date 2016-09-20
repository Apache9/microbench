/**
 * 
 */
package com.xiaomi.infra.microbench.cm;

import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.hadoop.hbase.util.Bytes;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

/**
 * @author zhangduo
 */
@State(Scope.Benchmark)
public class CSLMTest {

  private static final ConcurrentSkipListMap<byte[], Object> MAP =
      new ConcurrentSkipListMap<>(Bytes::compareTo);

  @Benchmark
  public void test(Blackhole bh) {
    TestData.KEYS.stream().forEach(k -> MAP.put(k, TestData.VAL));
    TestData.KEYS.stream().forEach(k -> bh.consume(MAP.get(k)));
  }
}
