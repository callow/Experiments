// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.
package security.credentialstorage.implementation.windows;

import java.util.Objects;

import security.credentialstorage.model.StoredCredential;

/**
 * Credential Manager store for a credential.
 */
public final class CredManagerBackedCredentialStore extends CredManagerBackedSecureStore<StoredCredential> {

  @Override
  public boolean add(final String key, final StoredCredential secret) {
    Objects.requireNonNull(key, "key cannot be null");
    Objects.requireNonNull(secret, "secret cannot be null");

    logger.info("Adding secret for {}", key);

    return writeSecret(key, secret.getUsername(), secret.getPassword());
  }

  @Override
  protected StoredCredential create(final String username, final char[] secret) {
    return new StoredCredential(username, secret);
  }
}
