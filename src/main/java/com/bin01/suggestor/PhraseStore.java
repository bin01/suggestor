package com.bin01.suggestor;

import java.util.Optional;

public interface PhraseStore {

  Phrase get(int pos);

  Optional<Slice> sliceByPrefix(String prefix);

  int size();
  
}
