package com.beautytalk.lannet.beautytalk;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.beautytalk.lannet.beautytalk.adapters.ItemObjects;
import com.beautytalk.lannet.beautytalk.adapters.MyCustomPagerAdapter;
import com.beautytalk.lannet.beautytalk.adapters.SearchRecyclerViewAdapter;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;


import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.CAMERA;

public class SearchGalleryActivity extends AppCompatActivity {

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1;

    Bitmap myBitmap;

    TextView ageTxt,glassesTxt,sex,eyedis,lip,facew,faceh,eyeleftx,eyelefty,eyerightx,eyerighty;
    TextView ageTxt1,glassesTxt1,sex1,eyedis1,lip1,facew1,faceh1,eyeleftx1,eyelefty1,eyerightx1,eyerighty1;
    int flgGet=1;
    ArrayList<String> imgList1=new ArrayList<String>();
    ArrayList<String> imgList2=new ArrayList<String>();
    ArrayList<String> landmarksArr1=new ArrayList<String>();
    ArrayList<String> faceAttrArr1=new ArrayList<String>();

    ArrayList<String> landmarksArr2=new ArrayList<String>();
    ArrayList<String> faceAttrArr2=new ArrayList<String>();

    ViewPager viewPager;
    ViewPager viewPager1;
    int images[] = {R.mipmap.pic1,R.mipmap.pic2,R.mipmap.pic3,R.mipmap.pic4};
    MyCustomPagerAdapter myCustomPagerAdapter;
    MyCustomPagerAdapter1 myCustomPagerAdapter1;
    FaceDetector detector;
    Bitmap imageBitmap;
   // ImageView imageView;
    BootstrapThumbnail imageView;
    Uri contentURI;
    String imgPath1,imgPath2;
    FaceDetector detector1;
    Bitmap imageBitmap1;
   // ImageView imageView1;
    BootstrapThumbnail imageView1;
    TextView facecmp;
    BootstrapButton upload1,upload2;
    ArrayList<ItemObjects> results;
    RecyclerView recyclerView;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    int rnd=0;
    ImageView takepic;
    SharedPreferences mpreferences;
    SharedPreferences.Editor settingDataPrefe;
    String uidStr;
    SearchRecyclerViewAdapter rcAdapter;
    public Calendar myCalendar;
    EditText picdate,picdate2;
    RelativeLayout picdate1;
    String startingDateSelected="";

    ImageView drawericon;
    Date datestart;


    private int mYear;
    private int mMonth;
    private int mDay;

