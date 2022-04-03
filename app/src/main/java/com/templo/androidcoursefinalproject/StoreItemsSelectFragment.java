package com.templo.androidcoursefinalproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreItemsSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreItemsSelectFragment extends Fragment {
    WebView itemsGalleryWV;

    public StoreItemsSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SelectionItemFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreItemsSelectFragment newInstance() {
        StoreItemsSelectFragment fragment = new StoreItemsSelectFragment();
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
        return inflater.inflate(R.layout.fragment_store_items_select, container, false);
    }

    //getView(), requireView(), findViewById() need to be called after view is created.
    //Therefore, can place those statements inside onViewCreated().
    @Override
    @SuppressLint("SetJavaScriptEnabled") //Suppress warning.
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemsGalleryWV = requireView().findViewById(R.id.items_gallery_webview);
        WebSettings webSettings = itemsGalleryWV.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false); //Remove zooming magnifiers buttons.
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setBlockNetworkImage(false);//not block image

        //This is needed if JS alert() etc is needed to work.
        itemsGalleryWV.setWebChromeClient(new WebChromeClient());
        //This is required for setting custom events such as url loading overrides.
        itemsGalleryWV.setWebViewClient(new Callback());

        itemsGalleryWV.loadUrl("file:///android_asset/ItemsListsWeb/index.html");
        itemsGalleryWV.loadUrl("javascript:(function(){alert('Hello world!');})()");
    }

    private static class Callback extends WebViewClient {
        @Override //Don't override loading url from code.
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
    }
}