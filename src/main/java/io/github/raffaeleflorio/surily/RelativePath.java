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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * RFC3986 compliant relative {@link PathComponent} like: ../../../file.json
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class RelativePath implements PathComponent {
  /**
   * Builds an empty path
   *
   * @since 1.0.0
   */
  public RelativePath() {
    this(List.of());
  }

  /**
   * Builds a path
   *
   * @param segments The segments
   * @since 1.0.0
   */
  public RelativePath(final List<PathSegmentSubcomponent> segments) {
    this(segments, s -> new NonZeroPathSegment(new NonColonPathSegment(s)));
  }

  /**
   * Builds a path
   *
   * @param segments The segments
   * @param firstFn  The function to apply to the first segment
   * @since 1.0.0
   */
  RelativePath(final List<PathSegmentSubcomponent> segments, final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> firstFn) {
    this.segments = segments;
    this.firstFn = firstFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return segments()
      .map(segment -> segment.encoded(charset))
      .collect(Collectors.joining("/"));
  }

  private Stream<PathSegmentSubcomponent> segments() {
    return IntStream.range(0, segments.size()).mapToObj(i -> i == 0 ? firstFn.apply(segments.get(0)) : segments.get(i));
  }

  @Override
  public String asString() {
    return segments().map(PathSegmentSubcomponent::asString).collect(Collectors.joining("/"));
  }

  @Override
  public Iterator<PathSegmentSubcomponent> iterator() {
    return segments().iterator();
  }

  private final List<PathSegmentSubcomponent> segments;
  private final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> firstFn;
}
