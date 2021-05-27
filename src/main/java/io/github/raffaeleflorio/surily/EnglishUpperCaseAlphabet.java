/*
   Copyright 2021 Raffaele Florio

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package io.github.raffaeleflorio.surily;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * English upper-case alphabet (i.e. A-Z)
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
final class EnglishUpperCaseAlphabet extends AbstractSet<Character> {
  /**
   * Builds the alphabet
   */
  EnglishUpperCaseAlphabet() {
    this(
      Set.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
    );
  }

  private EnglishUpperCaseAlphabet(final Set<Character> alphabet) {
    this.alphabet = alphabet;
  }

  @Override
  public Iterator<Character> iterator() {
    return alphabet.iterator();
  }

  @Override
  public int size() {
    return alphabet.size();
  }

  private final Set<Character> alphabet;
}
