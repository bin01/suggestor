package com.bin01.suggestor;

import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class DefaultSuggestor implements Suggestor {
  private final PhraseStore phraseStore;
  private final Rmq rmq;

  public DefaultSuggestor(PhraseStore phraseStore, Rmq rmq) {
    this.phraseStore = phraseStore;
    this.rmq = rmq;
  }

  @Override
  public Stream<String> suggest(String prefix, int limit) {
    Optional<Slice> slice = phraseStore.sliceByPrefix(prefix);
    if (!slice.isPresent()) {
      return Stream.empty();
    }

    int size = 0;
    Stream.Builder<String> phrasesBuilder = Stream.builder();
    PriorityQueue<PhraseRange> heap = new PriorityQueue<>();
    offerHeaviest(heap, slice.get().getLow(), slice.get().getHigh());

    while (size <= limit && !heap.isEmpty()) {
      PhraseRange heaviest = heap.poll();
      String phrase = phraseStore.get(heaviest.index).getPhrase();
      phrasesBuilder.add(phrase);
      ++size;
      
      int low = heaviest.slice.getLow();
      int high = heaviest.index - 1;

      if ((heaviest.index - 1 < heaviest.index) && low <= high) {
        offerHeaviest(heap, low, high);
      }

      low = heaviest.index + 1;
      high = heaviest.slice.getHigh();

      if ((heaviest.index + 1 > heaviest.index) && low <= high) {
        offerHeaviest(heap, low, high);
      }
    }

    return phrasesBuilder.build();
  }

  private void offerHeaviest(PriorityQueue<PhraseRange> heap, int low, int high) {
    Slice slice = Slice.from(low, high);
    int pos = rmq.query(slice);
    Phrase phrase = phraseStore.get(pos);
    heap.offer(new PhraseRange(slice, phrase.getWeight(), pos));
  }

  private static class PhraseRange implements Comparable<PhraseRange> {
    public final Slice slice;
    public final int weight;
    public final int index;

    public PhraseRange(Slice slice, int weight, int index) {
      this.slice = slice;
      this.weight = weight;
      this.index = index;
    }

    @Override
    public int compareTo(PhraseRange other) {
      return this.weight - other.weight;
    }
  }
}
