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

/**
 * Path component of an URI
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-3.3">RFC3986 about path component</a>
 * @since 1.0.0
 */
public interface PathComponent extends UriComponent, Iterable<PathSegmentSubcomponent> {
  /**
   * Builds a relative-part component
   *
   * @return The relative-part
   */
  UriComponent relativePart();

  /**
   * Builds a relative-part component
   *
   * @param authority The authority
   * @return The hier-part
   */
  UriComponent relativePart(AuthorityComponent authority);

  /**
   * Builds a hier-part component
   *
   * @return The hier-part
   */
  UriComponent hierPart();

  /**
   * Builds a hier-part component
   *
   * @param authority The authority
   * @return The hier-part
   */
  UriComponent hierPart(AuthorityComponent authority);

  /**
   * {@link PathComponent} for testing purpose
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  final class Fake implements PathComponent {
    /**
     * Builds a fake
     *
     * @param segments The path segments
     * @since 1.0.0
     */
    public Fake(final Iterable<PathSegmentSubcomponent> segments) {
      this("", "", segments);
    }

    /**
     * Builds a fake
     *
     * @param encoded  The encoded representation
     * @param asString The asString representation
     * @since 1.0.0
     */
    public Fake(final CharSequence encoded, final String asString) {
      this(encoded, asString, List.of());
    }

    /**
     * Builds a fake
     *
     * @param encoded  The encoded representation
     * @param asString The asString representation
     * @param segments The segments
     * @author Raffaele Florio (raffaeleflorio@protonmail.com)
     * @since 1.0.0
     */
    public Fake(final CharSequence encoded, final String asString, final Iterable<PathSegmentSubcomponent> segments) {
      this(
        encoded,
        asString,
        segments,
        new UriComponent.Fake("", ""),
        new UriComponent.Fake("", "")
      );
    }

    /**
     * Builds a fake
     *
     * @param relativePart The relative-part
     * @param hierPart     The hier-part
     * @author Raffaele Florio (raffaeleflorio@protonmail.com)
     * @since 1.0.0
     */
    public Fake(final UriComponent relativePart, final UriComponent hierPart) {
      this("", "", List.of(), relativePart, hierPart);
    }

    /**
     * Builds a fake
     *
     * @param encoded      The encoded representation
     * @param asString     The asString representation
     * @param segments     The path segments
     * @param relativePart The relative-part
     * @param hierPart     The hier-part
     * @since 1.0.0
     */
    public Fake(
      final CharSequence encoded,
      final String asString,
      final Iterable<PathSegmentSubcomponent> segments,
      final UriComponent relativePart,
      final UriComponent hierPart
    ) {
      this.encoded = encoded;
      this.asString = asString;
      this.segments = segments;
      this.relativePart = relativePart;
      this.hierPart = hierPart;
    }

    @Override
    public CharSequence encoded(final Charset charset) {
      return encoded;
    }

    @Override
    public String asString() {
      return asString;
    }

    @Override
    public Iterator<PathSegmentSubcomponent> iterator() {
      return segments.iterator();
    }

    @Override
    public UriComponent relativePart() {
      return relativePart;
    }

    @Override
    public UriComponent relativePart(final AuthorityComponent authority) {
      return relativePart;
    }

    @Override
    public UriComponent hierPart() {
      return hierPart;
    }

    @Override
    public UriComponent hierPart(final AuthorityComponent authority) {
      return hierPart;
    }

    private final CharSequence encoded;
    private final String asString;
    private final Iterable<PathSegmentSubcomponent> segments;
    private final UriComponent relativePart;
    private final UriComponent hierPart;
  }
}
