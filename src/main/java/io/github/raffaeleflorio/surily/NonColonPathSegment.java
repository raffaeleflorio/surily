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
import java.util.function.Function;

/**
 * RFC3986 compliant non-colon {@link PathSegmentSubcomponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class NonColonPathSegment implements PathSegmentSubcomponent {
  /**
   * Builds a non-colon path segment
   *
   * @param segment The segment
   * @since 1.0.0
   */
  public NonColonPathSegment(final CharSequence segment) {
    this(new PathSegment(segment));
  }

  /**
   * Builds a non-colon path segment
   *
   * @param origin The segment to decorate
   * @since 1.0.0
   */
  public NonColonPathSegment(final PathSegmentSubcomponent origin) {
    this(
      origin,
      (segment, charset) -> segment
        .encoded(charset)
        .toString()
        .replace(":", new PercentEncoded(":", charset, new DiffSet<>(new Pchar(), Set.of(':'))))
    );
  }

  /**
   * Builds a non-colon path segment
   *
   * @param origin     The segment to decorate
   * @param encodingFn The encoding function
   * @since 1.0.0
   */
  NonColonPathSegment(final PathSegmentSubcomponent origin, final BiFunction<PathSegmentSubcomponent, Charset, CharSequence> encodingFn) {
    this.origin = origin;
    this.encodingFn = encodingFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return encodingFn.apply(origin, charset);
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
    return origin.ifDotElse(
      x -> singleFn.apply(new NonColonPathSegment(x)),
      x -> doubleFn.apply(new NonColonPathSegment(x)),
      x -> normalSegmentFn.apply(new NonColonPathSegment(x))
    );
  }

  private final PathSegmentSubcomponent origin;
  private final BiFunction<PathSegmentSubcomponent, Charset, CharSequence> encodingFn;
}
