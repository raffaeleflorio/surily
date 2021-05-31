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

/**
 * Segment subcomponent of a {@link PathComponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public interface PathSegmentSubcomponent extends UriComponent {
  /**
   * A {@link PathSegmentSubcomponent} for testing purpose
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  final class Fake implements PathSegmentSubcomponent {
    /**
     * Builds a fake for testing
     *
     * @param encoded  The encoded representation
     * @param asString The asString representation
     * @since 1.0.0
     */
    public Fake(final CharSequence encoded, final String asString) {
      this.encoded = encoded;
      this.asString = asString;
    }

    @Override
    public CharSequence encoded(final Charset charset) {
      return encoded;
    }

    @Override
    public String asString() {
      return asString;
    }

    private final CharSequence encoded;
    private final String asString;
  }
}
