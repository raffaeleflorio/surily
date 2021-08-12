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
 * RFC3986 compliant noscheme {@link PathComponent} like: ../../../file.json
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class NoSchemePath implements PathComponent {
  /**
   * Builds a path
   *
   * @param segments The segments
   * @since 1.0.0
   */
  public NoSchemePath(final List<PathSegmentSubcomponent> segments) {
    this(segments, s -> new NonZeroPathSegment(new NonColonPathSegment(s)), JoinedComponents::new);
  }

  /**
   * Builds a path
   *
   * @param segments The segments
   * @param firstFn  The function to apply to the first segment
   * @param joinedFn The function to build joined components
   * @since 1.0.0
   */
  NoSchemePath(
    final List<PathSegmentSubcomponent> segments,
    final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> firstFn,
    final BiFunction<List<UriComponent>, String, UriComponent> joinedFn
  ) {
    this.segments = segments;
    this.firstFn = firstFn;
    this.joinedFn = joinedFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return formattedSegments().encoded(charset);
  }

  private UriComponent formattedSegments() {
    return joinedFn.apply(segments().collect(Collectors.toUnmodifiableList()), "/");
  }

  private Stream<PathSegmentSubcomponent> segments() {
    return IntStream.range(0, segments.size())
      .mapToObj(i -> i == 0 ? firstFn.apply(segments.get(0)) : segments.get(i));
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
  private final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> firstFn;
  private final BiFunction<List<UriComponent>, String, UriComponent> joinedFn;
}
