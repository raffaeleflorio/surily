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
package io.github.raffaeleflorio.surily.characters;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Digits set (i.e. 0-9)
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class Digits extends AbstractSet<Character> {
  /**
   * Builds the digits set
   *
   * @since 1.0.0
   */
  public Digits() {
    this(
      Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    );
  }

  private Digits(final Set<Character> digits) {
    this.digits = digits;
  }

  @Override
  public Iterator<Character> iterator() {
    return digits.iterator();
  }

  @Override
  public int size() {
    return digits.size();
  }

  private final Set<Character> digits;
}
