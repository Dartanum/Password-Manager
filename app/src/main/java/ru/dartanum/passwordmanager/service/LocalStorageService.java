package ru.dartanum.passwordmanager.service;

import android.content.SharedPreferences;
import at.favre.lib.crypto.bcrypt.BCrypt;

import static android.content.SharedPreferences.*;

public class LocalStorageService {
    public static final String name = "storage";

    private static final String masterPasswordKey = "masterPassword";
    private static LocalStorageService instance;
    private SharedPreferences storage;

    private LocalStorageService(SharedPreferences storage) {
        this.storage = storage;
    }

    public static LocalStorageService getInstance(SharedPreferences preferences) {
        if (instance == null) {
            instance = new LocalStorageService(preferences);
        }
        return instance;
    }

    public boolean checkMasterPassword(String password) {
        String actualHash = storage.getString(masterPasswordKey, "");
        return BCrypt.verifyer().verify(password.toCharArray(), actualHash).verified;
    }

    public void createMasterPassword(String password) {
        Editor editor = storage.edit();
        editor.putString(masterPasswordKey, hashPassword(password));
        editor.apply();
    }

    public String getHashedMasterPassword() {
        return storage.getString(masterPasswordKey, "");
    }

    public boolean isMasterPasswordExist() {
        return !storage.getString(masterPasswordKey, "").equals("");
    }

    public void clearMasterPassword() {
        storage.edit().clear().apply();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
