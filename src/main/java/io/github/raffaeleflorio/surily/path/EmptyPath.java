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
import io.github.raffaeleflorio.surily.UriComponent;
import io.github.raffaeleflorio.surily.authority.AuthorityComponent;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * RFC3986 compliant empty {@link PathComponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class EmptyPath implements PathComponent {
  /**
   * Builds an empty path
   *
   * @since 1.0.0
   */
  public EmptyPath() {
    this(FormattedComponents::new);
  }

  /**
   * Builds an empty path
   *
   * @param formattedFn The function to format components
   * @since 1.0.0
   */
  EmptyPath(final BiFunction<String, List<UriComponent>, UriComponent> formattedFn) {
    this.formattedFn = formattedFn;
  }

  @Override
  public UriComponent relativePart() {
    return this;
  }

  @Override
  public UriComponent relativePart(final AuthorityComponent authority) {
    return part(authority);
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
    return part(authority);
  }

  @Override
  public PathComponent segments(final List<PathSegmentSubcomponent> segments) {
    throw new IllegalStateException("An empty path cannot have segments");
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return "";
  }

  @Override
  public String asString() {
    return "";
  }

  @Override
  public Iterator<PathSegmentSubcomponent> iterator() {
    return Stream.<PathSegmentSubcomponent>empty().iterator();
  }

  private final BiFunction<String, List<UriComponent>, UriComponent> formattedFn;
}
