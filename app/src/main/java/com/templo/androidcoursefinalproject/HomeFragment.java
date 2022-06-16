package com.templo.androidcoursefinalproject;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.templo.androidcoursefinalproject.room_database.model.Product;
import com.templo.androidcoursefinalproject.room_database.model.ProductViewModel;
import com.templo.androidcoursefinalproject.room_database.model.UserViewModel;

import java.util.List;

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
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false); //Remove zooming magnifiers buttons.
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setBlockNetworkImage(false);//not block image

        //This is needed if JS alert() etc is needed to work.
        homeFragWV.setWebChromeClient(new WebChromeClient() {
            //Log console message from WebView.
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("WebViewConsole", consoleMessage.message() + " -- From line " +
                        consoleMessage.lineNumber() + " of " + consoleMessage.sourceId());
                return true;
            }
        });
        //This is required for setting custom events such as url loading overrides.
        homeFragWV.setWebViewClient(new Callback());

        //For calling to the class's methods from WebView's JS.
        homeFragWV.addJavascriptInterface(new JSCallbacks(), "jsc");

//        webSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
//        CookieManager.getInstance().setAcceptThirdPartyCookies(homeFragWV, true);

        homeFragWV.loadUrl("file:///android_asset/ItemsListsWeb/index.html");
//        homeFragWV.loadUrl("javascript:(function(){alert('Hello world!');})()");

        ImageButton cartBtn = requireView().findViewById(R.id.shopping_cart_btn);
        cartBtn.setOnClickListener(v -> {
            Log.d("TAG", "Click cart button!");
        });
    }

    private class Callback extends WebViewClient {
        private boolean homePageLoaded = false;

        @Override //Don't override loading url from code.
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        //Need to wait for the website to load complete before calling insertGallery() custom function;
        //Otherwise, the function might be called before it's even instantiated.
        public void onPageFinished(WebView view, String url) {
            if (homePageLoaded) return;
            homePageLoaded = true;
            ProductViewModel productViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
                    .create(ProductViewModel.class);
            productViewModel.getAllProducts().observe(HomeFragment.this.requireActivity(), allProducts -> {
                Log.d("ALLPRODUCTS", "COUNT: " + allProducts.size());
                allProducts.forEach(product -> {
                    String argument = "javascript:insertGallery("
                            + "\"" + product.getImage1() + "\","
                            + "\"" + product.getDescription() + "\""
                            + "," + product.getId() + ")";
                    homeFragWV.loadUrl(argument);
                });
            });
        }
    }

    private class JSCallbacks {
        //Mark with @JavascriptInterface annotation to make this callback function accessible within JS in WebView.
        @JavascriptInterface
        public void showListItem(int productId) {
            Activity homeFragmentActivity = HomeFragment.this.requireActivity();
            ProductViewModel productViewModel = new ViewModelProvider.AndroidViewModelFactory(homeFragmentActivity.getApplication())
                    .create(ProductViewModel.class);

            //This makes the code in run() execute in main thread. .observer() cannot work on background thread.
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    productViewModel.getProduct(homeFragmentActivity.getApplication(), productId).observe(HomeFragment.this.requireActivity(), product -> {
                        Dialog itemDialog = new Dialog(homeFragmentActivity);
                        itemDialog.setContentView(R.layout.item_custom_dialog);
                        itemDialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_bg);
                        itemDialog.show();
                    });
                }
            });

        }
    }
}