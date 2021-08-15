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
package io.github.raffaeleflorio.surily.query;

import io.github.raffaeleflorio.surily.characters.Pchar;
import io.github.raffaeleflorio.surily.set.UnionSet;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * RFC3986 query characters (i.e. pchar / "/" / "?")
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
final class QueryCharacters extends AbstractSet<Character> {
  /**
   * Builds the characters sets
   *
   * @since 1.0.0
   */
  QueryCharacters() {
    this(
      new UnionSet<>(
        new Pchar(),
        Set.of('/', '?')
      )
    );
  }

  private QueryCharacters(final Set<Character> queryCharacters) {
    this.queryCharacters = queryCharacters;
  }

  @Override
  public Iterator<Character> iterator() {
    return queryCharacters.iterator();
  }

  @Override
  public int size() {
    return queryCharacters.size();
  }

  private final Set<Character> queryCharacters;
}
