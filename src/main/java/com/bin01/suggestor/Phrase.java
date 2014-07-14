package com.bin01.suggestor;

import java.util.Comparator;

public class Phrase {
  private final String phrase;
  private final int weight;

  public Phrase(String phrase, int weight) {
    this.phrase = phrase;
    this.weight = weight;
  }

  public String getPhrase() {
    return phrase;
  }

  public int getWeight() {
    return weight;
  }
  
  public static class PhraseComparator implements Comparator<Phrase> {

    @Override
    public int compare(Phrase o1, Phrase o2) {
      return o1.phrase.compareTo(o2.phrase);
    }
    
  }
  
  public static class PhraseWeightComparator implements Comparator<Phrase> {

    @Override
    public int compare(Phrase o1, Phrase o2) {
      return o1.weight - o2.weight;
    }
    
  }
}
