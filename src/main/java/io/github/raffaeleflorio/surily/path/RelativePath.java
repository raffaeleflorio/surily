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
package io.github.raffaeleflorio.surily.path;

import io.github.raffaeleflorio.surily.JoinedComponents;
import io.github.raffaeleflorio.surily.UriComponent;
import io.github.raffaeleflorio.surily.authority.AuthorityComponent;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
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
   * Builds a path
   *
   * @param segments The segments
   * @since 1.0.0
   */
  public RelativePath(final List<PathSegmentSubcomponent> segments) {
    this(
      segments,
      NonZeroPathSegment::new,
      s -> new NonColonPathSegment(new NonZeroPathSegment(s)),
      JoinedComponents::new
    );
  }

  /**
   * Builds a path
   *
   * @param segments   The segments
   * @param rootlessFn The function to apply to a rootless segment
   * @param noSchemeFn The function to apply to a noscheme segment
   * @param joinedFn   The function to build joined components
   * @since 1.0.0
   */
  RelativePath(
    final List<PathSegmentSubcomponent> segments,
    final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> rootlessFn,
    final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> noSchemeFn,
    final BiFunction<List<UriComponent>, String, UriComponent> joinedFn
  ) {
    this.segments = segments;
    this.rootlessFn = rootlessFn;
    this.noSchemeFn = noSchemeFn;
    this.joinedFn = joinedFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return formattedSegments(rootlessFn).encoded(charset);
  }

  private UriComponent formattedSegments(
    final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> firstFn
  ) {
    return joinedFn.apply(segments(firstFn).collect(Collectors.toUnmodifiableList()), "/");
  }

  private Stream<PathSegmentSubcomponent> segments(
    final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> firstFn
  ) {
    return IntStream.range(0, segments.size())
      .mapToObj(i -> i == 0 ? firstFn.apply(segments.get(0)) : segments.get(i));
  }

  @Override
  public String asString() {
    return formattedSegments(rootlessFn).asString();
  }

  @Override
  public Iterator<PathSegmentSubcomponent> iterator() {
    return segments(rootlessFn).iterator();
  }

  @Override
  public UriComponent relativePart() {
    return formattedSegments(noSchemeFn);
  }

  @Override
  public UriComponent relativePart(final AuthorityComponent authority) {
    throw new IllegalStateException("Unable to build a relative-part with an authority");
  }

  @Override
  public UriComponent hierPart() {
    return this;
  }

  @Override
  public UriComponent hierPart(final AuthorityComponent authority) {
    throw new IllegalStateException("Unable to build a hier-part with an authority");
  }

  private final List<PathSegmentSubcomponent> segments;
  private final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> rootlessFn;
  private final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> noSchemeFn;
  private final BiFunction<List<UriComponent>, String, UriComponent> joinedFn;
}
