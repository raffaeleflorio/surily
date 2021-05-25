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

/**
 * Authority component of an URI
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-3.2">RFC3986 about the authority component</a>
 * @since 1.0.0
 */
public interface AuthorityComponent extends UriComponent {
  /**
   * Builds the host subcomponent
   *
   * @return The subcomponent
   * @since 1.0.0
   */
  HostSubcomponent host();

  /**
   * Builds the port subcomponent
   *
   * @return The subcomponent
   * @since 1.0.0
   */
  PortSubcomponent port();

  /**
   * Builds the userinfo subcomponent
   *
   * @return The subcomponent
   * @since 1.0.0
   */
  UserinfoSubComponent userinfo();
}
