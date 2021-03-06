package com.stylingandroid.downloadmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements Downloader.Listener {
    private static final String URI_STRING = "http://www.cbu.edu.zm/downloads/pdf-sample.pdf";

    private Button download;

    private Downloader downloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        download = (Button) findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadOrCancel();
            }
        });
        downloader = Downloader.newInstance(this);
    }

    void downloadOrCancel() {
        if (downloader.isDownloading()) {
            cancel();
        } else {
            download();
        }
        updateUi();
    }

    private void cancel() {
        downloader.cancel();
    }

    private void download() {
        Uri uri = Uri.parse(URI_STRING);
        downloader.download(uri);
    }

    @Override
    public void fileDownloaded(Uri uri, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);
        startActivity(intent);
        updateUi();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    private void updateUi() {
        if (downloader.isDownloading()) {
            download.setText(R.string.cancel);
        } else {
            download.setText(R.string.download);
        }
    }

    @Override
    protected void onDestroy() {
        downloader.unregister();
        super.onDestroy();
    }
}
