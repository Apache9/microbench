/**
 * 
 */
package com.xiaomi.infra.microbench;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 * @author zhangduo
 */
@State(Scope.Benchmark)
public class TestData {

  public static final List<byte[]> KEYS;

  static {
    int numKeys = 16384;
    Random rand = new Random(1234);
    KEYS = Stream.generate(() -> {
      byte[] key = new byte[16];
      rand.nextBytes(key);
      return key;
    }).limit(numKeys).collect(Collectors.toList());
  }
  
  public static final Object VAL = new Object();
}
