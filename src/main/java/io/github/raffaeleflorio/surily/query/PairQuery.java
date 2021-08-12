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
import io.github.raffaeleflorio.surily.characters.DiffSet;

import java.nio.charset.Charset;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * RFC3986 compliant key-value {@link QueryComponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class PairQuery implements QueryComponent {
  /**
   * Builds a key-value query with equals as delimiter
   *
   * @param key   The key
   * @param value The value
   * @since 1.0.0
   */
  public PairQuery(final CharSequence key, final CharSequence value) {
    this(key, value, '=');
  }

  /**
   * Builds a key-value query with a custom delimiter
   *
   * @param key       The key
   * @param value     The value
   * @param delimiter The delimiter (e.g. =)
   * @since 1.0.0
   */
  PairQuery(final CharSequence key, final CharSequence value, final Character delimiter) {
    this(
      key,
      value,
      delimiter,
      (s, charset) -> new PercentEncoded(s, charset, new DiffSet<>(new QueryCharacters(), Set.of(delimiter))),
      (s, charset) -> new PercentEncoded(s, charset, new QueryCharacters()),
      new DiffSet<>(new QueryCharacters(), Set.of('%'))
    );
  }

  /**
   * Builds a key-value query
   *
   * @param key               The key
   * @param value             The value
   * @param delimiter         The delimiter (e.g. =)
   * @param keyEncoding       The key encoding function
   * @param valueEncoding     The value encoding function
   * @param allowedDelimiters The allowed delimiters
   * @since 1.0.0
   */
  PairQuery(final CharSequence key, final CharSequence value, final Character delimiter, final BiFunction<CharSequence, Charset, CharSequence> keyEncoding, final BiFunction<CharSequence, Charset, CharSequence> valueEncoding, final Set<Character> allowedDelimiters) {
    this.key = key;
    this.value = value;
    this.delimiter = delimiter;
    this.keyEncoding = keyEncoding;
    this.valueEncoding = valueEncoding;
    this.allowedDelimiters = allowedDelimiters;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return concatenated(
      keyEncoding.apply(key, charset),
      delimiter,
      valueEncoding.apply(value, charset)
    );
  }

  private String concatenated(final CharSequence key, final Character delimiter, final CharSequence value) {
    assertDelimiter();
    return String.format("%s%s%s", key, delimiter, value);
  }

  private void assertDelimiter() {
    if (!allowedDelimiters.contains(delimiter)) {
      throw new IllegalStateException(String.format("Illegal delimiter: <%s>", delimiter));
    }
  }

  @Override
  public String asString() {
    return concatenated(key, delimiter, value);
  }

  @Override
  public <T> T ifDefinedElse(final Function<QueryComponent, T> fn, final Supplier<T> undefinedFn) {
    return fn.apply(this);
  }

  private final CharSequence key;
  private final CharSequence value;
  private final Character delimiter;
  private final BiFunction<CharSequence, Charset, CharSequence> keyEncoding;
  private final BiFunction<CharSequence, Charset, CharSequence> valueEncoding;
  private final Set<Character> allowedDelimiters;
}
