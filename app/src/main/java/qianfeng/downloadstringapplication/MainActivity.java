package qianfeng.downloadstringapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String url="http://m2.qiushibaike.com/article/list/text?page=1&count=30&readarticles=[115335035]&rqcnt=17&r=d0dc8ad41456830262443";
    private TextView mTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = ((TextView) findViewById(R.id.tv));
        new MyAsyncTask().execute(url);
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute(); // 一般可以在这里加入进度条的显示
        }

        @Override
        protected String doInBackground(String... params) {

            String str =  http(params[0]);
            String st = parseJson(str);
            return st;
        }

        @Override
        protected void onPostExecute(String s) {
           mTv.setText(s);
        }
        private String http(String params)
        {
            HttpURLConnection httpURLConnection = null;
            BufferedReader br = null;
            try {
                URL url = new URL(params);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();
                if(httpURLConnection.getResponseCode() == 200)
                {
                    // 连接成功了，要做什么？从网站返回字符串
                    StringBuilder builder = new StringBuilder();
                    String str = "";

                    br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    while((str = br.readLine())!=null)
                    {
                        builder.append(str);
                    }
                    return builder.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try{
                    if(br != null)
                    {
                        br.close();
                    }
                    if(httpURLConnection != null)
                    {
                        httpURLConnection.disconnect();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private String parseJson(String u)
        {
            StringBuilder builder = new StringBuilder();
            String ret = "";
            try {
                JSONObject object = new JSONObject(u);
                JSONArray array = object.getJSONArray("items");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject js = array.getJSONObject(i);
                    String content = js.getString("content"); // 解析对象里面的字符串，用getString();
                    builder.append(content).append("\n\n").append("---------------------").append("\n\n");
                }
                return builder.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally
            {

            }
            return null;
        }


    }



    }



