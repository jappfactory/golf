package kr.appfactory.golf;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AdsFull {

    private static InterstitialAd adFull;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static AdsFull instance = null;
    private static Context context;
    private InterstitialAd mInterstitialAd;

    public AdsFull(Context context) {
        this.context = context;
    }

    public static AdsFull getInstance(Context context) {


        if (instance == null) {
            instance = new AdsFull(context);
           // adFull = new InterstitialAd(context);
           // setAds2(this);
        }

        return instance;
    }


    public void  setAdsFull() {//

        MobileAds.initialize(context, context.getResources().getString(R.string.ap_ad_id));
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.banner_ad_unit_id2));

        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
               //.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
                .build();

        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded(){
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("asd", "The interstitial wasn't loaded yet.");
                }
            }
        });
    }

    public void  setAds(Context context) {//

        MobileAds.initialize(context, context.getResources().getString(R.string.ap_ad_id));

        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");//test

//

        Log.d("adView", "" + AdRequest.DEVICE_ID_EMULATOR);
        AdView adView = (AdView) ((Activity) context).findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
                .build();
        adView.loadAd(adRequest);

    }


}
