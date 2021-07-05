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

/**
 * Double dot {@link PathSegmentSubcomponent} (i.e. "..")
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class DoubleDotSegment implements PathSegmentSubcomponent {
  public DoubleDotSegment() {
    this(new PathSegment(".."));
  }

  DoubleDotSegment(final PathSegmentSubcomponent origin) {
    this.origin = origin;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return origin.encoded(charset);
  }

  @Override
  public String asString() {
    return origin.asString();
  }

  @Override
  public <T> T ifDotElse(
    Function<PathSegmentSubcomponent, T> singleFn,
    Function<PathSegmentSubcomponent, T> doubleFn,
    Function<PathSegmentSubcomponent, T> normalSegmentFn
  ) {
    return origin.ifDotElse(singleFn, doubleFn, normalSegmentFn);
  }

  private final PathSegmentSubcomponent origin;
}
