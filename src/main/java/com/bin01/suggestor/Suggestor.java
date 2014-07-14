package com.bin01.suggestor;

import java.util.stream.Stream;

public interface Suggestor {
  
  Stream<String> suggest(String prefix, int limit);

}
