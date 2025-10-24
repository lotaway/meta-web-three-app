package com.metawebthree.app.broadcast;

import android.content.Context;

import androidx.annotation.NonNull;

public interface WatcherForActivity {
    void onCreate(@NonNull Context context);
    void onResume(@NonNull Context context);
    void onPause(@NonNull Context context);
    void onDestroy(@NonNull Context context);
}
