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
import io.github.raffaeleflorio.surily.authority.UndefinedAuthority;
import io.github.raffaeleflorio.surily.fragment.FragmentComponent;
import io.github.raffaeleflorio.surily.fragment.UndefinedFragment;
import io.github.raffaeleflorio.surily.path.EmptyPath;
import io.github.raffaeleflorio.surily.path.NormalizedSegments;
import io.github.raffaeleflorio.surily.path.PathComponent;
import io.github.raffaeleflorio.surily.path.PathSegmentSubcomponent;
import io.github.raffaeleflorio.surily.query.QueryComponent;
import io.github.raffaeleflorio.surily.query.UndefinedQuery;
import io.github.raffaeleflorio.surily.scheme.SchemeComponent;

import java.nio.charset.Charset;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * RFC3986 compliant URI
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-3">RFC3986 definition</a>
 * @since 1.0.0
 */
public final class Uri implements UriReference {
  /**
   * Builds an URI
   *
   * @param scheme The scheme
   * @since 1.0.0
   */
  public Uri(final SchemeComponent scheme) {
    this(scheme, new UndefinedAuthority());
  }

  /**
   * Builds an URI
   *
   * @param scheme    The scheme
   * @param authority The authority
   * @since 1.0.0
   */
  public Uri(final SchemeComponent scheme, final AuthorityComponent authority) {
    this(scheme, authority, new EmptyPath());
  }

  /**
   * Builds an URI
   *
   * @param scheme    The scheme
   * @param authority The authority
   * @param path      The path
   * @since 1.0.0
   */
  public Uri(final SchemeComponent scheme, final AuthorityComponent authority, final PathComponent path) {
    this(scheme, authority, path, new UndefinedQuery());
  }

  /**
   * Builds an URI
   *
   * @param scheme    The scheme
   * @param authority The authority
   * @param path      The path
   * @param query     The query
   * @since 1.0.0
   */
  public Uri(
    final SchemeComponent scheme,
    final AuthorityComponent authority,
    final PathComponent path,
    final QueryComponent query
  ) {
    this(scheme, authority, path, query, new UndefinedFragment());
  }

  /**
   * Builds an URI
   *
   * @param scheme    The scheme
   * @param authority The authority
   * @param path      The path
   * @param query     The query
   * @param fragment  The fragment
   * @since 1.0.0
   */
  public Uri(
    final SchemeComponent scheme,
    final AuthorityComponent authority,
    final PathComponent path,
    final QueryComponent query,
    final FragmentComponent fragment
  ) {
    this(
      scheme,
      authority,
      path,
      query,
      fragment,
      FormattedComponents::new,
      JoinedComponents::new,
      NormalizedSegments::new
    );
  }

  /**
   * Builds an URI
   *
   * @param scheme The scheme
   * @param path   The path
   * @since 1.0.0
   */
  public Uri(final SchemeComponent scheme, final PathComponent path) {
    this(scheme, new UndefinedAuthority(), path);
  }

  /**
   * Builds an URI
   *
   * @param scheme   The scheme
   * @param path     The path
   * @param fragment The fragment
   * @since 1.0.0
   */
  public Uri(final SchemeComponent scheme, final PathComponent path, final FragmentComponent fragment) {
    this(scheme, new UndefinedAuthority(), path, new UndefinedQuery(), fragment);
  }

  /**
   * Builds an URI
   *
   * @param scheme The scheme
   * @param path   The path
   * @param query  The query
   * @since 1.0.0
   */
  public Uri(final SchemeComponent scheme, final PathComponent path, final QueryComponent query) {
    this(scheme, new UndefinedAuthority(), path, query);
  }

  /**
   * Builds an URI
   *
   * @param scheme The scheme
   * @param query  The query
   * @since 1.0.0
   */
  public Uri(final SchemeComponent scheme, final QueryComponent query) {
    this(scheme, new UndefinedAuthority(), new EmptyPath(), query);
  }

  /**
   * Builds an URI
   *
   * @param scheme    The scheme
   * @param authority The authority
   * @param query     The query
   * @since 1.0.0
   */
  public Uri(final SchemeComponent scheme, final AuthorityComponent authority, final QueryComponent query) {
    this(scheme, authority, new EmptyPath(), query);
  }

