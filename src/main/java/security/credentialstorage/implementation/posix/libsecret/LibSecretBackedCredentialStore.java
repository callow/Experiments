// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.
package security.credentialstorage.implementation.posix.libsecret;

import java.util.Objects;

import com.sun.jna.ptr.PointerByReference;

import security.credentialstorage.implementation.posix.internal.GLibLibrary;
import security.credentialstorage.model.StoredCredential;

/**
 * Libsecret store for a credential.
 */
public final class LibSecretBackedCredentialStore extends LibSecretBackedSecureStore<StoredCredential> {

  @Override
  public boolean add(final String key, StoredCredential secret) {
    Objects.requireNonNull(key, "key cannot be null");
    Objects.requireNonNull(secret, "secret cannot be null");

    if (INSTANCE != null && SCHEMA != null) {
      logger.info("Adding a {} for {}", getType(), key);

      final PointerByReference error = new PointerByReference();
      try {
        return writeSecret(key, secret.getUsername(), secret.getPassword(), error);
      } finally {
        if (error.getValue() != null) {
          GLibLibrary.INSTANCE.g_error_free(error.getValue());
        }
      }
    }

    logger.warn("Libsecret is not available.");
    return false;
  }

  @Override
  protected StoredCredential create(String username, char[] secret) {
    return new StoredCredential(username, secret);
  }

  @Override
  protected String getType() {
    return "Credential";
  }
}
