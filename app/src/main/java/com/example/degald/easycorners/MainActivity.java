package com.example.degald.easycorners;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;


import com.jjoe64.graphview.GraphView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private WebView mWebView;
    final ArrayList<Integer> corners_array = new ArrayList<Integer>();
    private GraphView graph;
    private ProgressBar mProgressBar;

    public static final String INDICATOR_ID = "dfgfhd";
    public static final String OUTGOING_CORNERS = "corners";
    public static final String TITLE_ID = "sdgfdsag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graph = (GraphView) findViewById(R.id.graph);
        mWebView = (WebView) findViewById(R.id.browser);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.loadUrl("https://corner-stats.com/index.php?route=account/login&amp;red_route=");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.INVISIBLE);
                if (url.equals("https://corner-stats.com/index.php?route=account/login&amp;red_route=")) {
                    view.loadUrl("javascript: {" +
                            "  var form = document.getElementById('login');" +
                            "  form.getElementsByTagName('input')[0].value = 'rizzborngood@gmail.com';" +
                            "  form.getElementsByTagName('input')[1].value = 'UhttlBpUjjl';" +
                            "  document.forms['login'].submit();" +
                            "}");
                    view.loadUrl("javascript: {document.getElementById('mob-filtr').click();");
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    public void filterIt(final int indicator) {
        if (!(mWebView.getProgress() == 100)){
            Toast.makeText(MainActivity.this, "The page ain't downloaded yet!!!", Toast.LENGTH_LONG).show();
            return;
        }

        mWebView.evaluateJavascript("(function(){return document.getElementsByTagName('html')[0].innerHTML})();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        html = html.replace("\\u003C", "<");
                        html = html.replace("\\n", "");
                        html = html.replace("\\t", "");
                        html = html.replace("\\", "");
                        html = "<html>" + html + "</html";

                        String url = mWebView.getUrl();
                        String[] urlSplitted = url.split("/");

                        Document document = Jsoup.parse(html);
                        String welcomeMessage = document.select("span[class=login-name]").html();
                        Element table = document.getElementById("table_corners");
                        Elements corners = table.getElementsByClass("team_2_corners_quantity");

                        Elements combined = table.select(".team_1_corners_quantity, .team_2_corners_quantity, a[href ^='/" + urlSplitted[3] + "/']");

                        String title = urlSplitted[3];

                        if (indicator == 1) {
                            for (int i = 1; i < combined.size(); i += 3) {
                                if (!combined.get(i).text().equals("?")) {
                                    corners_array.add(Integer.valueOf(combined.get(i).text()));
                                }
                            }
                        } else {
                            for (int i = 0; i < combined.size(); i += 3) {
                                if (((Element) combined.get(i)).text().toCharArray().length > 2) {
                                    if (!((Element) combined.get(i + 2)).text().equals("?")) {
                                        corners_array.add(Integer.valueOf(((Element) combined.get(i + 2)).text()));
                                    }
                                } else if (!((Element) combined.get(i)).text().equals("?")) {
                                    corners_array.add(Integer.valueOf(((Element) combined.get(i)).text()));
                                }
                            }
                        }
                        makeVisible(title, indicator);
                    }
                });


    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        corners_array.clear();
        super.onResume();
    }

    public void makeVisible(String title, int indicator) {
        Intent intent = new Intent(this, FullscreenActivity.class);
        Integer[] cornersArray = corners_array.toArray(new Integer[corners_array.size()]);
        int[] corners = new int[cornersArray.length];
        for (int i = 0; i < cornersArray.length; i++) {
            corners[i] = cornersArray[i];
        }
        intent.putExtra(TITLE_ID, title);
        intent.putExtra(OUTGOING_CORNERS, corners);
        intent.putExtra(INDICATOR_ID, indicator);
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idSelectedAction = item.getItemId();
        Switch switch_widget = (Switch) findViewById(R.id.switch_widget);
        int indicator = 0;
        if (switch_widget.isChecked())
            indicator = 1;


        switch (idSelectedAction) {
            case R.id.action_team: {
                filterIt(indicator);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
