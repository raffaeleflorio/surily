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
import java.util.function.Function;

/**
 * Segment subcomponent of a {@link PathComponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public interface PathSegmentSubcomponent extends UriComponent {
  /**
   * Uses a function if the segment is a dot segment (i.e. "." or "..") otherwise another one
   *
   * @param singleFn        The function to use if the segment is a dot segment (i.e. ".")
   * @param doubleFn        The function to use if the segment is a double dot segment (i.e. "..")
   * @param normalSegmentFn The function to use if the segment is a normal segment
   * @param <T>             The result type
   * @return The result
   * @since 1.0.0
   */
  <T> T ifDotElse(
    Function<PathSegmentSubcomponent, T> singleFn,
    Function<PathSegmentSubcomponent, T> doubleFn,
    Function<PathSegmentSubcomponent, T> normalSegmentFn
  );

  /**
   * A {@link PathSegmentSubcomponent} for testing purpose
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  final class Fake implements PathSegmentSubcomponent {
    /**
     * Builds a fake for testing
     *
     * @param encoded  The encoded representation
     * @param asString The asString representation
     * @since 1.0.0
     */
    public Fake(final CharSequence encoded, final String asString) {
      this.encoded = encoded;
      this.asString = asString;
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
    public <T> T ifDotElse(
      Function<PathSegmentSubcomponent, T> singleFn,
      Function<PathSegmentSubcomponent, T> doubleFn,
      Function<PathSegmentSubcomponent, T> normalSegmentFn
    ) {
      return normalSegmentFn.apply(this);
    }

    private final CharSequence encoded;
    private final String asString;
  }
}
