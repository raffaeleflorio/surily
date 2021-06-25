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
 * The most common used resource identifier. It is either a URI or a relative reference.
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-4.1">RFC3986 definition</a>
 * @since 1.0.0
 */
public interface UriReference {
  /**
   * Builds the ASCII representation with percent encoding applied when needed
   *
   * @param charset The charset to use to get bytes of non-ASCII characters
   * @return The encoded representation
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
   * Builds the scheme component
   *
   * @return The scheme component
   * @since 1.0.0
   */
  SchemeComponent scheme();

  /**
   * Builds the authority component
   *
   * @return The authority component
   * @since 1.0.0
   */
  AuthorityComponent authority();

  /**
   * Builds the path component
   *
   * @return The path component
   * @since 1.0.0
   */
  PathComponent path();

  /**
   * Builds the query component
   *
   * @return The query component
   * @since 1.0.0
   */
  QueryComponent query();

  /**
   * Builds the fragment component
   *
   * @return The fragment component
   * @since 1.0.0
   */
  FragmentComponent fragment();
}
