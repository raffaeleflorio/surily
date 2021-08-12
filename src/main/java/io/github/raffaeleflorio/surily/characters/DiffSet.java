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
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Difference set
 *
 * @param <T> The elements type
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class DiffSet<T> extends AbstractSet<T> {
  /**
   * Builds the difference
   *
   * @param one A set
   * @param two Another set
   * @since 1.0.0
   */
  public DiffSet(final Set<T> one, final Set<T> two) {
    this.one = one;
    this.two = two;
  }

  @Override
  public Iterator<T> iterator() {
    return stream().iterator();
  }

  @Override
  public Stream<T> stream() {
    return one.stream().filter(Predicate.not(two::contains));
  }

  @Override
  public int size() {
    return Long.valueOf(stream().count()).intValue();
  }

  private final Set<T> one;
  private final Set<T> two;
}
