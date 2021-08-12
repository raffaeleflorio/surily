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
package io.github.raffaeleflorio.surily.authority;

import io.github.raffaeleflorio.surily.UriComponent;

import java.nio.charset.Charset;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Port subcomponent of an {@link AuthorityComponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-3.2.3">RFC3986 about port subcomponent</a>
 * @since 1.0.0
 */
public interface PortSubcomponent extends UriComponent {
  /**
   * Uses a function if the port is defined otherwise another one
   *
   * @param fn          The function used when the port is defined
   * @param undefinedFn The supplier used when the port is undefined
   * @param <T>         The result type
   * @return The result
   */
  <T> T ifDefinedElse(Function<PortSubcomponent, T> fn, Supplier<T> undefinedFn);

  /**
   * {@link PortSubcomponent} for testing purpose
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  final class Fake implements PortSubcomponent {
    /**
     * Builds a fake port
     *
     * @param port The port
     * @since 1.0.0
     */
    public Fake(final Integer port) {
      this.port = port;
    }

    @Override
    public CharSequence encoded(final Charset charset) {
      return port.toString();
    }

    @Override
    public String asString() {
      return port.toString();
    }

    @Override
    public <T> T ifDefinedElse(final Function<PortSubcomponent, T> fn, final Supplier<T> undefinedFn) {
      return fn.apply(this);
    }

    private final Integer port;
  }
}
