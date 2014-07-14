package com.bin01.suggestor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RmqSparseTableTest {

  @Test
  public void test() {
    int[] weights = new int[] {2, 1, 3, 4, 6, 0};
    RmqSegmentTree rmq = new RmqSegmentTree(weights);
    assertEquals(4, weights[rmq.query(Slice.from(0, 3))]);
  }

}
