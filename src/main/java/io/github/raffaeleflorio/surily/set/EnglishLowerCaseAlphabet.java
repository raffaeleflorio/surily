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
package io.github.raffaeleflorio.surily.set;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * English lower-case alphabet (i.e. a-z)
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class EnglishLowerCaseAlphabet extends AbstractSet<Character> {
  /**
   * Builds the alphabet set
   *
   * @since 1.0.0
   */
  public EnglishLowerCaseAlphabet() {
    this(
      Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
    );
  }

  private EnglishLowerCaseAlphabet(final Set<Character> alphabet) {
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