  /**
   * Builds an URI
   *
   * @param scheme   The scheme
   * @param fragment The fragment
   * @since 1.0.0
   */
  public Uri(final SchemeComponent scheme, final FragmentComponent fragment) {
    this(scheme, new UndefinedAuthority(), new EmptyPath(), new UndefinedQuery(), fragment);
  }

  /**
   * Builds an URI
   *
   * @param scheme    The scheme
   * @param authority The authority
   * @param fragment  The fragment
   * @since 1.0.0
   */
  public Uri(final SchemeComponent scheme, final AuthorityComponent authority, final FragmentComponent fragment) {
    this(scheme, authority, new EmptyPath(), new UndefinedQuery(), fragment);
  }

  /**
   * Builds an URI
   *
   * @param scheme    The scheme
   * @param authority The authority
   * @param path      The path
   * @param fragment  The fragment
   * @since 1.0.0
   */
  public Uri(
    final SchemeComponent scheme,
    final AuthorityComponent authority,
    final PathComponent path,
    final FragmentComponent fragment
  ) {
    this(scheme, authority, path, new UndefinedQuery(), fragment);
  }

  /**
   * Builds an URI
   *
   * @param scheme   The scheme
   * @param query    The query
   * @param fragment The fragment
   * @since 1.0.0
   */
  public Uri(final SchemeComponent scheme, final QueryComponent query, final FragmentComponent fragment) {
    this(scheme, new UndefinedAuthority(), new EmptyPath(), query, fragment);
  }

  /**
   * Builds an URI
   *
   * @param scheme    The scheme
   * @param authority The authority
   * @param query     The query
   * @param fragment  The fragment
   * @since 1.0.0
   */
  public Uri(
    final SchemeComponent scheme,
    final AuthorityComponent authority,
    final QueryComponent query,
    final FragmentComponent fragment
  ) {
    this(scheme, authority, new EmptyPath(), query, fragment);
  }

  /**
   * Builds an URI
   *
   * @param scheme      The scheme
   * @param authority   The authority
   * @param path        The path
   * @param query       The query
   * @param fragment    The fragment
   * @param formattedFn The function to format components
   * @param joinedFn    The function to join components
   * @since 1.0.0
   */
  Uri(
    final SchemeComponent scheme,
    final AuthorityComponent authority,
    final PathComponent path,
    final QueryComponent query,
    final FragmentComponent fragment,
    final BiFunction<String, List<UriComponent>, UriComponent> formattedFn,
    final BiFunction<List<UriComponent>, String, UriComponent> joinedFn,
    final Function<PathComponent, List<PathSegmentSubcomponent>> normalizedSegmentsFn
  ) {
    this.scheme = scheme;
    this.authority = authority;
    this.path = path;
    this.query = query;
    this.fragment = fragment;
    this.formattedFn = formattedFn;
    this.joinedFn = joinedFn;
    this.normalizedSegmentsFn = normalizedSegmentsFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return joinedComponents().encoded(charset);
  }

  private UriComponent joinedComponents() {
    return joinedFn.apply(formattedComponents(), "");
  }

  private List<UriComponent> formattedComponents() {
    return Stream.of(formattedScheme(), hierPart(), formattedQuery(), formattedFragment())
      .flatMap(Function.identity())
      .collect(Collectors.toUnmodifiableList());
  }

  private Stream<UriComponent> formattedScheme() {
    return Stream.of(formattedFn.apply("%s:", List.of(scheme)));
  }

  private Stream<UriComponent> hierPart() {
    return Stream.of(
      authority.<UriComponent>ifDefinedElse(path::hierPart, path::hierPart)
    );
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
      Stream::empty
    );
  }

  @Override
  public String asString() {
    return joinedComponents().asString();
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

  private final SchemeComponent scheme;
  private final AuthorityComponent authority;
  private final PathComponent path;
  private final QueryComponent query;
  private final FragmentComponent fragment;
  private final BiFunction<String, List<UriComponent>, UriComponent> formattedFn;
  private final BiFunction<List<UriComponent>, String, UriComponent> joinedFn;
  private final Function<PathComponent, List<PathSegmentSubcomponent>> normalizedSegmentsFn;
}
