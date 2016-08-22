package com.amanapp.ui.models;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.amanapp.R;
import com.amanapp.cnnections.DropboxClientFactory;
import com.amanapp.tasks.UploadFileTask;
import com.dropbox.core.v2.files.FileMetadata;

/**
 * Created by Abdullah ALT on 8/20/2016.
 */
public class UploadOperation extends Operation<FileMetadata> {

    private final String uri;
    private final String path;
    private final String name;

    public UploadOperation(@NonNull Context context, @NonNull Activity activity, @NonNull String path, @NonNull String name, @NonNull String uri) {
        super(context, activity, FileAction.UPLOAD);
        this.path = path;
        this.name = name;
        this.uri = uri;
    }

    @Override
    protected void startAction() {
        new UploadFileTask(context, DropboxClientFactory.getClient(), this).execute(path, name, uri);
    }

    @Override
    protected void onPermissionDenied(String permission) {
        Toast.makeText(context,
                "Can't upload file: read access denied. " +
                        "Please grant storage permissions to use this functionality.",
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    protected void onPermissionGranted(String waitingMessage) {
        performAction(action, waitingMessage);
    }

    @Override
    public void onTaskComplete(FileMetadata result) {
        dialog.dismiss();
        Toast.makeText(context, "The file has been uploaded", Toast.LENGTH_LONG).show();
        Snackbar.make(activity.findViewById(R.id.container), "Test", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onError(Exception e) {
        dialog.dismiss();
        Toast.makeText(context, "Error uploading the file", Toast.LENGTH_LONG).show();
    }
}