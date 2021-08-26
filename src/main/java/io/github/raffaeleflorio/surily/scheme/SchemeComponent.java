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
package io.github.raffaeleflorio.surily.scheme;

import io.github.raffaeleflorio.surily.UriComponent;

import java.nio.charset.Charset;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Scheme component of an URI
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-3.1">RFC3986 about scheme component</a>
 * @since 1.0.0
 */
public interface SchemeComponent extends UriComponent {
  /**
   * Uses a function if the scheme is defined otherwise another one
   *
   * @param fn          The function used when the scheme is defined
   * @param undefinedFn The supplier used when the scheme is undefined
   * @param <T>         The result type
   * @return The result
   */
  <T> T ifDefinedElse(Function<SchemeComponent, T> fn, Supplier<T> undefinedFn);

  /**
   * {@link SchemeComponent} for testing purpose
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  final class Fake implements SchemeComponent {
    /**
     * Builds a fake
     *
     * @param encoded  The encoded representation
     * @param asString The asString representation
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
    public <T> T ifDefinedElse(final Function<SchemeComponent, T> fn, final Supplier<T> undefinedFn) {
      return fn.apply(this);
    }

    private final UriComponent origin;
  }
}
