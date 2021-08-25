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
import io.github.raffaeleflorio.surily.authority.AuthorityComponent;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * RFC3986 compliant empty {@link PathComponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class EmptyPath implements PathComponent {
  /**
   * Builds an empty path
   */
  public EmptyPath() {
    this(new RelativePath());
  }

  private EmptyPath(final PathComponent origin) {
    this.origin = origin;
  }

  @Override
  public UriComponent relativePart() {
    return origin.relativePart();
  }

  @Override
  public UriComponent relativePart(final AuthorityComponent authority) {
    return origin.relativePart(authority);
  }

  @Override
  public UriComponent hierPart() {
    return origin.hierPart();
  }

  @Override
  public UriComponent hierPart(final AuthorityComponent authority) {
    return origin.hierPart(authority);
  }

  @Override
  public PathComponent segments(final List<PathSegmentSubcomponent> segments) {
    throw new IllegalStateException("An empty path cannot have segments");
  }

  @Override
  public <T> T ifEmptyElse(
    final Function<PathComponent, T> emptyFn,
    final Function<PathComponent, T> fullFn
  ) {
    return emptyFn.apply(this);
  }

  @Override
  public <T> T ifAbsoluteElse(final Function<PathComponent, T> absoluteFn, final Function<PathComponent, T> relativeFn) {
    return relativeFn.apply(this);
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return origin.encoded(charset);
  }

  @Override
  public String asString() {
    return origin.asString();
  }

  @Override
  public Iterator<PathSegmentSubcomponent> iterator() {
    return origin.iterator();
  }

  private final PathComponent origin;
}
