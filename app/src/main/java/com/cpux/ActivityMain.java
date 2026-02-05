package com.cpux;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cocosw.bottomsheet.BottomSheet;
import com.cpux.adapter.PagerAdapter;
import com.cpux.tools.LoaderData;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityMain extends FragmentActivity implements ActionBar.TabListener {


    PagerAdapter mAppSectionsPagerAdapter;
    private String[] tabTitle;
    //for ads
    ViewPager mViewPager;
    ActionBar actionBar;


    private ViewPager viewPager;
    private Toolbar toolbar;


    JSONObject json = null;
    String str = "";
    HttpResponse response;
    Context context;
    ProgressBar progressbar;
    String image_url, site_url, type;
    int i1;
    Boolean state = false;
    Boolean timer_state = false;
    String ads_state = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);


        progressbar = (ProgressBar) findViewById(R.id.progressBar1);

       /* if (isOnline()) {
            try {
                new Thread() {
                    @Override
                    public void run() {
                        String path = "http://studioapk.ir/ads.txt";
                        URL u = null;
                        try {
                            u = new URL(path);
                            HttpURLConnection c = (HttpURLConnection) u.openConnection();
                            c.setRequestMethod("GET");
                            c.connect();
                            final InputStream in = c.getInputStream();
                            final ByteArrayOutputStream bo = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            in.read(buffer); // Read from Buffer.
                            bo.write(buffer); // Write Into Buffer.

                            ActivityMain.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView textss = (TextView) findViewById(R.id.textView15);
                                    textss.setText(bo.toString());
                                    ads_state = textss.getText().toString();
                                    try {
                                        bo.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } catch (Exception e) {

            }
        }*/


        Timer timers1 = new Timer();
        timers1.schedule(new TimerTask() {
            public void run() {
                ActivityMain.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (isOnline() && ads_state.toString().trim().equals("yes")) {
                            try {
                                new GetTextViewData(ActivityMain.this).execute();
                                timer_state = true;
                            } catch (Exception e) {
                            }
                        } else {
                        }
                    }
                });
            }
        }, 3000);

        Timer timers12 = new Timer();
        timers12.schedule(new TimerTask() {
            public void run() {
                ActivityMain.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (timer_state == true) {
                            final int num = 1;
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                public void run() {
                                    ActivityMain.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            int run = 1;
                                            if (isOnline() && run == num) {
                                                viewPager = (ViewPager) findViewById(R.id.pager);
                                                viewPager.setVisibility(View.GONE);
                                                mViewPager.setVisibility(View.GONE);
                                                actionBar.hide();

                                                String types = getMimeType(image_url);
                                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                                if (types == "image/png") {
                                                    ImageView image = (ImageView) findViewById(R.id.imageView3);
                                                    Glide.with(getApplicationContext())
                                                            .load(image_url)
                                                            .into(image);
                                                    image.setVisibility(View.VISIBLE);
                                                    Button close = (Button) findViewById(R.id.close);
                                                    close.setVisibility(View.VISIBLE);
                                                    image.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            if (type.equals("website")) {
                                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(site_url));
                                                                startActivity(browserIntent);
                                                            } else if (type.equals("instagram")) {
                                                                Uri uri = Uri.parse("https://instagram.com/_u/" + site_url);
                                                                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                                                                likeIng.setPackage("com.instagram.android");

                                                                try {
                                                                    startActivity(likeIng);
                                                                } catch (ActivityNotFoundException e) {
                                                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                                                            Uri.parse("https://instagram.com/" + site_url)));
                                                                }
                                                            } else if (type.equals("telegram")) {
                                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=" + site_url));
                                                                startActivity(intent);
                                                            }
                                                            ImageView imageView = (ImageView) findViewById(R.id.imageView3);
                                                            imageView.setVisibility(View.GONE);
                                                            viewPager = (ViewPager) findViewById(R.id.pager);
                                                            viewPager.setVisibility(View.VISIBLE);
                                                            mViewPager.setVisibility(View.VISIBLE);
                                                            actionBar.show();
                                                            Button close = (Button) findViewById(R.id.close);
                                                            close.setVisibility(View.GONE);

                                                        }
                                                    });
                                                } else if (types == "image/gif") {
                                                    gif();
                                                } else if (types == "image/jpeg") {
                                                    ImageView image = (ImageView) findViewById(R.id.imageView3);
                                                    Glide.with(getApplicationContext())
                                                            .load(image_url)
                                                            .into(image);
                                                    mViewPager.setVisibility(View.GONE);
                                                    actionBar.hide();
                                                    image.setVisibility(View.VISIBLE);
                                                    final Button close = (Button) findViewById(R.id.close);
                                                    close.setVisibility(View.VISIBLE);
                                                    image.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            if (type.equals("website")) {
                                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(site_url));
                                                                startActivity(browserIntent);
                                                            } else if (type.equals("instagram")) {
                                                                Uri uri = Uri.parse("https://instagram.com/_u/" + site_url);
                                                                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                                                                likeIng.setPackage("com.instagram.android");

                                                                try {
                                                                    startActivity(likeIng);
                                                                } catch (ActivityNotFoundException e) {
                                                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                                                            Uri.parse("https://instagram.com/" + site_url)));
                                                                }
                                                            } else if (type.equals("telegram")) {
                                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=" + site_url));
                                                                startActivity(intent);
                                                            }
                                                            ImageView imageView = (ImageView) findViewById(R.id.imageView3);
                                                            imageView.setVisibility(View.GONE);
                                                            viewPager = (ViewPager) findViewById(R.id.pager);
                                                            viewPager.setVisibility(View.VISIBLE);
                                                            mViewPager.setVisibility(View.VISIBLE);
                                                            actionBar.show();
                                                            close.setVisibility(View.GONE);

                                                        }
                                                    });
                                                }
                                                run++;
                                            }
                                        }

                                    });
                                }
                            }, 10000);
                        }
                    }
                });
            }
        }, 5000);


        //makeActionOverflowMenuShown();
        tabTitle = getResources().getStringArray(R.array.tab_title);

        mAppSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabTitle.length);

        // Set up the action bar.
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        defineTabAnpageView();

    }

    public void defineTabAnpageView() {
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(tabTitle[i]).setTabListener(this));
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //mAppSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.

        //showInterstitial();

        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //mAppSectionsPagerAdapter.notifyDataSetChanged();
    }


    private Menu menu;
    // @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.refresh:
