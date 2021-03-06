package com.gabrielezanelli.whatapic;

import android.app.Dialog;
import android.app.ProgressDialog;

import android.graphics.Bitmap;
import android.graphics.Point;

import android.os.Build;
import android.os.Bundle;
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

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Dialog containing WebView for instagram's authorization page
 */

public class InstagramDialog extends Dialog {
    private ProgressDialog loadingDialog;
    private WebView webView;
    private LinearLayout linearLayout;

    private String authenticationUrl;
    private String redirectUri;

    private InstagramDialogListener instagramListener;

    private static final FrameLayout.LayoutParams totalMatch = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);

    @BindString(R.string.loading_message)
    String loadingMessage;
    @BindInt(R.integer.instagram_dialog_padding)
    int dialogPadding;

    public InstagramDialog(Context context, String authenticationUrl, String redirectUri,
                           InstagramDialogListener instagramListener) {
        super(context);

        this.authenticationUrl = authenticationUrl;
        this.instagramListener = instagramListener;
        this.redirectUri = redirectUri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this);

        loadingDialog = new ProgressDialog(getContext());
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setMessage(loadingMessage);

        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(dialogPadding, dialogPadding, dialogPadding, dialogPadding);

        setUpWebView();

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point outSize = new Point();

        int width, height;

        double[] dimensions = new double[2];

        display.getSize(outSize);

        width = outSize.x;
        height = outSize.y;

        if (width < height) {
            dimensions[0] = 0.87 * width;
            dimensions[1] = 0.45 * height;
        } else {
            dimensions[0] = 0.75 * width;
            dimensions[1] = 0.75 * height;
        }

        addContentView(linearLayout, new FrameLayout.LayoutParams((int) dimensions[0], (int) dimensions[1]));
    }

    private void setUpWebView() {
        webView = new WebView(getContext());

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new InstagramWebViewClient());
        webView.loadUrl(authenticationUrl);
        webView.setLayoutParams(totalMatch);

        WebSettings webSettings = webView.getSettings();
        webSettings.setSaveFormData(false);

        linearLayout.addView(webView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        instagramListener.onCancel();
    }

    private class InstagramWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= 21) {
                String url = request.getUrl().toString();

                if (url.startsWith(redirectUri)) {
                    if (url.contains("access_token")) {
                        String temp[] = url.split("=");

                        instagramListener.onSuccess(temp[1]);
                    } else if (url.contains("error")) {
                        String temp[] = url.split("=");

                        instagramListener.onError(temp[temp.length - 1]);
                    }
                    dismiss();
                    return true;
                }
                return false;
            } else
                return super.shouldOverrideUrlLoading(view, request);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Build.VERSION.SDK_INT < 21) {

                if (url.startsWith(redirectUri)) {
                    if (url.contains("access_token")) {
                        String temp[] = url.split("=");

                        instagramListener.onSuccess(temp[1]);
                    } else if (url.contains("error")) {
                        String temp[] = url.split("=");

                        instagramListener.onError(temp[temp.length - 1]);
                    }
                    dismiss();
                    return true;
                }
                return false;
            } else
                return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            instagramListener.onError(error.toString());

            dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            loadingDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadingDialog.dismiss();
        }
    }

    public interface InstagramDialogListener {
        void onSuccess(String token);
        void onCancel();
        void onError(String error);
    }
}