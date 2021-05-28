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
import java.util.Set;
import java.util.function.BiFunction;

/**
 * RFC3986 compliant {@link FragmentComponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class Fragment implements FragmentComponent {
  /**
   * Builds the fragment
   *
   * @param origin The value of the unencoded fragment
   * @since 1.0.0
   */
  public Fragment(final CharSequence origin) {
    this(
      origin,
      (s, charset) -> new PercentEncoded(s, charset, new UnionSet<>(new Pchar(), Set.of('/', '?')))
    );
  }

  /**
   * Builds the fragment
   *
   * @param origin     The value of the unencoded fragment
   * @param encodingFn The encoding function
   * @since 1.0.0
   */
  Fragment(final CharSequence origin, final BiFunction<CharSequence, Charset, CharSequence> encodingFn) {
    this.origin = origin;
    this.encodingFn = encodingFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return encodingFn.apply(origin, charset).toString();
  }

  @Override
  public String asString() {
    return origin.toString();
  }

  private final CharSequence origin;
  private final BiFunction<CharSequence, Charset, CharSequence> encodingFn;
}
