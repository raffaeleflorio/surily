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
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * RFC3986 compliant array {@link QueryComponent} like: key=value0&key=value1 or key[]=value0&key[]=value1
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class ArrayQuery implements QueryComponent {
  /**
   * Builds a query array
   *
   * @param key    The key
   * @param values The values
   * @since 1.0.0
   */
  public ArrayQuery(final CharSequence key, final Iterable<CharSequence> values) {
    this(key, values, '=');
  }

  /**
   * Builds a query array with a custom key-value delimiter
   *
   * @param key               The key
   * @param values            The values
   * @param keyValueDelimiter The key-value delimiter
   * @since 1.0.0
   */
  public ArrayQuery(final CharSequence key, final Iterable<CharSequence> values, final Character keyValueDelimiter) {
    this(key, values, keyValueDelimiter, '&');
  }

  /**
   * Builds a query array with custom delimiters
   *
   * @param key               The key
   * @param values            The values
   * @param keyValueDelimiter The key-value delimiter
   * @param pairsDelimiter    The pars delimiter
   * @since 1.0.0
   */
  public ArrayQuery(final CharSequence key, final Iterable<CharSequence> values, final Character keyValueDelimiter, final Character pairsDelimiter) {
    this(
      key,
      values,
      (k, v) -> new ConcatenatedQueries(
        StreamSupport.stream(v.spliterator(), false)
          .map(value -> new PairQuery(k, value, keyValueDelimiter))
          .collect(Collectors.toUnmodifiableList()),
        pairsDelimiter
      )
    );
  }

  /**
   * Builds a query array
   *
   * @param key            The key
   * @param values         The values
   * @param concatenatedFn The function used to concatenate each key-value pair
   * @since 1.0.0
   */
  ArrayQuery(final CharSequence key, final Iterable<CharSequence> values, final BiFunction<CharSequence, Iterable<CharSequence>, QueryComponent> concatenatedFn) {
    this.key = key;
    this.values = values;
    this.concatenatedFn = concatenatedFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return concatenatedFn.apply(key, values).encoded(charset);
  }

  @Override
  public String asString() {
    return concatenatedFn.apply(key, values).asString();
  }

  @Override
  public <T> T ifDefinedElse(final Function<QueryComponent, T> fn, final Supplier<T> undefinedFn) {
    return fn.apply(this);
  }

  private final CharSequence key;
  private final Iterable<CharSequence> values;
  private final BiFunction<CharSequence, Iterable<CharSequence>, QueryComponent> concatenatedFn;
}
