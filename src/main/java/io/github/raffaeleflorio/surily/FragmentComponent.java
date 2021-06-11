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
import java.util.function.Supplier;

/**
 * Fragment component of an URI
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-3.5">RFC3986 about fragment component</a>
 * @since 1.0.0
 */
public interface FragmentComponent extends UriComponent {
  /**
   * Uses a function if the fragment is defined otherwise another one
   *
   * @param fn          The function used when the fragment is defined
   * @param undefinedFn The supplier used when the fragment is undefined
   * @param <T>         The result type
   * @return The result
   */
  <T> T ifDefinedElse(Function<FragmentComponent, T> fn, Supplier<T> undefinedFn);

  /**
   * {@link FragmentComponent} for testing purpose
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  final class Fake implements FragmentComponent {
    /**
     * Builds a fake
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
    public <T> T ifDefinedElse(final Function<FragmentComponent, T> fn, final Supplier<T> undefinedFn) {
      return fn.apply(this);
    }

    @Override
    public CharSequence encoded(final Charset charset) {
      return encoded;
    }

    @Override
    public String asString() {
      return asString;
    }

    private final CharSequence encoded;
    private final String asString;
  }
}
