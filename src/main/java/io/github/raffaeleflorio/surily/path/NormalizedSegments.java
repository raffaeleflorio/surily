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

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

/**
 * Immutable list of normalized {@link PathSegmentSubcomponent}s. For example [., .., ., path] becomes [path]
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-5.2.4">RFC3986 definition</a>
 * @since 1.0.0
 */
public final class NormalizedSegments extends AbstractList<PathSegmentSubcomponent> {
  /**
   * Builds normalized segments
   *
   * @param origin The iterable to decorate
   */
  public NormalizedSegments(final Iterable<PathSegmentSubcomponent> origin) {
    this.origin = origin;
  }

  @Override
  public PathSegmentSubcomponent get(final int index) {
    return normalizedList().get(index);
  }

  @Override
  public Iterator<PathSegmentSubcomponent> iterator() {
    return normalizedList().iterator();
  }

  @Override
  public int size() {
    return normalizedList().size();
  }

  private List<PathSegmentSubcomponent> normalizedList() {
    var ret = new Stack<PathSegmentSubcomponent>();
    origin.forEach(i -> i.ifDotElse(
        Function.identity(),
        x -> ret.isEmpty() ? x : ret.pop(),
        ret::push
      )
    );
    return ret;
  }

  private final Iterable<PathSegmentSubcomponent> origin;
}
