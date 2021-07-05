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
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * RFC3986 compliant {@link PathSegmentSubcomponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class PathSegment implements PathSegmentSubcomponent {
  /**
   * Builds an empty segment
   *
   * @since 1.0.0
   */
  public PathSegment() {
    this("");
  }

  /**
   * Builds a segment
   *
   * @param segment The segment
   * @since 1.0.0
   */
  public PathSegment(final CharSequence segment) {
    this(segment, (s, charset) -> new PercentEncoded(s, charset, new Pchar()));
  }

  /**
   * Builds a segment
   *
   * @param segment    The segment
   * @param encodingFn The encoding function
   * @since 1.0.0
   */
  PathSegment(final CharSequence segment, final BiFunction<CharSequence, Charset, CharSequence> encodingFn) {
    this.segment = segment;
    this.encodingFn = encodingFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return encodingFn.apply(segment, charset).toString();
  }

  @Override
  public String asString() {
    return segment.toString();
  }

  @Override
  public <T> T ifDotElse(final Function<PathSegmentSubcomponent, T> fn, final Function<PathSegmentSubcomponent, T> normalSegmentFn) {
    return segment.equals(".") || segment.equals("..") ? fn.apply(this) : normalSegmentFn.apply(this);
  }

  private final CharSequence segment;
  private final BiFunction<CharSequence, Charset, CharSequence> encodingFn;
}
