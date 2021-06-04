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
 * RFC3986 compliant {@link PortSubcomponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class Port implements PortSubcomponent {
  /**
   * Builds a port
   *
   * @param port The port
   * @since 1.0.0
   */
  public Port(final Integer port) {
    this(port.toString());
  }

  /**
   * Builds a port
   *
   * @param port The port
   * @since 1.0.0
   */
  public Port(final CharSequence port) {
    this.port = port;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return port();
  }

  private String port() {
    if (legalPort()) {
      return port.toString();
    }
    throw illegalPort();
  }

  private Boolean legalPort() {
    return emptyPort() || legalNumericPort();
  }

  private Boolean legalNumericPort() {
    try {
      var numericPort = Integer.parseInt(port.toString());
      return numericPort > -1 && numericPort < 65536;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private Boolean emptyPort() {
    return port.length() == 0;
  }

  private RuntimeException illegalPort() {
    return new IllegalStateException(
      String.format(
        "Illegal port: <%s>", port.length() > 4096 ? port.toString().substring(0, 4096).concat("...") : port
      )
    );
  }

  @Override
  public String asString() {
    return port();
  }

  private final CharSequence port;
}
