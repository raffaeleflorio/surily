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

/**
 * Multiple {@link UriComponent} formatted according a format string
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class FormattedComponents implements UriComponent {
  /**
   * Builds a formatted component
   *
   * @param formatString The format string
   * @param components   The components to format
   * @since 1.0.0
   */
  public FormattedComponents(final String formatString, final List<UriComponent> components) {
    this(formatString, components, String::format);
  }

  /**
   * Builds a formatted component
   *
   * @param formatString The format string
   * @param components   The components to format
   * @param formatFn     The function used to format strings
   * @since 1.0.0
   */
  FormattedComponents(
    final String formatString,
    final List<UriComponent> components,
    final BiFunction<String, Object[], String> formatFn
  ) {
    this.formatString = formatString;
    this.components = components;
    this.formatFn = formatFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return formatFn.apply(formatString, components(c -> c.encoded(charset)));
  }

  private Object[] components(final Function<UriComponent, Object> fn) {
    return components.stream().map(fn).toArray();
  }

  @Override
  public String asString() {
    return formatFn.apply(formatString, components(UriComponent::asString));
  }

  private final String formatString;
  private final List<UriComponent> components;
  private final BiFunction<String, Object[], String> formatFn;
}
