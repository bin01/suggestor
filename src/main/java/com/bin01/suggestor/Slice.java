package com.bin01.suggestor;

public class Slice {
  private final int low;
  private final int high;

  public Slice(int low, int high) {
    if (low > high) {
      throw new IllegalArgumentException(low + " > " + high);
    }
    if (low < 0) {
      throw new IllegalArgumentException(low + " < 0");
    }
    this.low = low;
    this.high = high;
  }

  public int getLow() {
    return low;
  }

  public int getHigh() {
    return high;
  }

  public static Slice from(int low, int high) {
    return new Slice(low, high);
  }
}
