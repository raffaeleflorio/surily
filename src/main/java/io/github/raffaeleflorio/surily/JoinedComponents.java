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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Joined {@link UriComponent} with a separator
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class JoinedComponents implements UriComponent {
  /**
   * Builds concatenated components
   *
   * @param components The components to join
   * @since 1.0.0
   */
  public JoinedComponents(final List<UriComponent> components) {
    this(components, "");
  }

  /**
   * Builds joined components
   *
   * @param components The components to join
   * @param separator  The separator
   * @since 1.0.0
   */
  public JoinedComponents(final List<UriComponent> components, final String separator) {
    this.components = components;
    this.separator = separator;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return concatenated(c -> c.encoded(charset));
  }

  private String concatenated(final Function<UriComponent, CharSequence> fn) {
    return components.stream().map(fn).collect(Collectors.joining(separator));
  }

  @Override
  public String asString() {
    return concatenated(UriComponent::asString);
  }

  private final List<UriComponent> components;
  private final String separator;
}