//            	new LoaderInfo(this).execute("");
//            return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    public class LoaderInfo extends AsyncTask<String, String, String> {
        LoaderData cpu = null;
        String status = "failed";
        Context context;

        public LoaderInfo(Activity act) {
            context = act;
            cpu = new LoaderData(act);
            menu.getItem(0).setVisible(false);
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                cpu.loadCpuInfo();
                cpu.loadBateryInfo();
                cpu.loadDeviceInfo();
                cpu.loadSystemInfo();
                cpu.loadSupportInfo();
                status = "succced";
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            setProgressBarIndeterminateVisibility(false);
            menu.getItem(0).setVisible(true);
            if (!status.equals("failed")) {
                Toast.makeText(context, "Info updated", Toast.LENGTH_SHORT).show();
                //refresh view
                mAppSectionsPagerAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(result);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        new BottomSheet.Builder(this).title("").sheet(R.menu.list).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.share:
                        startActivity(new Intent(ActivityMain.this, Email.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.call:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://programchi.ir"));
                        startActivity(browserIntent);
                        break;
                    case R.id.help:
                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setData(Uri.parse("bazaar://details?id=" + getPackageName()));
                        intent.setPackage("com.farsitel.bazaar");
                        startActivity(intent);
                        break;

                }
            }
        }).show();

        if (id == R.id.menu) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    private class GetTextViewData extends AsyncTask<Void, Void, Void> {
        public Context context;


        public GetTextViewData(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpClient myClient = new DefaultHttpClient();
            HttpPost myConnection = new HttpPost("http://studioapk.ir/ads/send-data.php");

            try {
                response = myClient.execute(myConnection);
                str = EntityUtils.toString(response.getEntity(), "UTF-8");

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONArray jArray = new JSONArray(str);
                int zero = 0;
                if (jArray.length() == zero) {
                    state = true;
                } else {
                    int length = jArray.length() - 1;
                    int min = 0;
                    int max = length;
                    Random r = new Random();
                    i1 = r.nextInt(max - min + 1) + min;
                    json = jArray.getJSONObject(i1);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            try {
                image_url = json.getString("image-url");
                site_url = json.getString("site-url");
                type = json.getString("type");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //Hiding progress bar after done loading TextView.
            progressbar.setVisibility(View.GONE);

        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public void gif() {
        final ImageView imageView = (ImageView) findViewById(R.id.imageView3);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
        Glide.with(getApplicationContext()).load(image_url).into(imageViewTarget);
        imageView.setVisibility(View.VISIBLE);
        final Button close = (Button) findViewById(R.id.close);
        close.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("website")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(site_url));
                    startActivity(browserIntent);
                } else if (type.equals("instagram")) {
                    Uri uri = Uri.parse("https://instagram.com/_u/" + site_url);
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                    likeIng.setPackage("com.instagram.android");

                    try {
                        startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://instagram.com/" + site_url)));
                    }
                } else if (type.equals("telegram")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=" + site_url));
                    startActivity(intent);
                }
                imageView.setVisibility(View.GONE);
                viewPager = (ViewPager) findViewById(R.id.pager);
                viewPager.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
                close.setVisibility(View.GONE);

            }
        });
    }

}
