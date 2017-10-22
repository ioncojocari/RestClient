package com.example.ion.restclient;

import android.support.annotation.Nullable;

/**
 * This interface should can display Error and success
 */

public interface IDisplayResponse {
    void error(@Nullable String message);
    void success(@Nullable String message);
}
