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
 * Undefined {@link SchemeComponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class UndefinedScheme implements SchemeComponent {
  @Override
  public <T> T ifDefinedElse(final Function<SchemeComponent, T> fn, final Supplier<T> undefinedFn) {
    return undefinedFn.get();
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    throw undefinedSchemeException();
  }

  private RuntimeException undefinedSchemeException() {
    return new IllegalStateException("No representations for an undefined scheme");
  }

  @Override
  public String asString() {
    throw undefinedSchemeException();
  }
}
