package com.example.ysl.servicebestpractice;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    private static final String TAG = "DownloadTask";

    private static final int STATUS_TYPE_SUCCESS = 0;
    private static final int STATUS_TYPE_FAILED = 1;
    private static final int STATUS_TYPE_PAUSED = 2;
    private static final int STATUS_TYPE_CANCELED = 3;

    private DownloadListener listener;
    private boolean isPaused = false;
    private boolean isCanceled = false;
    private int lastProgress = 0;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    public void pauseDownload() {
        this.isPaused = true;
    }

    public void cancelDownload() {
        this.isCanceled = true;
    }

    @Override
    protected void onPostExecute(Integer status) {
        // this will be called after doInBackground() by AsyncTask automatically
        switch (status) {
            case STATUS_TYPE_SUCCESS:
                Log.i(TAG, "onPostExecute: download success.");
                listener.onSuccess();
                break;
            case STATUS_TYPE_FAILED:
                Log.i(TAG, "onPostExecute: download failed.");
                listener.onFailed();
                break;
            case STATUS_TYPE_PAUSED:
                Log.i(TAG, "onPostExecute: download paused.");
                listener.onPaused();
                break;
            case STATUS_TYPE_CANCELED:
                Log.i(TAG, "onPostExecute: download canceled.");
                listener.onCancled();
                break;
            default:
                Log.i(TAG, "onPostExecute: Other invalid status: " + status);
                break;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        Log.i(TAG, "onProgressUpdate: progress ( " + progress + "% )");
        if (progress > lastProgress) {
            Log.i(TAG, "onProgressUpdate: progress update.");
            listener.onProgress(progress);
            // Update the lastProgress
            lastProgress = progress;
        } else {
            Log.i(TAG, "onProgressUpdate: progress not increase ... ");
        }
    }

    @Override
    protected Integer doInBackground(String... params) {

        InputStream inputStream = null;
        RandomAccessFile savedFile = null;
        File file = null;

        // Read the data from Internet and write to the local file in Stream
        try {
            long downloadedLength = 0;
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);

            if (file.exists()) {
                Log.i(TAG, "doInBackground: file exit... " + file.getAbsolutePath());
                downloadedLength = file.length();
            }

            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                Log.w(TAG, "doInBackground: download failed... content length is 0.");
                return STATUS_TYPE_FAILED;
            } else if (contentLength == downloadedLength){
                Log.i(TAG, "doInBackground: whole content was downloaded.");
                return STATUS_TYPE_SUCCESS;
            } else {
                Log.i(TAG, "doInBackground: whole content length: " + contentLength);
            }

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    // Support broken point download, find the position that begin download
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                inputStream = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downloadedLength);

                byte[] bytes = new byte[1024]; // Set the default read buffer size 1024 byte
                int total = 0;
                int len;
                while ((len = inputStream.read(bytes)) != -1) {
                    // Here need to consider all kinds of cases
                    if (isCanceled) {
                        Log.w(TAG, "doInBackground: download is canceled!");
                        return STATUS_TYPE_CANCELED;
                    } else if (isPaused) {
                        Log.w(TAG, "doInBackground: download is paused!");
                        return STATUS_TYPE_PAUSED;
                    } else {
                        total += len;
                        // Here do NOT need offset because we have seek already
                        savedFile.write(bytes, 0, total);

                        // Update the progress in percent
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        onProgressUpdate(progress);
                        // onProgressUpdate() will be called later by AsyncTask automatically
                    }
                }

                // Here do NOT forget close the response body
                response.body().close();
                return STATUS_TYPE_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (savedFile != null) {
                    savedFile.close();
                }

                // In case the download is interrupted or called
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Get the content length from the http server by using the download url.
     *
     * @param url the download url.
     * @return content length if success. Return 0 if failed.
     * @throws IOException if exception occurs when call OkHttpClient.Call.execute()
     */
    private long getContentLength(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        // The response from Http server is OK
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();

            // Here do NOT forget to close the response body
            response.body().close();
            return contentLength;
        }

        return 0;
    }

}
