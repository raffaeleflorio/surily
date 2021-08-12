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
 * RFC3986 reserved characters (i.e. gen-delims / sub-delims)
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class ReservedCharacters extends AbstractSet<Character> {
  /**
   * Builds the reserved set
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  public ReservedCharacters() {
    this(new UnionSet<>(new GenDelims(), new SubDelims()));
  }

  private ReservedCharacters(final Set<Character> reserved) {
    this.reserved = reserved;
  }

  @Override
  public Iterator<Character> iterator() {
    return reserved.iterator();
  }

  @Override
  public int size() {
    return reserved.size();
  }

  private final Set<Character> reserved;
}
