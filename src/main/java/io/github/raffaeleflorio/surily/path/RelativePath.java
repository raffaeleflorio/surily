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

import io.github.raffaeleflorio.surily.FormattedComponents;
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
   * Builds an empty path (i.e. "")
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
    this(
      segments,
      NonZeroPathSegment::new,
      s -> new NonColonPathSegment(new NonZeroPathSegment(s)),
      JoinedComponents::new,
      FormattedComponents::new
    );
  }

  /**
   * Builds a path
   *
   * @param segments    The segments
   * @param rootlessFn  The function to apply to a rootless segment
   * @param noSchemeFn  The function to apply to a noscheme segment
   * @param joinedFn    The function to build joined components
   * @param formattedFn The function to build formatted components
   * @since 1.0.0
   */
  RelativePath(
    final List<PathSegmentSubcomponent> segments,
    final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> rootlessFn,
    final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> noSchemeFn,
    final BiFunction<List<UriComponent>, String, UriComponent> joinedFn,
    final BiFunction<String, List<UriComponent>, UriComponent> formattedFn
  ) {
    this.segments = segments;
    this.rootlessFn = rootlessFn;
    this.noSchemeFn = noSchemeFn;
    this.joinedFn = joinedFn;
    this.formattedFn = formattedFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return joinedSegments(rootlessFn).encoded(charset);
  }

  private UriComponent joinedSegments(
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
    return joinedSegments(rootlessFn).asString();
  }

  @Override
  public Iterator<PathSegmentSubcomponent> iterator() {
    return segments(rootlessFn).iterator();
  }

  @Override
  public UriComponent relativePart() {
    return joinedSegments(noSchemeFn);
  }

  @Override
  public UriComponent relativePart(final AuthorityComponent authority) {
    return ifEmptyElse(
      x -> part(authority),
      y -> {
        throw new IllegalStateException("Unable to build a relative-part with an authority and a full relative-path");
      }
    );
  }

  private UriComponent part(final AuthorityComponent authority) {
    return formattedFn.apply("//%s", List.of(authority));
  }

  @Override
  public UriComponent hierPart() {
    return this;
  }

  @Override
  public UriComponent hierPart(final AuthorityComponent authority) {
    return ifEmptyElse(
      x -> part(authority),
      y -> {
        throw new IllegalStateException("Unable to build a hier-part with an authority and a full relative-path");
      }
    );
  }

  @Override
  public PathComponent segments(final List<PathSegmentSubcomponent> segments) {
    return new RelativePath(segments, rootlessFn, noSchemeFn, joinedFn, formattedFn);
  }

  @Override
  public <T> T ifEmptyElse(final Function<PathComponent, T> emptyFn, final Function<PathComponent, T> fullFn) {
    return segments.isEmpty() ? emptyFn.apply(this) : fullFn.apply(this);
  }

  @Override
  public <T> T ifAbsoluteElse(final Function<PathComponent, T> absoluteFn, final Function<PathComponent, T> relativeFn) {
    return relativeFn.apply(this);
  }

  private final List<PathSegmentSubcomponent> segments;
  private final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> rootlessFn;
  private final Function<PathSegmentSubcomponent, PathSegmentSubcomponent> noSchemeFn;
  private final BiFunction<List<UriComponent>, String, UriComponent> joinedFn;
  private final BiFunction<String, List<UriComponent>, UriComponent> formattedFn;
}
