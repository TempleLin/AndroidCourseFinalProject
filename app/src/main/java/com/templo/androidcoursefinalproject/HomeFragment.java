package com.templo.androidcoursefinalproject;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    WebView homeFragWV;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //getView(), requireView(), findViewById() need to be called after view is created.
    //Therefore, can place those statements inside onViewCreated().
    @Override
    @SuppressLint("SetJavaScriptEnabled") //Suppress warning.
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeFragWV = requireView().findViewById(R.id.home_frag_webview);
        WebSettings webSettings = homeFragWV.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false); //Remove zooming magnifiers buttons.
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setBlockNetworkImage(false);//not block image

        //This is needed if JS alert() etc is needed to work.
        homeFragWV.setWebChromeClient(new WebChromeClient());
        //This is required for setting custom events such as url loading overrides.
        homeFragWV.setWebViewClient(new Callback());

//        webSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
//        CookieManager.getInstance().setAcceptThirdPartyCookies(homeFragWV, true);

        homeFragWV.loadUrl("file:///android_asset/ItemsListsWeb/index.html");
//        homeFragWV.loadUrl("javascript:(function(){alert('Hello world!');})()");

        ImageButton cartBtn = requireView().findViewById(R.id.shopping_cart_btn);
        cartBtn.setOnClickListener(v -> {
            Log.d("TAG", "Click cart button!");
        });
    }

    private static class Callback extends WebViewClient {
        @Override //Don't override loading url from code.
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
    }
}