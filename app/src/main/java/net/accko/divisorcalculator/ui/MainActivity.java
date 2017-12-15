package net.accko.divisorcalculator.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.accko.divisorcalculator.R;
import net.accko.divisorcalculator.util.PreferenceView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    ImageButton tabHome, tabStar, searchBack, searchClear, searchButton;
    EditText searchBar;
    Animation fadein, fadeout;
    TextView toolbarText, searchResult, result;
    TextView tv2 = null;
    LinearLayout tutorialLayout;
    RelativeLayout calculatorLayout, settingsLayout;
    PreferenceView preferenceVersion, preferenceInfo, preferenceContact, preferenceOsl, preferenceDev;

    int isPosition = 1;

    boolean clearTrue = true;
    boolean tutorial = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbarText = toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        toolbarText.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out); //animation settings

        findTab(); //find tab, onclick
        findSearchview(); //find searchview, onclick
        findPreference(); //find preference, onclick
        findMainview(); //find mainview, onclick

        tabAlpha(255, 100); //tabicon translucent

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
                        if (searchBar.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.this.getApplicationContext(), "cannot", Toast.LENGTH_SHORT).show(); //if edittext if blank
                        } else if (searchBar.getText().toString().startsWith("0")) {
                            Toast.makeText(MainActivity.this.getApplicationContext(), "cannot", Toast.LENGTH_SHORT).show(); //if edittext start number is 0
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
        preferenceOsl = findViewById(R.id.preferenceOsl);
        preferenceOsl.setOnClickListener(this);
        preferenceDev = findViewById(R.id.preferenceDev);
        preferenceDev.setOnClickListener(this);
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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);

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

                    isPosition = 1;
                }
                break;
            case R.id.tabStar:
                if (isPosition == 1) {
                    tabView(false, true);
                    tabAlpha(100, 255);

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
                break;
            case R.id.preferenceContact:
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:me@accko.net")));
                break;
            case R.id.preferenceOsl:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/accko199806/divisorcalculator-android")));
                break;
            case R.id.preferenceDev:
                break;
        }
    }
}