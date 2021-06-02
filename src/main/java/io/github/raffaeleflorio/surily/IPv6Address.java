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
import java.util.List;
import java.util.regex.Pattern;

/**
 * RFC3986 compliant IPv6 address {@link HostSubcomponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class IPv6Address implements HostSubcomponent {
  /**
   * Builds an IPv6 address
   *
   * @param hextets The hextets
   * @since 1.0.0
   */
  public IPv6Address(final List<CharSequence> hextets) {
    this(String.join(":", hextets));
  }

  /**
   * Builds an IPv6 address
   *
   * @param address The address
   * @since 1.0.0
   */
  public IPv6Address(final CharSequence address) {
    this.address = address;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return address();
  }

  private String address() {
    if (legalAddress()) {
      return "[".concat(address.toString()).concat("]");
    }
    throw illegalIPAddress();
  }

  /*
    From RFC3986:
      IPv6address =                            6( h16 ":" ) ls32
                  /                       "::" 5( h16 ":" ) ls32
                  / [               h16 ] "::" 4( h16 ":" ) ls32
                  / [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) ls32
                  / [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) ls32
                  / [ *3( h16 ":" ) h16 ] "::"    h16 ":"   ls32
                  / [ *4( h16 ":" ) h16 ] "::"              ls32
                  / [ *5( h16 ":" ) h16 ] "::"              h16
                  / [ *6( h16 ":" ) h16 ] "::"

      ls32        = ( h16 ":" h16 ) / IPv4address
                  ; least-significant 32 bits of address

      h16         = 1*4HEXDIG
                  ; 16 bits of address represented in hexadecimal
   */
  private Boolean legalAddress() {
    var h16 = "[a-zA-Z0-9]{1,4}";
    var ipv4 = "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";
    var ls32 = String.format("(%s:%s)|(%s)", h16, h16, ipv4);
    var first = String.format("(%s:){6}(%s)", h16, ls32);
    var second = String.format("::(%s:){5}(%s)", h16, ls32);
    var third = String.format("(%s)?::(%s:){4}(%s)", h16, h16, ls32);
    var fourth = String.format("((%s:){0,1}%s)?::(%s:){3}(%s)", h16, h16, h16, ls32);
    var fifth = String.format("((%s:){0,2}%s)?::(%s:){2}(%s)", h16, h16, h16, ls32);
    var sixth = String.format("((%s:){0,3}%s)?::%s:(%s)", h16, h16, h16, ls32);
    var seventh = String.format("((%s:){0,4}%s)?::(%s)", h16, h16, ls32);
    var eighth = String.format("((%s:){0,5}%s)?::%s", h16, h16, h16);
    var ninth = String.format("((%s:){0,6}%s)?::", h16, h16);
    return Pattern.matches(
      String.format(
        "^((%s)|(%s)|(%s)|(%s)|(%s)|(%s)|(%s)|(%s)|(%s))$",
        first, second, third, fourth, fifth, sixth, seventh, eighth, ninth
      ),
      address
    );
  }

  private RuntimeException illegalIPAddress() {
    return new IllegalStateException(
      String.format(
        "Illegal IPv6 address: <%s>", address.length() > 4096 ? address.toString().substring(0, 4096).concat("...") : address)
    );
  }

  @Override
  public String asString() {
    return address();
  }

  private final CharSequence address;
}
