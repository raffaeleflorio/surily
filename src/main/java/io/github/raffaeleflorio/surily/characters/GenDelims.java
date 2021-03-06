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
 * RFC3986 gen-delims characters (i.e. ":" / "/" / "?" / "#" / "[" / "]" / "@")
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class GenDelims extends AbstractSet<Character> {
  /**
   * Builds the gen-delims set
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  public GenDelims() {
    this(Set.of(':', '/', '?', '#', '[', ']', '@'));
  }

  private GenDelims(final Set<Character> genDelims) {
    this.genDelims = genDelims;
  }

  @Override
  public Iterator<Character> iterator() {
    return genDelims.iterator();
  }

  @Override
  public int size() {
    return genDelims.size();
  }

  private final Set<Character> genDelims;
}
