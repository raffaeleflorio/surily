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
import java.util.function.BiFunction;

/**
 * RFC3986 compliant relative-ref {@link UriReference}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-4.2">RFC3986 definition</a>
 * @since 1.0.0
 */
public final class RelativeRef implements UriReference {
  /**
   * Builds an empty relative-ref
   *
   * @since 1.0.0
   */
  public RelativeRef() {
    this(new RelativePath());
  }

  /**
   * Builds a relative-ref with undefined authority, query and fragment
   *
   * @param path The path
   * @since 1.0.0
   */
  public RelativeRef(final PathComponent path) {
    this(new UndefinedAuthority(), path);
  }

  /**
   * Builds a relative-ref with an empty path and undefined fragment and query
   *
   * @param authority The authority
   * @since 1.0.0
   */
  public RelativeRef(final AuthorityComponent authority) {
    this(authority, new AbsolutePath());
  }

  /**
   * Builds a relative-ref with undefined fragment and query
   *
   * @param authority The authority
   * @param path      The path
   * @since 1.0.0
   */
  public RelativeRef(final AuthorityComponent authority, final PathComponent path) {
    this(authority, path, new UndefinedQuery());
  }

  /**
   * Builds a relative-ref with an undefined fragment
   *
   * @param authority The authority
   * @param path      The path
   * @param query     The query
   * @since 1.0.0
   */
  public RelativeRef(final AuthorityComponent authority, final PathComponent path, final QueryComponent query) {
    this(authority, path, query, new UndefinedFragment());
  }

  /**
   * Builds a relative-ref
   *
   * @param authority The authority
   * @param path      The path
   * @param query     The query
   * @param fragment  The fragment
   * @since 1.0.0
   */
  public RelativeRef(
    final AuthorityComponent authority,
    final PathComponent path,
    final QueryComponent query,
    final FragmentComponent fragment
  ) {
    this(authority, path, query, fragment, RelativePart::new);
  }

  /**
   * Builds a relative-ref
   *
   * @param path     The path
   * @param query    The query
   * @param fragment The fragment
   * @since 1.0.0
   */
  public RelativeRef(
    final PathComponent path,
    final QueryComponent query,
    final FragmentComponent fragment
  ) {
    this(new UndefinedAuthority(), path, query, fragment);
  }

  /**
   * Builds a relative-ref
   *
   * @param fragment The fragment
   * @since 1.0.0
   */
  public RelativeRef(final FragmentComponent fragment) {
    this(new UndefinedAuthority(), new RelativePath(), new UndefinedQuery(), fragment);
  }

  /**
   * Builds a relative-ref
   *
   * @param query The query
   * @since 1.0.0
   */
  public RelativeRef(final QueryComponent query) {
    this(new UndefinedAuthority(), new RelativePath(), query, new UndefinedFragment());
  }


  /**
   * Builds a relative-ref
   *
   * @param authority      The authority
   * @param path           The path
   * @param query          The query
   * @param fragment       The fragment
   * @param relativePartFn The function to build a relative-part
   * @since 1.0.0
   */
  RelativeRef(
    final AuthorityComponent authority,
    final PathComponent path,
    final QueryComponent query,
    final FragmentComponent fragment,
    final BiFunction<AuthorityComponent, PathComponent, UriComponent> relativePartFn
  ) {
    this.authority = authority;
    this.path = path;
    this.query = query;
    this.fragment = fragment;
    this.relativePartFn = relativePartFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return concatenated(
      relativePartFn.apply(authority, path).encoded(charset),
      query.ifDefinedElse(x -> x.encoded(charset), () -> ""),
      fragment.ifDefinedElse(x -> x.encoded(charset), () -> "")
    );
  }

  private String concatenated(final CharSequence relativePart, final CharSequence query, final CharSequence fragment) {
    return String.format(
      "%s%s%s",
      relativePart,
      query.length() == 0 ? "" : "?".concat(query.toString()),
      fragment.length() == 0 ? "" : "#".concat(fragment.toString())
    );
  }

  @Override
  public String asString() {
    return concatenated(
      relativePartFn.apply(authority, path).asString(),
      query.ifDefinedElse(QueryComponent::asString, () -> ""),
      fragment.ifDefinedElse(FragmentComponent::asString, () -> "")
    );
  }

  @Override
  public SchemeComponent scheme() {
    return new UndefinedScheme();
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

  private final AuthorityComponent authority;
  private final PathComponent path;
  private final QueryComponent query;
  private final FragmentComponent fragment;
  private final BiFunction<AuthorityComponent, PathComponent, UriComponent> relativePartFn;
}
