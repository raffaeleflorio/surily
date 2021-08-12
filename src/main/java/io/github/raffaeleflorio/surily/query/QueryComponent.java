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

import io.github.raffaeleflorio.surily.UriComponent;

import java.nio.charset.Charset;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Query component of an URI
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-3.4">RFC3986 about query component</a>
 * @since 1.0.0
 */
public interface QueryComponent extends UriComponent {
  /**
   * Uses a function if the query is defined otherwise another one
   *
   * @param fn          The function used when the query is defined
   * @param undefinedFn The supplier used when the query is undefined
   * @param <T>         The result type
   * @return The result
   */
  <T> T ifDefinedElse(Function<QueryComponent, T> fn, Supplier<T> undefinedFn);

  /**
   * {@link QueryComponent} for testing purpose
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  final class Fake implements QueryComponent {
    /**
     * Builds a fake with its representations
     *
     * @param encoded  The encoded representation
     * @param asString The String representation
     * @since 1.0.0
     */
    public Fake(final CharSequence encoded, final String asString) {
      this(new UriComponent.Fake(encoded, asString));
    }

    /**
     * Builds a fake with its representations from an {@link UriComponent}
     *
     * @param origin The component
     * @since 1.0.0
     */
    public Fake(final UriComponent origin) {
      this.origin = origin;
    }

    @Override
    public CharSequence encoded(final Charset charset) {
      return origin.encoded(charset);
    }

    @Override
    public String asString() {
      return origin.asString();
    }

    @Override
    public <T> T ifDefinedElse(final Function<QueryComponent, T> fn, final Supplier<T> undefinedFn) {
      return fn.apply(this);
    }

    private final UriComponent origin;
  }
}
