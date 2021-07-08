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
import java.util.Stack;
import java.util.function.Function;

/**
 * {@link PathComponent} without dot segments (e.g. "relative/../path/with/dot/segments" becomes "path/with/dot/segments")
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-5.2.4">RFC3986 definition</a>
 * @since 1.0.0
 */
public final class PathWithoutDotSegments implements PathComponent {
  /**
   * Builds a path without dot segments
   *
   * @param origin The path to remove dot segments
   * @since 1.0.0
   */
  public PathWithoutDotSegments(final PathComponent origin) {
    this(origin, AbsolutePath::new, RelativePath::new);
  }

  /**
   * Builds a path without dot segments
   *
   * @param origin     The path to remove dot segments
   * @param absoluteFn The function used to build absolute path
   * @param relativeFn The function used to build relative path
   * @since 1.0.0
   */
  PathWithoutDotSegments(final PathComponent origin, final Function<List<PathSegmentSubcomponent>, PathComponent> absoluteFn, final Function<List<PathSegmentSubcomponent>, PathComponent> relativeFn) {
    this.origin = origin;
    this.absoluteFn = absoluteFn;
    this.relativeFn = relativeFn;
  }

  @Override
  public <T> T ifAbsoluteElse(final Function<PathComponent, T> fn, final Function<PathComponent, T> relativeFn) {
    return origin.ifAbsoluteElse(fn, relativeFn);
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return withoutDotSegments().encoded(charset);
  }

  private PathComponent withoutDotSegments() {
    return ifAbsoluteElse(x -> absoluteFn.apply(segments()), y -> relativeFn.apply(segments()));
  }

  private List<PathSegmentSubcomponent> segments() {
    var segments = new Stack<PathSegmentSubcomponent>();
    origin.forEach(segment -> segment
      .ifDotElse(
        Function.identity(),
        x -> segments.isEmpty() ? x : segments.pop(),
        segments::push
      )
    );
    return segments;
  }

  @Override
  public String asString() {
    return withoutDotSegments().asString();
  }

  @Override
  public Iterator<PathSegmentSubcomponent> iterator() {
    return withoutDotSegments().iterator();
  }

  private final PathComponent origin;
  private final Function<List<PathSegmentSubcomponent>, PathComponent> absoluteFn;
  private final Function<List<PathSegmentSubcomponent>, PathComponent> relativeFn;
}
