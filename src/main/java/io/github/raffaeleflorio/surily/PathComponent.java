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

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Path component of an URI
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-3.3">RFC3986 about path component</a>
 * @since 1.0.0
 */
public interface PathComponent extends UriComponent, Iterable<PathSegmentSubcomponent> {
  /**
   * Uses a function if the path is absolute otherwise another one
   *
   * @param fn         The function used when the path is absolute
   * @param relativeFn The function used when the path is relative
   * @param <T>        The result type
   * @return The result
   */
  <T> T ifAbsoluteElse(Function<PathComponent, T> fn, Function<PathComponent, T> relativeFn);

  /**
   * {@link PathComponent} for testing purpose
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  final class Fake implements PathComponent {
    /**
     * Builds a fake
     *
     * @param encoded  The encoded representation
     * @param asString The asString representation
     * @param segments The path segments
     * @since 1.0.0
     */
    public Fake(final CharSequence encoded, final String asString, final List<PathSegmentSubcomponent> segments) {
      this.encoded = encoded;
      this.asString = asString;
      this.segments = segments;
    }

    @Override
    public CharSequence encoded(final Charset charset) {
      return encoded;
    }

    @Override
    public String asString() {
      return asString;
    }

    @Override
    public Iterator<PathSegmentSubcomponent> iterator() {
      return segments.iterator();
    }

    @Override
    public <T> T ifAbsoluteElse(final Function<PathComponent, T> fn, final Function<PathComponent, T> relativeFn) {
      return encoded.toString().startsWith("/") ? fn.apply(this) : relativeFn.apply(this);
    }

    private final CharSequence encoded;
    private final String asString;
    private final List<PathSegmentSubcomponent> segments;
  }
}
