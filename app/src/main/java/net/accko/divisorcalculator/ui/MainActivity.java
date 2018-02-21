package net.accko.divisorcalculator.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import net.accko.divisorcalculator.R;
import net.accko.divisorcalculator.util.PreferenceView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RewardedVideoAdListener {

    Toolbar toolbar;
    ImageButton tabHome, tabStar, searchBack, searchClear, searchButton;
    EditText searchBar;
    Animation fadein, fadeout;
    TextView toolbarText, searchResult, result;
    TextView tv2 = null;
    LinearLayout tutorialLayout, adLayout;
    RelativeLayout calculatorLayout, settingsLayout;
    PreferenceView preferenceVersion, preferenceInfo, preferenceContact, preferenceOsp, preferenceDev, preferenceRemoveAds;
    InputMethodManager imm;
    SharedPreferences divisor_sp;
    SharedPreferences.Editor divisor_sp_ed;

    int adsNumber;
    int isPosition = 1;

    boolean clearTrue = true;
    boolean tutorial = true;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        divisor_sp = getSharedPreferences("divisor", MODE_PRIVATE);
        divisor_sp_ed = divisor_sp.edit();

        adsNumber = divisor_sp.getInt("adsNumberSp", 0);

        toolbar = findViewById(R.id.toolbar);
        toolbarText = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbarText.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out); //animation settings

        findAds(); //find ads, show
        findTab(); //find tab, onclick
        findSearchview(); //find searchview, onclick
        findPreference(); //find preference, onclick
        findMainview(); //find mainview, onclick

        tabAlpha(255, 100); //tabicon translucent

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchBar.length() == 0) {
                    searchClear.setVisibility(View.GONE);
                    searchClear.startAnimation(fadeout);
                    clearTrue = true;
                }
                if (searchBar.length() == 1) {
                    if (clearTrue == true) {
                        clearTrue = false;
                        searchClear.setVisibility(View.VISIBLE);
                        searchClear.startAnimation(fadein);
                    } else {
                        searchClear.setVisibility(View.VISIBLE);
                    }
                }
                if (searchBar.length() == 2) {
                    searchClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        if (mInterstitialAd.isLoaded() && divisor_sp.getInt("adsNumberSp", 0) == 0) {
                            mInterstitialAd.show();
                        } //Interstitial Ads

                        if (searchBar.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.this.getApplicationContext(), "cannot", Toast.LENGTH_SHORT).show(); //if edittext is blank
                        } else if (searchBar.getText().toString().startsWith("0")) {
                            Toast.makeText(MainActivity.this.getApplicationContext(), "cannot", Toast.LENGTH_SHORT).show(); //if edittext start number is 0
                        } else if (searchBar.getText().toString().equals("1")) { //if edittext text is 1
                            tutorialLayout.setVisibility(View.GONE);
                            if (tutorial == true) {
                                tutorialLayout.setAnimation(fadeout);
                            }
                            tutorial = false;

                            result.setText("\n1");
                            result.startAnimation(fadein);
                            searchReturn();

                            searchResult.setText("1" + " / " + "1" + getString(R.string.result));
                        } else {
                            tutorialLayout.setVisibility(View.GONE);
                            if (tutorial == true) {
                                tutorialLayout.setAnimation(fadeout);
                            }
                            tutorial = false;

                            final ArrayList<Long> divisors = new ArrayList();
                            String enteredNumber = searchBar.getText().toString();
                            long num = Long.parseLong(searchBar.getText().toString());
                            long i = 2;
                            divisors.add(Long.valueOf(1));
                            double sqrt = Math.sqrt((double) num);
                            while (((double) i) < sqrt) {
                                if (num % i == 0) {
                                    divisors.add(Long.valueOf(i));
                                }
                                i++;
                            }
                            int length = divisors.size() - 1;
                            if (i * i == num) {
                                divisors.add(Long.valueOf(i));
                            }
                            while (length > 0) {
                                divisors.add(Long.valueOf(num / ((Long) divisors.get(length)).longValue()));
                                length--;
                            }
                            divisors.add(Long.valueOf(num));

                            result.setText("\n" + TextUtils.join(", ", divisors) + "\n");
                            result.startAnimation(fadein);
                            searchReturn();

                            searchResult.setText(enteredNumber + " / " + divisors.size() + getString(R.string.result));
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    void findAds() {
        adLayout = findViewById(R.id.adLayout);
        if (divisor_sp.getInt("adsNumberSp", 0) != 0) {
            adLayout.setVisibility(View.GONE);
        }
        mAdView = findViewById(R.id.adView);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8184195003057423/3560691791");
        mInterstitialAd.loadAd(new AdRequest.Builder().build()); //InterstitilAd

        MobileAds.initialize(this, "ca-app-pub-8184195003057423/2039076197");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest); //banner

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build()); //reward
    }

    void findTab() {
        tabHome = findViewById(R.id.tabHome);
        tabHome.setOnClickListener(this);
        tabStar = findViewById(R.id.tabStar);
        tabStar.setOnClickListener(this);
    }

    void findSearchview() {
        searchBack = findViewById(R.id.searchBack);
        searchBack.setOnClickListener(this);
        searchClear = findViewById(R.id.searchClear);
        searchClear.setOnClickListener(this);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        searchResult = findViewById(R.id.searchResult);
        searchBar = findViewById(R.id.searchBar);
        searchBack.setVisibility(View.GONE);
        searchBar.setVisibility(View.GONE);
        searchClear.setVisibility(View.GONE);
    }

    void findMainview() {
        result = findViewById(R.id.result);
        result.setMovementMethod(new ScrollingMovementMethod());

        tutorialLayout = findViewById(R.id.tutorialLayout);
        calculatorLayout = findViewById(R.id.calculatorLayout);
        settingsLayout = findViewById(R.id.settingsLayout); //layout
        settingsLayout.setVisibility(View.GONE);
    }

    void findPreference() {
        preferenceVersion = findViewById(R.id.preferenceVersion);
        preferenceVersion.setOnClickListener(this);
        preferenceInfo = findViewById(R.id.preferenceInfo);
        preferenceInfo.setOnClickListener(this);
        preferenceContact = findViewById(R.id.preferenceContact);
        preferenceContact.setOnClickListener(this);
        preferenceOsp = findViewById(R.id.preferenceOsp);
        preferenceOsp.setOnClickListener(this);
        preferenceDev = findViewById(R.id.preferenceDev);
        preferenceDev.setOnClickListener(this);
        preferenceRemoveAds = findViewById(R.id.preferenceRemoveAds);
        preferenceRemoveAds.setOnClickListener(this);
    }

    void searchMode() {
        searchButton.setVisibility(View.GONE);
        searchButton.startAnimation(fadeout);
        searchResult.setVisibility(View.GONE);
        searchButton.startAnimation(fadeout);
        searchBack.setVisibility(View.VISIBLE);
        searchBack.startAnimation(fadein);
        searchBar.setVisibility(View.VISIBLE);
        searchBar.startAnimation(fadein);

        imm.showSoftInput(searchBar, 0); //keyboard show

        if (tutorial == true) {
            tutorialLayout.setVisibility(View.GONE);
            tutorialLayout.startAnimation(fadeout);
        }
    }

    void searchReturn() {
        searchBar.setVisibility(View.GONE);
        searchBar.startAnimation(fadeout);
        searchBar.setText("");
        searchBack.setVisibility(View.GONE);
        searchBack.startAnimation(fadeout);
        searchClear.setVisibility(View.GONE);
        searchClear.startAnimation(fadeout);
        searchResult.setVisibility(View.VISIBLE);
        searchButton.startAnimation(fadein);
        searchButton.setVisibility(View.VISIBLE);
        searchButton.startAnimation(fadein);

        imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0); //keyboard hide

        if (tutorial == true) {
            tutorialLayout.setVisibility(View.VISIBLE);
            tutorialLayout.startAnimation(fadein);
        }
    }

    void tabAlpha(int calculator, int settings) {
        tabHome.setAlpha(calculator);
        tabStar.setAlpha(settings);
    }

    void tabView(boolean calVisibility, boolean setVisibility) {
        if (calVisibility == true) {
            calculatorLayout.setVisibility(View.VISIBLE);
            calculatorLayout.startAnimation(fadein);
        } else if (calVisibility == false) {
            calculatorLayout.setVisibility(View.GONE);
            calculatorLayout.startAnimation(fadeout);
        }
        if (setVisibility == true) {
            settingsLayout.setVisibility(View.VISIBLE);
            settingsLayout.startAnimation(fadein);
        } else if (setVisibility == false) {
            settingsLayout.setVisibility(View.GONE);
            settingsLayout.startAnimation(fadeout);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tabHome:
                if (isPosition == 2) {
                    tabView(true, false);
                    tabAlpha(255, 100);
                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0); //keyboard hide
                    isPosition = 1;
                }
                break;

            case R.id.tabStar:
                if (isPosition == 1) {
                    tabView(false, true);
                    tabAlpha(100, 255);
                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0); //keyboard hide
                    isPosition = 2;
                }
                break;

            case R.id.searchButton:
                searchMode();
                break;

            case R.id.searchBack:
                searchReturn();
                break;

            case R.id.searchClear:
                searchBar.setText("");
                break;

            case R.id.preferenceVersion:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.accko.divisorcalculator")));
                break;

            case R.id.preferenceInfo:
                LayoutInflater titleInflater = getLayoutInflater();
                final View viewTitle = titleInflater.inflate(R.layout.dialog_title, null);
                TextView dialogTitleText = viewTitle.findViewById(R.id.title);
                dialogTitleText.setText(getString(R.string.app_info));

                LayoutInflater infoInflater = getLayoutInflater();
                final View viewInfo = infoInflater.inflate(R.layout.dialog_appinfo, null);

                AlertDialog.Builder infoBuider = new AlertDialog.Builder(this);
                infoBuider.setCustomTitle(viewTitle);
                infoBuider.setView(viewInfo);
                AlertDialog infoDialog = infoBuider.create();
                infoDialog.show();
                break;

            case R.id.preferenceContact:
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:me@accko.net")));
                break;

            case R.id.preferenceOsp:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/accko199806/divisorcalculator-android")));
                break;

            case R.id.preferenceDev:
                LayoutInflater titleInflater2 = getLayoutInflater();
                final View viewTitle2 = titleInflater2.inflate(R.layout.dialog_title, null);
                ImageView titleImage = viewTitle2.findViewById(R.id.titleImage);
                titleImage.setImageResource(R.drawable.ic_person_black_24dp);
                TextView dialogTitleText2 = viewTitle2.findViewById(R.id.title);
                dialogTitleText2.setText(getString(R.string.developer));

                LayoutInflater devInflater = getLayoutInflater();
                final View viewDev = devInflater.inflate(R.layout.dialog_dev, null);
                TextView textDev = viewDev.findViewById(R.id.textDev);
                TextView blog = viewDev.findViewById(R.id.blog);
                blog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.accko.net")));
                    }
                });
                TextView googleplus = viewDev.findViewById(R.id.googleplus);
                googleplus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+학이")));
                    }
                });
                TextView twitter = viewDev.findViewById(R.id.twitter);
                twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/accko199806")));
                    }
                });
                TextView github = viewDev.findViewById(R.id.github); //view find
                github.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/accko199806")));
                    }
                });

                long now = System.currentTimeMillis(); //now time
                Date date = new Date(now); //new date
                SimpleDateFormat shiftNow = new SimpleDateFormat("yyyy");
                String getTimeStr = shiftNow.format(date); //change form
                int getTime = Integer.parseInt(getTimeStr); //set time string to int
                String myInfo;
                if (getTime >= 2019) {
                    myInfo = getString(R.string.developer_text_age_after);
                } else {
                    myInfo = getString(R.string.developer_text_age);
                }
                textDev.setText(myInfo + "\n" + getString(R.string.developer_text_dev) + "\n" + getString(R.string.developer_text_more));

                AlertDialog.Builder deleteAdsBuider = new AlertDialog.Builder(this);
                deleteAdsBuider.setCustomTitle(viewTitle2);
                deleteAdsBuider.setView(viewDev);
                AlertDialog deleteAdsDialog = deleteAdsBuider.create();
                deleteAdsDialog.show();
                break;

            case R.id.preferenceRemoveAds:
                LayoutInflater titleInflater3 = getLayoutInflater();
                final View viewTitle3 = titleInflater3.inflate(R.layout.dialog_title, null);
                ImageView titleImage2 = viewTitle3.findViewById(R.id.titleImage);
                titleImage2.setImageResource(R.drawable.ic_remove_black_24dp);
                TextView dialogTitleText3 = viewTitle3.findViewById(R.id.title);
                dialogTitleText3.setText(getString(R.string.remove_ads));

                LayoutInflater removeAdsInflater = getLayoutInflater();
                final View viewRemoveAds = removeAdsInflater.inflate(R.layout.dialog_removeads, null);
                TextView numberAds = viewRemoveAds.findViewById(R.id.numberAds);
                numberAds.setText(getString(R.string.number_ads) + " " + String.valueOf(divisor_sp.getInt("adsNumberSp", 0)));
                Button takeAds = viewRemoveAds.findViewById(R.id.takeAds);

                AlertDialog.Builder removeAdsBuider = new AlertDialog.Builder(this);
                removeAdsBuider.setCustomTitle(viewTitle3);
                removeAdsBuider.setView(viewRemoveAds);
                final AlertDialog removeAdsDialog = removeAdsBuider.create();
                removeAdsDialog.show();

                takeAds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mRewardedVideoAd.isLoaded()) {
                            mRewardedVideoAd.show();
                            removeAdsDialog.dismiss();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    public void onRewarded(RewardItem reward) { // Reward the user.
        if (divisor_sp.getInt("adsNumberSp", 0) == 0) {
            adLayout.setVisibility(View.GONE);
        }

        Toast.makeText(getApplicationContext(), getString(R.string.ads_participation), Toast.LENGTH_LONG).show();
        adsNumber++;
        divisor_sp_ed.putInt("adsNumberSp", adsNumber);
        divisor_sp_ed.apply();
    }

    public void onRewardedVideoAdLeftApplication() {
    }

    public void onRewardedVideoAdClosed() {
    }

    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(getApplicationContext(), getString(R.string.ads_load_fail), Toast.LENGTH_SHORT).show();
    }

    public void onRewardedVideoAdLoaded() {
    }

    public void onRewardedVideoAdOpened() {
    }

    public void onRewardedVideoStarted() {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}