    static final int DATE_DIALOG_ID = 0;
    Typeface fontCustome;
    TextView to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);
        AndroidNetworking.initialize(getApplicationContext());
        mpreferences = getSharedPreferences(String.format("%s_preferences", getPackageName()), Context.MODE_PRIVATE);
        settingDataPrefe = mpreferences.edit();
        uidStr=mpreferences.getString("id","0");
        takepic=(ImageView)findViewById(R.id.takepic);
        drawericon=(ImageView)findViewById(R.id.drawericon);
        drawericon.setVisibility(View.VISIBLE);
        drawericon.setImageResource(R.drawable.bck);
        to=(TextView)findViewById(R.id.to);
        fontCustome= Typeface.createFromAsset(getAssets(),"Raleway_Regular.ttf");
        to.setTypeface(fontCustome);
        results = new ArrayList<ItemObjects>();
        drawericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });



        LinearLayout profile =(LinearLayout)findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchGalleryActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

       // Search = (EditText)findViewById(R.id.Search);
         picdate1= (RelativeLayout)findViewById(R.id.picdate1);
        picdate= (EditText)findViewById(R.id.picdate);
        picdate2= (EditText)findViewById(R.id.picdate2);
        try {
            myCalendar = Calendar.getInstance();
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                Toast.makeText(SearchGalleryActivity.this,""+year+"-"+monthOfYear+"-"+dayOfMonth,Toast.LENGTH_LONG).show();
            }

        };


        picdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                results.clear();
                imgList1.clear();
                faceAttrArr1.clear();
                landmarksArr1.clear();
                picdate.setTextSize(12);
                new DatePickerDialog(SearchGalleryActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),

                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        picdate2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                results.clear();
                imgList1.clear();
                faceAttrArr1.clear();
                landmarksArr1.clear();
                picdate2.setTextSize(12);
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchGalleryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                picdate2.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                new ImageGET(picdate.getText().toString(),picdate2.getText().toString()).execute();
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });



        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);



        permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //Toast.makeText(getActivity(),"has perm 0",Toast.LENGTH_LONG).show();
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            // Toast.makeText(getActivity(),"has perm 0",Toast.LENGTH_LONG).show();
        }


      // new ImageGETList().execute();


    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateLabel() {
        boolean checkDt = false;
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        picdate.setText(sdf.format(myCalendar.getTime()));
        results.clear();
        imgList1.clear();
        faceAttrArr1.clear();
        landmarksArr1.clear();
        try {
            rcAdapter.notifyDataSetChanged();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        new ImageGET(picdate.getText().toString(),picdate2.getText().toString()).execute();

        startingDateSelected=sdf.format(myCalendar.getTime());

    }

    // updates the date in the TextView
    @SuppressLint("StringFormatInvalid")
    private void updateDisplay() {
        picdate.setText(getString(R.string.strSelectedDate,
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" ")));
    }

    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
        }
        return null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    myBitmap = getResizedBitmap(bitmap, 500);
                    String path = saveImage(bitmap);
                    uploadBitmap(myBitmap);
                    //  Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    //   imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    /// Toast.makeText(SectionActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == 2) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            myBitmap = getResizedBitmap(thumbnail, 500);
            //imageview.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            uploadBitmap(myBitmap);
            //Toast.makeText(SectionActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }









       /* if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                contentURI= data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    myBitmap = getResizedBitmap(bitmap, 500);
                    String path = saveImage(bitmap);
                    uploadBitmap(myBitmap);
                    //  Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    //   imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    /// Toast.makeText(SectionActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == 2) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            myBitmap = getResizedBitmap(thumbnail, 500);
            //imageview.setImageBitmap(thumbnail);
            saveImage(thumbnail);
          //  uploadBitmap(myBitmap);
            //Toast.makeText(SectionActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }*/

    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void uploadBitmap(final Bitmap bitmap) {



        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Toast.makeText(SearchGalleryActivity.this, "" + response, Toast.LENGTH_LONG).show();

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchGalleryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
            * If you want to add more parameters with the image
            * you can do it here
            * here we have only one parameter with the image
            * which is tags
            * */


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("USER_MOBILE", flgGet+"");
                params.put("dt", ""+rnd);
                params.put("uid", ""+uidStr);
                return params;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("photoimg", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

// permission Code here


    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (SearchGalleryActivity.this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);

            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (hasPermission(perms)) {
                        // Toast.makeText(getActivity(),"has perm 1",Toast.LENGTH_LONG).show();
                    } else {
                        // Toast.makeText(getActivity(),"has perm 0",Toast.LENGTH_LONG).show();
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(SearchGalleryActivity.this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Take picture from camera"
               };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();

                                break;
                            case 1:
                                takePhotoFromCamera();

                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(SearchGalleryActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private void detectFaces() {
        Bitmap bmp = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), imageBitmap.getConfig());
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(imageBitmap, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        Paint landmarkPaint = new Paint();
        landmarkPaint.setColor(Color.RED);
        landmarkPaint.setStyle(Paint.Style.STROKE);
        landmarkPaint.setStrokeWidth(1);


        Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
        SparseArray<Face> faces = detector.detect(frame);

        if(faces.size() > 0){
            for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);

                canvas.drawRect(
                        face.getPosition().x,
                        face.getPosition().y,
                        face.getPosition().x + face.getWidth(),
                        face.getPosition().y + face.getHeight(), paint);

                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x);
                    int cy = (int) (landmark.getPosition().y);
                    canvas.drawCircle(cx, cy, 1, landmarkPaint);

                }
            }

            imageView.setImageBitmap(bmp);
           // Toast.makeText(this, faces.size() + " faces detected", Toast.LENGTH_LONG).show();
            Toast.makeText(this, " faces detected", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "No faces detected", Toast.LENGTH_LONG).show();
        }

    }


    private void detectFaces1() {
        Bitmap bmp = Bitmap.createBitmap(imageBitmap1.getWidth(), imageBitmap1.getHeight(), imageBitmap1.getConfig());
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(imageBitmap1, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        Paint landmarkPaint = new Paint();
        landmarkPaint.setColor(Color.RED);
        landmarkPaint.setStyle(Paint.Style.STROKE);
        landmarkPaint.setStrokeWidth(1);


        Frame frame = new Frame.Builder().setBitmap(imageBitmap1).build();
        SparseArray<Face> faces = detector1.detect(frame);
        if(faces.size() > 0){
            for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);

                canvas.drawRect(
                        face.getPosition().x,
                        face.getPosition().y,
                        face.getPosition().x + face.getWidth(),
                        face.getPosition().y + face.getHeight(), paint);

                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x);
                    int cy = (int) (landmark.getPosition().y);
                    canvas.drawCircle(cx, cy, 1, landmarkPaint);
                }
            }

            imageView1.setImageBitmap(bmp);
            // Toast.makeText(this, faces.size() + " faces detected", Toast.LENGTH_LONG).show();
            Toast.makeText(this, " faces detected", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "No faces detected", Toast.LENGTH_LONG).show();
        }
    }


    class ImageGET extends AsyncTask<String, String, String> {

        String respo;
        ProgressDialog pd;
        String mobNo;

        String DateLocal;
        String DateLocal1;

        ImageGET(String dtStr,String dtStr1)
        {

            DateLocal=dtStr;
            DateLocal1=dtStr1;
            results.clear();
            imgList1.clear();
            faceAttrArr1.clear();
            landmarksArr1.clear();
        }
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(SearchGalleryActivity.this);
            pd.setMessage("Please wait...");
            pd.show();
        }


        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub +"state_id="+citytem;



            String url =AppController.baseURL+AppController.getSearchebleImage+"uid="+uidStr+"&SelectedDate="+DateLocal+"&SelectedDate2="+DateLocal1+"";
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

                results.clear();
                imgList1.clear();
                faceAttrArr1.clear();
                landmarksArr1.clear();
                JSONObject jObj=new JSONObject(s);
                JSONObject jRes=jObj.getJSONObject("response");
                String stus=jRes.getString("status");

                if(stus.equals("1")) {

                    JSONArray jArrObject = jRes.getJSONArray("data");
                   // results = new ArrayList<ItemObjects>();
                    for (int i = 0; i < jArrObject.length(); i++) {
                        JSONObject jIndexObj = jArrObject.getJSONObject(i);

                        imgList1.add(jIndexObj.getString("img1"));
                        String attribFace=jIndexObj.getString("faceAttri");
                        String landmrks=jIndexObj.getString("landmarks");
                        faceAttrArr1.add(attribFace);
                        landmarksArr1.add(landmrks);
                     //   imgList2.add(jIndexObj.getString("img2"));

                        ItemObjects obj = new ItemObjects(jIndexObj.getString("img1"),
                                jIndexObj.getString("Take_Date"),jIndexObj.getString("id")
                               );
                        results.add(i, obj);

                    }
                     rcAdapter = new SearchRecyclerViewAdapter(SearchGalleryActivity.this, results,imgList1);
                    rcAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(rcAdapter);


                }else {


                }

                // CreateDB();,
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }








    class ImageGETList extends AsyncTask<String, String, String> {

        String respo;
        ProgressDialog pd;
        String mobNo;



        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(SearchGalleryActivity.this);
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

                    for (int i = 0; i < jArrObject.length(); i++) {
                        JSONObject jIndexObj = jArrObject.getJSONObject(i);

                        imgList1.add(jIndexObj.getString("img1"));
                        String attribFace=jIndexObj.getString("faceAttri");
                        String landmrks=jIndexObj.getString("landmarks");
                        faceAttrArr1.add(attribFace);
                        landmarksArr1.add(landmrks);
                        //   imgList2.add(jIndexObj.getString("img2"));

                        ItemObjects obj = new ItemObjects(jIndexObj.getString("img1"),
                                jIndexObj.getString("Take_Date"),jIndexObj.getString("id")
                        );
                        results.add(i, obj);

                    }
                    SearchRecyclerViewAdapter rcAdapter = new SearchRecyclerViewAdapter(SearchGalleryActivity.this, results,imgList1);
                    recyclerView.setAdapter(rcAdapter);

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
