package com.gabrielezanelli.whatapic;

import android.app.Dialog;
import android.app.ProgressDialog;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;

import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.BindString;

public class InstagramDialog extends Dialog {
    private ProgressDialog loadingDialog;
    private WebView webView;
    private LinearLayout linearLayout;

    private String authenticationUrl;
    private String redirectUri;

    private InstagramDialogListener instagramListener;

    static final FrameLayout.LayoutParams totalMatch = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);

    @BindString(R.string.log_tag) String TAG;
    @BindString(R.string.loading_message) String loadingMessage;

    public InstagramDialog(Context context, String authUrl, String redirectUri, InstagramDialogListener listener) {
        super(context);

        authenticationUrl = authUrl;
        instagramListener = listener;
        this.redirectUri = redirectUri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingDialog = new ProgressDialog(getContext());

        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setMessage(loadingMessage);

        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(20,20,20,20);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setUpWebView();

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point outSize = new Point();

        int width = 0;
        int height = 0;

        double[] dimensions = new double[2];

        display.getSize(outSize);

        width	= outSize.x;
        height	= outSize.y;

        if (width < height) {
            dimensions[0]	= 0.87 * width;
            dimensions[1]	= 0.82 * height;
        } else {
            dimensions[0]	= 0.75 * width;
            dimensions[1]	= 0.75 * height;
        }

        addContentView(linearLayout, new FrameLayout.LayoutParams((int) dimensions[0], (int) dimensions[1]));
    }

    private void setUpWebView() {
        webView = new WebView(getContext());

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new InstagramWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(authenticationUrl);
        webView.setLayoutParams(totalMatch);

        WebSettings webSettings = webView.getSettings();

        webSettings.setSaveFormData(false);

        linearLayout.addView(webView);
    }

    public void clearCache() {
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        instagramListener.onCancel();

    }

    private class InstagramWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirecting URL " + url);

            if (url.startsWith(redirectUri)) {
                if (url.contains("access_token")) {
                    String temp[] = url.split("=");

                    instagramListener.onSuccess(temp[1]);
                } else if (url.contains("error")) {
                    String temp[] = url.split("=");

                    instagramListener.onError(temp[temp.length-1]);
                }

                InstagramDialog.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error ) {
            super.onReceivedError(view, request, error);
            instagramListener.onError(error.toString());
            Log.d(TAG, "Request error: " + error.toString());

            InstagramDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            loadingDialog.show();

            Log.d(TAG, "Loading URL: " + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            loadingDialog.dismiss();
        }
    }

    public interface InstagramDialogListener {
        public abstract void onSuccess(String code);
        public abstract void onCancel();
        public abstract void onError(String error);
    }
}