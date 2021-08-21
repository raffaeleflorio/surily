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

import io.github.raffaeleflorio.surily.UriComponent;
import io.github.raffaeleflorio.surily.UriReference;
import io.github.raffaeleflorio.surily.authority.AuthorityComponent;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * RFC3986 compliant merged {@link PathComponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-5.2.3">RFC3986 merge path</a>
 * @since 1.0.0
 */
public final class MergedPath implements PathComponent {
  /**
   * Builds a merged path
   *
   * @param base      The base URI
   * @param reference The reference path
   * @since 1.0.0
   */
  public MergedPath(final UriReference base, final PathComponent reference) {
    this(base, reference, AbsolutePath::new);
  }

  /**
   * Builds a merged path
   *
   * @param base       The base URI
   * @param reference  The reference path
   * @param absoluteFn The function to build absolute paths
   * @since 1.0.0
   */
  MergedPath(
    final UriReference base,
    final Iterable<PathSegmentSubcomponent> reference,
    final Function<List<PathSegmentSubcomponent>, PathComponent> absoluteFn
  ) {
    this.base = base;
    this.reference = reference;
    this.absoluteFn = absoluteFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return merged().encoded(charset);
  }

  private PathComponent merged() {
    return Map.<Boolean, Supplier<PathComponent>>of(
        true, this::absoluteRef,
        false, this::appendedRef
      )
      .get(definedBaseAuthority() && emptyBasePath())
      .get();
  }

  private Boolean definedBaseAuthority() {
    return base.authority().ifDefinedElse(x -> true, () -> false);
  }

  private Boolean emptyBasePath() {
    return base.path().ifEmptyElse(x -> true, y -> false);
  }

  private PathComponent absoluteRef() {
    return absoluteFn.apply(referenceSegments());
  }

  private List<PathSegmentSubcomponent> referenceSegments() {
    return stream(reference).collect(Collectors.toUnmodifiableList());
  }

  private Stream<PathSegmentSubcomponent> stream(final Iterable<PathSegmentSubcomponent> path) {
    return StreamSupport.stream(path.spliterator(), false);
  }

  private PathComponent appendedRef() {
    return base.path().segments(mergedSegments());
  }

  private List<PathSegmentSubcomponent> mergedSegments() {
    return Stream.concat(baseSegmentsExceptLastOne(), referenceSegments().stream())
      .collect(Collectors.toUnmodifiableList());
  }

  private Stream<PathSegmentSubcomponent> baseSegmentsExceptLastOne() {
    var count = stream(base.path()).count();
    return stream(base.path()).limit(count > 1 ? count - 1 : 0);
  }

  @Override
  public String asString() {
    return merged().asString();
  }

  @Override
  public UriComponent relativePart() {
    return merged().relativePart();
  }

  @Override
  public UriComponent relativePart(final AuthorityComponent authority) {
    return merged().relativePart(authority);
  }

  @Override
  public UriComponent hierPart() {
    return merged().hierPart();
  }

  @Override
  public UriComponent hierPart(final AuthorityComponent authority) {
    return merged().hierPart(authority);
  }

  @Override
  public PathComponent segments(final List<PathSegmentSubcomponent> segments) {
    return new MergedPath(base, segments, absoluteFn);
  }

  @Override
  public <T> T ifEmptyElse(final Function<PathComponent, T> emptyFn, final Function<PathComponent, T> fullFn) {
    return merged().ifEmptyElse(x -> emptyFn.apply(this), x -> fullFn.apply(this));
  }

  @Override
  public Iterator<PathSegmentSubcomponent> iterator() {
    return merged().iterator();
  }

  private final UriReference base;
  private final Iterable<PathSegmentSubcomponent> reference;
  private final Function<List<PathSegmentSubcomponent>, PathComponent> absoluteFn;
}
