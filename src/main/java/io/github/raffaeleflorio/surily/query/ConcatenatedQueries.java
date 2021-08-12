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

import io.github.raffaeleflorio.surily.PercentEncoded;
import io.github.raffaeleflorio.surily.set.DiffSet;
import io.github.raffaeleflorio.surily.set.QueryCharacters;

import java.nio.charset.Charset;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * RFC3986 compliant concatenated {@link QueryComponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class ConcatenatedQueries implements QueryComponent {
  /**
   * Builds the concatenated query parameter with the ampersand as delimiter
   *
   * @param components The query components
   * @since 1.0.0
   */
  public ConcatenatedQueries(final Iterable<QueryComponent> components) {
    this(components, '&');
  }

  /**
   * Builds the concatenated query parameter with a custom delimiter
   *
   * @param components The query components
   * @param delimiter  The delimiter to use
   * @since 1.0.0
   */
  public ConcatenatedQueries(final Iterable<QueryComponent> components, final Character delimiter) {
    this(
      components,
      delimiter,
      new DiffSet<>(
        new QueryCharacters(),
        Set.of('%')
      ),
      (component, charset) -> component
        .encoded(charset)
        .toString()
        .replace(delimiter.toString(), new PercentEncoded(delimiter.toString(), charset, new DiffSet<>(new QueryCharacters(), Set.of(delimiter))))
    );
  }

  /**
   * Builds the concatenated query parameter with a custom delimiter
   *
   * @param components          The query components
   * @param delimiter           The delimiter to use
   * @param allowedDelimiters   The allowed delimiters
   * @param componentEncodingFn The component encoding function
   * @since 1.0.0
   */
  ConcatenatedQueries(final Iterable<QueryComponent> components, final Character delimiter, final Set<Character> allowedDelimiters, final BiFunction<QueryComponent, Charset, CharSequence> componentEncodingFn) {
    this.components = components;
    this.delimiter = delimiter;
    this.allowedDelimiters = allowedDelimiters;
    this.componentEncodingFn = componentEncodingFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return components()
      .map(component -> componentEncodingFn.apply(component, charset))
      .collect(Collectors.joining(delimiter.toString()));
  }

  private Stream<QueryComponent> components() {
    assertValidDelimiter();
    return StreamSupport.stream(components.spliterator(), false);
  }

  private void assertValidDelimiter() {
    if (!allowedDelimiters.contains(delimiter)) {
      throw new IllegalStateException(String.format("Illegal delimiter: <%s>", delimiter));
    }
  }

  @Override
  public String asString() {
    return components().map(QueryComponent::asString).collect(Collectors.joining(delimiter.toString()));
  }

  @Override
  public <T> T ifDefinedElse(final Function<QueryComponent, T> fn, final Supplier<T> undefinedFn) {
    return fn.apply(this);
  }

  private final Iterable<QueryComponent> components;
  private final Character delimiter;
  private final Set<Character> allowedDelimiters;
  private final BiFunction<QueryComponent, Charset, CharSequence> componentEncodingFn;
}
