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
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    this(new EmptyPath());
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
    this(authority, new EmptyPath());
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
    this(new UndefinedAuthority(), new EmptyPath(), new UndefinedQuery(), fragment);
  }

  /**
   * Builds a relative-ref
   *
   * @param query The query
   * @since 1.0.0
   */
  public RelativeRef(final QueryComponent query) {
    this(new UndefinedAuthority(), new EmptyPath(), query, new UndefinedFragment());
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
    this(authority, path, query, fragment, FormattedComponents::new, JoinedComponents::new);
  }

  /**
   * Builds a relative-ref
   *
   * @param authority   The authority
   * @param path        The path
   * @param query       The query
   * @param fragment    The fragment
   * @param formattedFn The function to build formatted components
   * @param joinedFn    The function to build joined components
   * @since 1.0.0
   */
  RelativeRef(
    final AuthorityComponent authority,
    final PathComponent path,
    final QueryComponent query,
    final FragmentComponent fragment,
    final BiFunction<String, List<UriComponent>, UriComponent> formattedFn,
    final BiFunction<List<UriComponent>, String, UriComponent> joinedFn
  ) {
    this.authority = authority;
    this.path = path;
    this.query = query;
    this.fragment = fragment;
    this.formattedFn = formattedFn;
    this.joinedFn = joinedFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return joinedComponents().encoded(charset);
  }

  private UriComponent joinedComponents() {
    return joinedFn.apply(formattedComponents(), "");
  }

  private List<UriComponent> formattedComponents() {
    return Stream.of(relativePart(), formattedQuery(), formattedFragment())
      .flatMap(Function.identity())
      .collect(Collectors.toUnmodifiableList());
  }

  private Stream<UriComponent> relativePart() {
    return Stream.of(authority.<UriComponent>ifDefinedElse(path::relativePart, path::relativePart));
  }

  private Stream<UriComponent> formattedQuery() {
    return query.ifDefinedElse(
      x -> Stream.of(formattedFn.apply("?%s", List.of(x))),
      Stream::empty
    );
  }

  private Stream<UriComponent> formattedFragment() {
    return fragment.ifDefinedElse(
      x -> Stream.of(formattedFn.apply("#%s", List.of(x))),
      Stream::of
    );
  }

  @Override
  public String asString() {
    return joinedComponents().asString();
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
  private final BiFunction<String, List<UriComponent>, UriComponent> formattedFn;
  private final BiFunction<List<UriComponent>, String, UriComponent> joinedFn;
}
