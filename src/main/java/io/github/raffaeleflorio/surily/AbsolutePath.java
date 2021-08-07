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
import java.util.function.BiFunction;
import java.util.function.Function;
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
   * Builds a root absolute path (i.e. /)
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
    this(segments, NonZeroPathSegment::new, FormattedComponents::new, JoinedComponents::new);
  }

  /**
   * Builds an absolute path
   *
   * @param segments      The segments
   * @param zeroSegmentFn The function to build non-zero segments
   * @param formattedFn   The function to format components
   * @param joinedFn      The function to join components
   * @since 1.0.0
   */
  AbsolutePath(
    final List<PathSegmentSubcomponent> segments,
    final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> zeroSegmentFn,
    final BiFunction<String, List<UriComponent>, UriComponent> formattedFn,
    final BiFunction<List<UriComponent>, String, UriComponent> joinedFn
  ) {
    this.segments = segments;
    this.zeroSegmentFn = zeroSegmentFn;
    this.formattedFn = formattedFn;
    this.joinedFn = joinedFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return formattedSegments().encoded(charset);
  }

  private UriComponent formattedSegments() {
    return formattedFn.apply(
      "/%s",
      List.of(joinedFn.apply(segments().collect(Collectors.toUnmodifiableList()), "/"))
    );
  }

  private Stream<PathSegmentSubcomponent> segments() {
    return IntStream.range(0, segments.size())
      .mapToObj(i -> i == 0 ? zeroSegmentFn.apply(segments.get(0)) : segments.get(i));
  }

  @Override
  public String asString() {
    return formattedSegments().asString();
  }

  @Override
  public Iterator<PathSegmentSubcomponent> iterator() {
    return segments().iterator();
  }

  @Override
  public UriComponent relativePart() {
    return this;
  }

  @Override
  public UriComponent relativePart(final AuthorityComponent authority) {
    return part(authority, this);
  }

  private UriComponent part(final AuthorityComponent authority, final PathComponent path) {
    return formattedFn.apply("//%s%s", List.of(authority, this));
  }

  @Override
  public UriComponent hierPart() {
    return this;
  }

  @Override
  public UriComponent hierPart(final AuthorityComponent authority) {
    return part(authority, this);
  }

  private final List<PathSegmentSubcomponent> segments;
  private final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> zeroSegmentFn;
  private final BiFunction<String, List<UriComponent>, UriComponent> formattedFn;
  private final BiFunction<List<UriComponent>, String, UriComponent> joinedFn;
}
