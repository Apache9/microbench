package com.xiaomi.infra.microbench.compute;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

public class CIATest {

  @State(Scope.Benchmark)
  public static class MapContainer {

    public ConcurrentMap<Integer, Object> map;

    @Setup(Level.Iteration)
    public void setup() {
      map = new ConcurrentHashMap<>();
    }
  }

  private static final int NUM_KEYS = 16384;

  @State(Scope.Thread)
  public static class RandomTestData {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public List<Integer> keys;

    @Setup(Level.Trial)
    public void setup() {
      Random rand = new Random(COUNTER.incrementAndGet());
      keys = Stream.generate(() -> rand.nextInt()).limit(NUM_KEYS).collect(Collectors.toList());
    }
  }

  @State(Scope.Thread)
  public static class RandomContentionalTestData {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public List<Integer> keys;

    @Setup(Level.Trial)
    public void setup() {
      Random rand = new Random(COUNTER.incrementAndGet());
      keys = Stream.generate(() -> rand.nextInt(100)).limit(NUM_KEYS).collect(Collectors.toList());
    }
  }

  private void testComputeIfAbsent(ConcurrentMap<Integer, Object> map, List<Integer> keys,
      Blackhole bh) {
    keys.stream().forEach(key -> bh.consume(map.computeIfAbsent(key, k -> new Object())));
  }

  @Benchmark
  public void testComputeIfAbsent(MapContainer container, RandomTestData testData, Blackhole bh) {
    testComputeIfAbsent(container.map, testData.keys, bh);
  }

  @Benchmark
  public void testComputeIfAbsentContentional(MapContainer container,
      RandomContentionalTestData testData, Blackhole bh) {
    testComputeIfAbsent(container.map, testData.keys, bh);
  }

  private void testPutIfAbsent(ConcurrentMap<Integer, Object> map, List<Integer> keys,
      Blackhole bh) {
    keys.stream().forEach(key -> {
      Object o = map.get(key);
      if (o != null) {
        bh.consume(o);
        return;
      }
      Object newO = new Object();
      o = map.putIfAbsent(key, newO);
      if (o != null) {
        bh.consume(o);
      } else {
        bh.consume(newO);
      }
    });
  }

  @Benchmark
  public void testPutIfAbsent(MapContainer container, RandomTestData testData, Blackhole bh) {
    testPutIfAbsent(container.map, testData.keys, bh);
  }

  @Benchmark
  public void testPutIfAbsentContentional(MapContainer container,
      RandomContentionalTestData testData, Blackhole bh) {
    testPutIfAbsent(container.map, testData.keys, bh);
  }
}
