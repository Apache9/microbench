
package com.xiaomi.infra.microbench.compute;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
public class CIATest {

  private static final ConcurrentHashMap<Integer, Object> MAP = new ConcurrentHashMap<>();

  private List<Integer> KEYS;

  @Setup
  public void setup() {
    int numKeys = 16384;
    Random rand = new Random(12345);
    KEYS = Stream.generate(() -> rand.nextInt()).limit(numKeys).collect(Collectors.toList());
    Collections.shuffle(KEYS, new Random(Thread.currentThread().getName().hashCode()));
  }

  @Benchmark
  public void test(Blackhole bh) {
    KEYS.stream().forEach(key -> bh.consume(MAP.computeIfAbsent(key, k -> new Object())));
  }
}
