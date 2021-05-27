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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ASCII {@link CharSequence} with percent-encoding applied where needed
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-2.1">RFC3986 about percent-encoding</a>
 * @see <a href="https://en.wikipedia.org/wiki/Percent-encoding">Wikipedia about percent-encoding</a>
 * @since 1.0.0
 */
public final class PercentEncoded implements CharSequence {
  /**
   * Builds the percent-encoded char sequence with UTF-8 to get bytes of non-ASCII characters and all RFC3986 unreserved characters
   *
   * @param origin The char sequence to decorate
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-2.3">RFC3986 unreserved characters</a>
   * @since 1.0.0
   */
  public PercentEncoded(final CharSequence origin) {
    this(origin, StandardCharsets.UTF_8);
  }

  /**
   * Builds the percent-encoded char sequence with all RFC3986 unreserved characters
   *
   * @param origin  The char sequence to decorate
   * @param charset The charset to get bytes of non-ASCII characters
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-2.3">RFC3986 unreserved characters</a>
   * @since 1.0.0
   */
  public PercentEncoded(final CharSequence origin, final Charset charset) {
    this(
      origin,
      charset,
      new UnreservedCharacters()
    );
  }

  /**
   * Builds the percent-encoded char sequence
   *
   * @param origin     The char sequence to decorate
   * @param charset    The charset to get bytes of non-ASCII characters
   * @param unreserved The reserved ASCII characters set
   * @since 1.0.0
   */
  public PercentEncoded(final CharSequence origin, final Charset charset, final Set<Character> unreserved) {
    this(origin, charset, unreserved, List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'));
  }

  /**
   * Builds the percent-encoded char sequence
   *
   * @param origin     The char sequence to decorate
   * @param charset    The charset to get bytes of non-ASCII characters
   * @param unreserved The unreserved characters set
   * @param hexChars   The hexadecimal chars
   * @since 1.0.0
   */
  PercentEncoded(final CharSequence origin, final Charset charset, final Set<Character> unreserved, final List<Character> hexChars) {
    this.origin = origin;
    this.charset = charset;
    this.unreserved = unreserved;
    this.hexChars = hexChars;
  }

  @Override
  public int length() {
    return toString().length();
  }

  @Override
  public char charAt(final int i) {
    return toString().charAt(i);
  }

  @Override
  public CharSequence subSequence(final int i, final int i1) {
    return toString().subSequence(i, i1);
  }

  @Override
  public String toString() {
    return origin
      .codePoints()
      .mapToObj(codePoint -> unreserved(codePoint) ? Character.toString(codePoint) : encoded(codePoint))
      .collect(Collectors.joining());
  }

  private Boolean unreserved(final Integer codepoint) {
    return codepoint < 128 && unreserved.contains(Character.toChars(codepoint)[0]);
  }

  private String encoded(final Integer codepoint) {
    var sb = new StringBuilder();
    for (var octect : Character.toString(codepoint).getBytes(charset)) {
      sb.append("%").append(hexChars.get(0x0F & (octect >>> 4))).append(hexChars.get(0x0F & octect));
    }
    return sb.toString();
  }

  private final CharSequence origin;
  private final Charset charset;
  private final Set<Character> unreserved;
  private final List<Character> hexChars;
}
