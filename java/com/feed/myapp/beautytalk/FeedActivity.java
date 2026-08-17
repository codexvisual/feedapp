package com.beautytalk.lannet.beautytalk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beautytalk.lannet.beautytalk.adapters.DataObject;
import com.beautytalk.lannet.beautytalk.adapters.DividerItemDecoration;
import com.beautytalk.lannet.beautytalk.adapters.FeedAdapter;
import com.beautytalk.lannet.beautytalk.adapters.ItemObjects;
import com.beautytalk.lannet.beautytalk.adapters.SolventRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    SharedPreferences mpreferences;
    SharedPreferences.Editor settingDataPrefe;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "Gett Message";
    ArrayList<DataObject> results;
    String uidStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);


        mpreferences = getSharedPreferences(String.format("%s_preferences", getPackageName()), Context.MODE_PRIVATE);
        settingDataPrefe = mpreferences.edit();
        uidStr=mpreferences.getString("id","0");
        mRecyclerView = (RecyclerView) findViewById(R.id.feedlist);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

       new ImageGET().execute();
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    /*private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 20; index++) {
            DataObject obj = new DataObject(" Nirbhay " + index,
                    "Comments  " + index,"img");
            results.add(index, obj);
        }
        return results;
    }*/


    class ImageGET extends AsyncTask<String, String, String> {

        String respo;
        ProgressDialog pd;
        String mobNo;



        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(FeedActivity.this);
            pd.setMessage("Please wait...");
            pd.show();
        }


        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub +"state_id="+citytem;



            String url =AppController.baseURL+AppController.getImageList+"uid="+uidStr+"";
            try {
                respo = CustomHttpClient.urlincoding(url);
                // jRespons = CustomHttpClient.executeHttpGet(url);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return respo;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try{


                JSONObject jObj=new JSONObject(s);
                JSONObject jRes=jObj.getJSONObject("response");
                String stus=jRes.getString("status");

                if(stus.equals("1")) {

                    JSONArray jArrObject = jRes.getJSONArray("data");
                    results = new ArrayList<DataObject>();
                    for (int i = 0; i < jArrObject.length(); i++) {
                        JSONObject jIndexObj = jArrObject.getJSONObject(i);

                      /*  imgList1.add(jIndexObj.getString("img1"));
                        String attribFace=jIndexObj.getString("faceAttri");
                        String landmrks=jIndexObj.getString("landmarks");
                        faceAttrArr1.add(attribFace);
                        landmarksArr1.add(landmrks);*/
                        //   imgList2.add(jIndexObj.getString("img2"));

                       /* DataObject obj = new DataObject(
                                jIndexObj.getString("Take_Date"),jIndexObj.getString("id"),jIndexObj.getString("img1")
                        );*/
                   //     results.add(i, obj);

                    }
                    mAdapter = new FeedAdapter(results,FeedActivity.this);
                    mRecyclerView.setAdapter(mAdapter);

                 /*   myCustomPagerAdapter = new MyCustomPagerAdapter(GalleryActivity.this, imgList1);
                    viewPager.setAdapter(myCustomPagerAdapter);
*/
                    // new ImageGET1().execute();
                }else {


                }

                // CreateDB();,
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


}