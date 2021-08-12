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
import io.github.raffaeleflorio.surily.characters.DiffSet;
import io.github.raffaeleflorio.surily.characters.Pchar;

import java.nio.charset.Charset;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * RFC3986 compliant {@link UserinfoSubComponent} like: username:scheme-auth-part
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class Userinfo implements UserinfoSubComponent {
  /**
   * Builds an empty userinfo
   *
   * @since 1.0.0
   */
  public Userinfo() {
    this("");
  }

  /**
   * Builds an userinfo
   *
   * @param userinfo The userinfo
   * @since 1.0.0
   */
  public Userinfo(final CharSequence userinfo) {
    this(
      userinfo,
      (s, charset) -> new PercentEncoded(s, charset, new DiffSet<>(new Pchar(), Set.of('@')))
    );
  }

  /**
   * Builds an userinfo
   *
   * @param userinfo   The userinfo
   * @param encodingFn The encoding function
   * @since 1.0.0
   */
  Userinfo(final CharSequence userinfo, final BiFunction<CharSequence, Charset, CharSequence> encodingFn) {
    this.userinfo = userinfo;
    this.encodingFn = encodingFn;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return encodingFn.apply(userinfo, charset).toString();
  }

  @Override
  public String asString() {
    return userinfo.toString();
  }

  @Override
  public <T> T ifDefinedElse(final Function<UserinfoSubComponent, T> fn, final Supplier<T> undefinedFn) {
    return fn.apply(this);
  }

  private final CharSequence userinfo;
  private final BiFunction<CharSequence, Charset, CharSequence> encodingFn;
}
