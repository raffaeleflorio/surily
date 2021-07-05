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
 * RFC3986 compliant non-zero {@link PathSegmentSubcomponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class NonZeroPathSegment implements PathSegmentSubcomponent {
  /**
   * Builds a non-zero path segment
   *
   * @param segment The segment
   * @since 1.0.0
   */
  public NonZeroPathSegment(final CharSequence segment) {
    this(new PathSegment(segment));
  }

  /**
   * Builds a non-zero path segment
   *
   * @param origin The segment to decorate
   * @since 1.0.0
   */
  public NonZeroPathSegment(final PathSegmentSubcomponent origin) {
    this.origin = origin;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return nonZero(origin.encoded(charset));
  }

  private String nonZero(final CharSequence cs) {
    if (cs.length() == 0) {
      throw new IllegalStateException("Illegal non-zero segment");
    }
    return cs.toString();
  }

  @Override
  public String asString() {
    return nonZero(origin.asString());
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
