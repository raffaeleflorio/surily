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

/**
 * An URI component
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-3">RFC3986 about components</a>
 * @since 1.0.0
 */
public interface UriComponent {
  /**
   * Builds the ASCII representation with percent encoding applied when needed
   *
   * @param charset The charset to use to get bytes of non-ASCII character
   * @return The ASCII representation
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-2.1">RFC3986 about percent encoding</a>
   * @since 1.0.0
   */
  CharSequence encoded(Charset charset);

  /**
   * Builds the unencoded representation
   *
   * @return The unencoded representation
   * @since 1.0.0
   */
  String asString();

  /**
   * {@link UriComponent} for testing purpose
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  final class Fake implements UriComponent {
    /**
     * Builds a fake with its representations
     *
     * @param encoded  The encoded representation
     * @param asString The String representation
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

    private final CharSequence encoded;
    private final String asString;
  }
}
