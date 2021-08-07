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
 * RFC3986 compliant absolute {@link UriReference}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-4.3">RFC3986 definition</a>
 * @since 1.0.0
 */
public final class AbsoluteUri implements UriReference {
  /**
   * Builds an absolute URI with an empty path and an undefined query
   *
   * @param scheme    The scheme
   * @param authority The authority
   * @since 1.0.0
   */
  public AbsoluteUri(final SchemeComponent scheme, final AuthorityComponent authority) {
    this(scheme, authority, new EmptyPath());
  }

  /**
   * Builds an absolute URI with an undefined query component
   *
   * @param scheme    The scheme
   * @param authority The authority
   * @param path      The path
   * @since 1.0.0
   */
  public AbsoluteUri(final SchemeComponent scheme, final AuthorityComponent authority, final PathComponent path) {
    this(scheme, authority, path, new UndefinedQuery());
  }

  /**
   * Builds an absolute URI with undefined authority and query
   *
   * @param scheme The scheme
   * @param path   The path
   * @since 1.0.0
   */
  public AbsoluteUri(final SchemeComponent scheme, final PathComponent path) {
    this(scheme, new UndefinedAuthority(), path);
  }

  /**
   * Builds an absolute URI with an empty path
   *
   * @param scheme    The scheme
   * @param authority The authority
   * @param query     The query
   * @since 1.0.0
   */
  public AbsoluteUri(final SchemeComponent scheme, final AuthorityComponent authority, final QueryComponent query) {
    this(scheme, authority, new EmptyPath(), query);
  }

  /**
   * Builds an absolute URI with an undefined authority and an empty path
   *
   * @param scheme The scheme
   * @param query  The query
   * @since 1.0.0
   */
  public AbsoluteUri(final SchemeComponent scheme, final QueryComponent query) {
    this(scheme, new UndefinedAuthority(), new EmptyPath(), query);
  }

  /**
   * Builds an absolute URI
   *
   * @param scheme    The scheme
   * @param authority The authority
   * @param path      The path
   * @param query     The query
   * @since 1.0.0
   */
  public AbsoluteUri(
    final SchemeComponent scheme,
    final AuthorityComponent authority,
    final PathComponent path,
    final QueryComponent query
  ) {
    this(scheme, authority, path, query, FormattedComponents::new, JoinedComponents::new);
  }

  /**
   * Builds an absolute URI
   *
   * @param scheme      The scheme
   * @param authority   The authority
   * @param path        The path
   * @param query       The query
   * @param formattedFn The function to format components
   * @param joinedFn    The function to join components
   * @since 1.0.0
   */
  AbsoluteUri(
    final SchemeComponent scheme,
    final AuthorityComponent authority,
    final PathComponent path,
    final QueryComponent query,
    final BiFunction<String, List<UriComponent>, UriComponent> formattedFn,
    final BiFunction<List<UriComponent>, String, UriComponent> joinedFn
  ) {
    this.scheme = scheme;
    this.authority = authority;
    this.path = path;
    this.query = query;
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
    return Stream.of(formattedScheme(), hierPart(), formattedQuery())
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
    return new UndefinedFragment();
  }

  private final SchemeComponent scheme;
  private final AuthorityComponent authority;
  private final PathComponent path;
  private final QueryComponent query;
  private final BiFunction<String, List<UriComponent>, UriComponent> formattedFn;
  private final BiFunction<List<UriComponent>, String, UriComponent> joinedFn;
}
