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
package io.github.raffaeleflorio.surily.scheme;

import java.nio.charset.Charset;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * RFC3986 {@link SchemeComponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class Scheme implements SchemeComponent {
  /**
   * Builds a scheme
   *
   * @param scheme The scheme
   * @since 1.0.0
   */
  public Scheme(final CharSequence scheme) {
    this.scheme = scheme;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return validated().toLowerCase();
  }

  private String validated() {
    var x = scheme.toString();
    if (x.matches("[a-zA-Z][a-zA-Z0-9+\\-.]*")) {
      return x;
    }
    throw illegalSchemeExcpetion();
  }

  private RuntimeException illegalSchemeExcpetion() {
    return new IllegalStateException(
      String.format(
        "Illegal scheme: <%s>",
        scheme.length() > 4096 ? scheme.toString().substring(0, 4096).concat("...") : scheme
      )
    );
  }

  @Override
  public String asString() {
    return validated();
  }

  @Override
  public <T> T ifDefinedElse(final Function<SchemeComponent, T> fn, final Supplier<T> undefinedFn) {
    return fn.apply(this);
  }

  private final CharSequence scheme;
}
