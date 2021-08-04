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
 * Concatenated {@link UriComponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class ConcatenatedComponents implements UriComponent {
  public ConcatenatedComponents(final UriComponent first, final UriComponent second) {
    this(List.of(first, second));
  }

  public ConcatenatedComponents(final List<UriComponent> components) {
    this.components = components;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return concatenated(c -> c.encoded(charset));
  }

  private String concatenated(final Function<UriComponent, CharSequence> fn) {
    return components.stream().map(fn).collect(Collectors.joining());
  }

  @Override
  public String asString() {
    return concatenated(UriComponent::asString);
  }

  private final List<UriComponent> components;
}
