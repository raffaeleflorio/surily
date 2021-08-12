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
package io.github.raffaeleflorio.surily.authority;

import io.github.raffaeleflorio.surily.PercentEncoded;
import io.github.raffaeleflorio.surily.characters.SubDelims;
import io.github.raffaeleflorio.surily.characters.UnionSet;
import io.github.raffaeleflorio.surily.characters.UnreservedCharacters;

import java.nio.charset.Charset;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * RFC3986 compliant reg-name {@link HostSubcomponent} like DNS name
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class RegName implements HostSubcomponent {
  /**
   * Builds an empty reg-name
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  public RegName() {
    this("");
  }

  /**
   * Builds a reg-name
   *
   * @param regname The reg-name
   * @since 1.0.0
   */
  public RegName(final CharSequence regname) {
    this(
      regname,
      (s, charset) -> new PercentEncoded(s, charset, new UnionSet<>(new UnreservedCharacters(), new SubDelims()))
    );
  }

  /**
   * Builds a reg-name
   *
   * @param regname    The reg-name
   * @param encodingFn The encoding function
   * @since 1.0.0
   */
  RegName(final CharSequence regname, final BiFunction<CharSequence, Charset, CharSequence> encodingFn) {
    this.regname = regname;
    this.encodingFn = encodingFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return encodingFn.apply(regname, charset).toString();
  }

  @Override
  public String asString() {
    return regname.toString();
  }

  @Override
  public <T> T ifDefinedElse(final Function<HostSubcomponent, T> fn, final Supplier<T> undefinedFn) {
    return fn.apply(this);
  }

  private final CharSequence regname;
  private final BiFunction<CharSequence, Charset, CharSequence> encodingFn;
}
