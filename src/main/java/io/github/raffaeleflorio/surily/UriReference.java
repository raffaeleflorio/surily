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

import io.github.raffaeleflorio.surily.authority.AuthorityComponent;
import io.github.raffaeleflorio.surily.fragment.FragmentComponent;
import io.github.raffaeleflorio.surily.path.PathComponent;
import io.github.raffaeleflorio.surily.query.QueryComponent;
import io.github.raffaeleflorio.surily.scheme.SchemeComponent;

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

  /**
   * {@link UriReference} for testing purpose
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  final class Fake implements UriReference {
    /**
     * Builds a fake with empty components
     *
     * @param encoded  The encoded representations
     * @param asString The asString representations
     * @since 1.0.0
     */
    public Fake(final CharSequence encoded, final String asString) {
      this(
        encoded,
        asString,
        new SchemeComponent.Fake("", ""),
        new AuthorityComponent.Fake("", ""),
        new PathComponent.Fake("", ""),
        new QueryComponent.Fake("", ""),
        new FragmentComponent.Fake("", "")
      );
    }


    /**
     * Builds a fake with empty components and representations
     *
     * @param scheme The scheme
     * @since 1.0.0
     */
    public Fake(final SchemeComponent scheme) {
      this(
        "",
        "",
        scheme,
        new AuthorityComponent.Fake("", ""),
        new PathComponent.Fake("", ""),
        new QueryComponent.Fake("", ""),
        new FragmentComponent.Fake("", "")
      );
    }

    /**
     * Builds a fake with empty components and representations
     *
     * @param authority The authority
     * @since 1.0.0
     */
    public Fake(final AuthorityComponent authority) {
      this(
        "",
        "",
        new SchemeComponent.Fake("", ""),
        authority,
        new PathComponent.Fake("", ""),
        new QueryComponent.Fake("", ""),
        new FragmentComponent.Fake("", "")
      );
    }

    /**
     * Builds a fake with empty components and representations
     *
     * @param path The path
     * @since 1.0.0
     */
    public Fake(final PathComponent path) {
      this(
        "",
        "",
        new SchemeComponent.Fake("", ""),
        new AuthorityComponent.Fake("", ""),
        path,
        new QueryComponent.Fake("", ""),
        new FragmentComponent.Fake("", "")
      );
    }

    /**
     * Builds a fake with empty components and representations
     *
     * @param query The query
     * @since 1.0.0
     */
    public Fake(final QueryComponent query) {
      this(
        "",
        "",
        new SchemeComponent.Fake("", ""),
        new AuthorityComponent.Fake("", ""),
        new PathComponent.Fake("", ""),
        query,
        new FragmentComponent.Fake("", "")
      );
    }

    /**
     * Builds a fake with empty components and representations
     *
     * @param fragment The fragment
     * @since 1.0.0
     */
    public Fake(final FragmentComponent fragment) {
      this(
        "",
        "",
        new SchemeComponent.Fake("", ""),
        new AuthorityComponent.Fake("", ""),
        new PathComponent.Fake("", ""),
        new QueryComponent.Fake("", ""),
        fragment
      );
    }

    /**
     * Builds a fake
     *
     * @param encoded   The encoded representation
     * @param asString  The asString representation
     * @param scheme    The scheme
     * @param authority The authority
     * @param path      The path
     * @param query     The query
     * @param fragment  The fragment
     * @since 1.0.0
     */
    public Fake(
      final CharSequence encoded,
      final String asString,
      final SchemeComponent scheme,
      final AuthorityComponent authority,
      final PathComponent path,
      final QueryComponent query,
      final FragmentComponent fragment
    ) {
      this.encoded = encoded;
      this.asString = asString;
      this.scheme = scheme;
      this.authority = authority;
      this.path = path;
      this.query = query;
      this.fragment = fragment;
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
    public SchemeComponent scheme() {
      return scheme;
    }

    @Override
    public AuthorityComponent authority() {
      return authority;
    }

    @Override
    public PathComponent path() {
      return path;
    }

    @Override
    public QueryComponent query() {
      return query;
    }

    @Override
    public FragmentComponent fragment() {
      return fragment;
    }

    private final CharSequence encoded;
    private final String asString;
    private final SchemeComponent scheme;
    private final AuthorityComponent authority;
    private final PathComponent path;
    private final QueryComponent query;
    private final FragmentComponent fragment;
  }
}
