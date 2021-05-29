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
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * RFC3986 compliant absolute {@link PathComponent} like: /an/absolute/path or /
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class AbsolutePath implements PathComponent {
  /**
   * Builds the root absolute path (i.e. /)
   *
   * @since 1.0.0
   */
  public AbsolutePath() {
    this(List.of());
  }

  /**
   * Builds an absolute path
   *
   * @param segments The segments
   * @since 1.0.0
   */
  public AbsolutePath(final List<PathSegmentSubcomponent> segments) {
    this(segments, NonZeroPathSegment::new);
  }

  /**
   * Builds an absolute path
   *
   * @param segments      The segments
   * @param zeroSegmentFn The function to build non-zero segments
   * @since 1.0.0
   */
  AbsolutePath(final List<PathSegmentSubcomponent> segments, final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> zeroSegmentFn) {
    this.segments = segments;
    this.zeroSegmentFn = zeroSegmentFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return "/".concat(
      segments().map(segment -> segment.encoded(charset)).collect(segmentsCollector())
    );
  }

  private Stream<PathSegmentSubcomponent> segments() {
    return IntStream.range(0, segments.size())
      .mapToObj(i -> i == 0 ? zeroSegmentFn.apply(segments.get(0)) : segments.get(i));
  }

  private Collector<CharSequence, ?, String> segmentsCollector() {
    return Collectors.joining("/");
  }

  @Override
  public String asString() {
    return "/".concat(
      segments().map(PathSegmentSubcomponent::asString).collect(segmentsCollector())
    );
  }

  @Override
  public Iterator<PathSegmentSubcomponent> iterator() {
    return segments().iterator();
  }

  private final List<PathSegmentSubcomponent> segments;
  private final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> zeroSegmentFn;
}
