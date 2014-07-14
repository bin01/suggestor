package com.bin01.suggestor;

public class RmqSegmentTree implements Rmq {
  private int[] data;
  private int[] tree;

  private int nextPowerOf2(int n) {
    int p = 1;
    for (; p < n; p <<= 1);
    return p;
  }

  private void init(int root, int f, int t) {
    if (f == t) {
      tree[root] = f;
    } else {
      int m = (f + t) / 2, l = left(root), r = right(root);
      init(l, f, m);
      init(r, m + 1, t);
      tree[root] = (data[tree[l]] > data[tree[r]]) ? tree[l] : tree[r];
    }
  }

  public RmqSegmentTree(int[] a) {
    data = a;
    int n = a.length;
    int p = nextPowerOf2(n);
    tree = new int[2 * p - 1];
    init(0, 0, n - 1);
  }

  private int left(int i) {
    return 2 * i + 1;
  }

  private int right(int i) {
    return 2 * i + 2;
  }

  private int maximum(int root, int low, int high, int f, int t) {
    if (t < low || high < f)
      return -1;
    if (f <= low && high <= t)
      return tree[root];
    int l = left(root);
    int r = right(root);
    int m = (low + high) / 2;
    int l_max = maximum(l, low, m, f, t);
    int r_max = maximum(r, m + 1, high, f, t);
    if (l_max == -1)
      return r_max;
    if (r_max == -1)
      return l_max;
    return data[l_max] > data[r_max] ? l_max : r_max;
  }

  private int maximum(int f, int t) {
    return maximum(0, 0, data.length - 1, f, t);
  }

  @Override
  public int query(Slice slice) {
    return maximum(slice.getLow(), slice.getHigh());
  }
}
