package com.bin01.suggestor;

import java.util.List;
import java.util.Optional;

public class InMemoryPhraseStore implements PhraseStore {
  private final List<Phrase> phrases;

  /**
   * Assumes that the phrases are given in a sorted fashion
   * 
   * @param phrases
   */
  public InMemoryPhraseStore(List<Phrase> phrases) {
    this.phrases = phrases;
  }

  @Override
  public Phrase get(int pos) {
    if (pos < 0 && pos >= size()) {
      throw new IllegalArgumentException("Invalid pos " + pos);
    }
    return phrases.get(pos);
  }

  @Override
  public Optional<Slice> sliceByPrefix(String prefix) {
    int low = findFirst(prefix);
    if(low == -1) {
      return Optional.empty();
    }
    int high = findLast(prefix);
    return Optional.of(Slice.from(low, high));
  }

  @Override
  public int size() {
    return phrases.size();
  }

  /**
   * Performs binary search to find the first phrase that matches the prefix
   * 
   * @param prefix the desired prefix
   * @return index of the first matching phrase
   */
  private int findFirst(String prefix) {
    int low = 0;
    int high = this.phrases.size() - 1;
    while (low <= high) {
      int mid = (low + high) >>> 1;
      if (this.phrases.get(mid).getPhrase().startsWith(prefix)) {
        if (mid == 0 || !this.phrases.get(mid - 1).getPhrase().startsWith(prefix)) {
          return mid;
        } else {
          high = mid - 1;
        }
      } else if (prefix.compareTo(this.phrases.get(mid).getPhrase()) > 0) {
        low = mid + 1;
      } else {
        high = mid - 1;
      }
    }
    return low;
  }

  /**
   * Performs binary search to find the last phrase that matches the prefix
   * 
   * @param prefix the desired prefix
   * @return index of the last matching phrase
   */
  private int findLast(String prefix) {
    int low = 0;
    int high = this.phrases.size() - 1;
    while (low <= high) {
      int mid = (low + high) >>> 1;
      if (this.phrases.get(mid).getPhrase().startsWith(prefix)) {
        if (mid == this.phrases.size() - 1 || !this.phrases.get(mid + 1).getPhrase().startsWith(prefix)) {
          return mid;
        } else {
          low = mid + 1;
        }
      } else if (prefix.compareTo(this.phrases.get(mid).getPhrase()) > 0) {
        low = mid + 1;
      } else {
        high = mid - 1;
      }
    }
    return high;
  }
}
