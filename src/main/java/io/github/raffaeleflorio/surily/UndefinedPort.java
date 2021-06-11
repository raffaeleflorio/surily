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
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Undefined {@link PortSubcomponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class UndefinedPort implements PortSubcomponent {
  /**
   * Builds an undefined port
   *
   * @since 1.0.0
   */
  public UndefinedPort() {

  }

  @Override
  public CharSequence encoded(final Charset charset) {
    throw undefinedPort();
  }

  private RuntimeException undefinedPort() {
    return new IllegalStateException("No representations for an undefined port");
  }

  @Override
  public String asString() {
    throw undefinedPort();
  }

  @Override
  public <T> T ifDefinedElse(final Function<PortSubcomponent, T> fn, final Supplier<T> undefinedFn) {
    return undefinedFn.get();
  }
}
