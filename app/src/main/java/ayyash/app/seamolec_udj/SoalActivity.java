package ayyash.app.seamolec_udj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Abdul Rizal Adompo on 24/09/2016.
 */
public class SoalActivity extends AppCompatActivity {

    TextView tv,tempatWaktu;
    RequestQueue requestQueue;
    JsonArrayRequest jsonArrayRequest;
    ProgressBar progressBar;
    LinearLayout mLinearLayout;



    List<ModelSoal> listSoal;

    String JSON_ID_SOAL = "id_soal";
    String JSON_QUESTION_TEXT = "question_text";
    String JSON_OPSI_SOAL = "opsi";

    String JSON_ANSWER_TEXT = "text";
    String JSON_ANSWER_STATUS = "status";

    private int id_quiz;
    private int answer_soal;
    private int answer_benar;
    private String ambilIP;


    //ulala
    public static final String KEY_ID_QUIZ = "id_quiz";
    public static final String KEY_NIS = "nis";
    public static final String KEY_NILAI = "nilai";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal);
        tv = (TextView)findViewById(R.id.textView8);
        tempatWaktu =(TextView)findViewById(R.id.textView10);
        Intent i = getIntent();
        id_quiz = i.getIntExtra("kirimanIDQuiz", 0);
        tv.setText("Menjawab 0/0 soal");
        tempatWaktu.setText("Waktu tersisa: ");

        SharedPreferences sps = getSharedPreferences("", MODE_PRIVATE);
        ambilIP = sps.getString("IPnya", "");


        loadListSoal();

        mLinearLayout = (LinearLayout) findViewById(R.id.mLinearLayout);

        String [] Soal = {"Berapa?", "Siapa?", "Kapan?", "Dimana?"};
        String[][] opsiSoal={
                {"1", "2", "3"} ,
                { "saya", "kamu", "dia", "mereka", "kita"},
                {"sekarang", "waktu itu"},
                {"disini", "disana"}
        } ;

////        int jml_soal = opsiSoal.length - 1;
////        for (int x =0; x<Soal.length; x++){
////            System.out.println("soal "+ Soal[x]);
////            for (int y=0; y < opsiSoal[x].length; y++){
////                System.out.println(opsiSoal[x][y]);
////            }
////        }
//
//        for (int k = 0; k < Soal.length; k++) {
//            //create text button
//
//            TextView tempatSoal = new TextView(this);
//            tempatSoal.setText(Soal[k]);
//            tempatSoal.setTextColor(Color.BLUE);
//            mLinearLayout.addView(tempatSoal);
//            // create radio button
//            final RadioButton[] rb = new RadioButton[6];
//            RadioGroup rg = new RadioGroup(this);
//            rg.setOrientation(RadioGroup.VERTICAL);
//            for (int l=0; l<opsiSoal[k].length; l++ ){
//                rb[l] = new RadioButton(this);
//                rg.addView(rb[l]);
//                rb[l].setText(opsiSoal[k][l]);
//            }
//
//            mLinearLayout.addView(rg);
//        }
    }

    private void loadListSoal() {
        listSoal = new ArrayList<ModelSoal>();
        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
       // progressBar.setVisibility(View.GONE);
        //buttonLoadPaket = (Button) findViewById(R.id.buttonLoadPaket);
        //recyclerView.setHasFixedSize(true);
        //recyclerViewlayoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(recyclerViewlayoutManager);
        /*buttonLoadPaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                JSON_DATA_WEB_CALL();
            }
        });*/
        JSON_DATA_WEB_CALL();
    }

    public void JSON_DATA_WEB_CALL() {
        int eco = id_quiz;
        //tv.setText("http://" + ambilIP + "/new_udj/list_soal.php?id_quiz=" + eco);
        jsonArrayRequest = new JsonArrayRequest("http://" + ambilIP + "/new_udj/list_soal.php?id_quiz=" + eco,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //tv.setText("load selesai");
                   //     progressBar.setVisibility(View.GONE);
                        //Log.d("DEBUG", response.toString());

                        //aktifkan
                        JSON_PARSE_DATA_AFTER_WEBCALL(response);

//                       try {
//                           for (int i = 0; i < response.length(); i++) {
//                               JSONObject row = response.getJSONObject(i);
//                               ambilIDKelas = row.getInt("id_kelas");
//                               ambilNamaQuiz = row.getString("nama_quiz");
//                           }
//                           Toast.makeText(getApplicationContext(),"ID Kelas : "+ambilIDKelas+ "Nama Quiz : " +ambilNamaQuiz, Toast.LENGTH_SHORT).show();
//                       } catch (JSONException e) {
//                           e.printStackTrace();
//                       }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array) {
        //tv.setText("parsing...");
        answer_soal = array.length();
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = null;
            ModelSoal mSoal = new ModelSoal();

            try {
                json = array.getJSONObject(i);

                //Log.d("DEBUG", json.getString(JSON_QUESTION_TEXT));
                mSoal.setId_soal(json.getInt(JSON_ID_SOAL));
                mSoal.setText(json.getString(JSON_QUESTION_TEXT));
                //busuk
                JSONArray arrayOpsi = json.getJSONArray(JSON_OPSI_SOAL);
                for (int j = 0; j < arrayOpsi.length(); j++) {
                    JSONObject jsonOpsi = arrayOpsi.getJSONObject(j);
                    ModelOpsiSoal mOpsiSoal = new ModelOpsiSoal();

                    mOpsiSoal.setText(jsonOpsi.getString(JSON_ANSWER_TEXT));
                    if (jsonOpsi.getString(JSON_ANSWER_STATUS).equals("1")){
                        mOpsiSoal.setStatus(true);
                    } else {
                        mOpsiSoal.setStatus(false);
                    }

                    mSoal.addOpsiSoal(mOpsiSoal);
                }


                listSoal.add(mSoal);
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        tv.setText("Menjawab 0/"+array.length()+" soal");

        loadTampilan();
    }

    private void loadTampilan() {
        //Log.d("DEBUG", "adding soal...");
        for (int k = 0; k < listSoal.size(); k++) {
            //Log.d("DEBUG", "soal ke-"+k);
            // get Model
            ModelSoal mSoal = listSoal.get(k);

            //create text button
            TextView tempatSoal = new TextView(this);
            tempatSoal.setText((k+1) + ". " + mSoal.getText());
            tempatSoal.setTextColor(Color.BLUE);
            mLinearLayout.addView(tempatSoal);

            // create radio button
            final int opsiSize = mSoal.getModelOpsiSoal().size();
            final RadioButton[] rb = new RadioButton[opsiSize];
            RadioGroup rg = new RadioGroup(this);
            rg.setOrientation(RadioGroup.VERTICAL);
            //Log.d("DEBUG", "jumlah opsi soal: "+opsiSize);
            for (int i1= 0; i1 < opsiSize; i1++) {
                //Log.d("DEBUG", "opsi soal ke-"+i1);
                ModelOpsiSoal mOpsiSoal = mSoal.getModelOpsiSoal().get(i1);
                rb[i1] = new RadioButton(this);
                rg.addView(rb[i1]);
                rb[i1].setText(mOpsiSoal.getText());
                rb[i1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // cek sesuatu
                        int child = mLinearLayout.getChildCount();
                        int nRadio = 0;
                        int nJawab = 0;
                        for (int i = 0; i < child; i++) {
                            if(mLinearLayout.getChildAt(i) instanceof RadioGroup){
                                nRadio++;
                                RadioGroup nrg = (RadioGroup) mLinearLayout.getChildAt(i);
                                if(nrg.getCheckedRadioButtonId() != -1){
                                    nJawab++;
                                }
                            }
                        }
                        tv.setText("Menjawab "+nJawab+"/"+nRadio+" soal");
                    }
                });
            }
            mLinearLayout.addView(rg);
        }


        Button btnSubmit = new Button(this);
        btnSubmit.setText("Submit Jawaban!");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pemanis belaka
                int child = mLinearLayout.getChildCount();
                int nRadio = 0;
                int nJawab = 0;
                for (int i = 0; i < child; i++) {
                    if(mLinearLayout.getChildAt(i) instanceof RadioGroup){
                        nRadio++;
                        RadioGroup nrg = (RadioGroup) mLinearLayout.getChildAt(i);

                        if(nrg.getCheckedRadioButtonId() != -1){
                            nJawab++;
                        }
                    }
                }

                if(nJawab != nRadio){
                    Toast.makeText(SoalActivity.this, "Soal belum semua terjawab", Toast.LENGTH_LONG).show();
                } else {

                    answer_benar = 0;
                    for (int i = 0; i < child; i++) {
                        if(mLinearLayout.getChildAt(i) instanceof RadioGroup){
                            RadioGroup nrg = (RadioGroup) mLinearLayout.getChildAt(i);
                            int radioButtonID = nrg.getCheckedRadioButtonId();
                            RadioButton radioButton = (RadioButton) findViewById(radioButtonID);
                            int idx = nrg.indexOfChild(radioButton);

                            ModelSoal tmSoal = listSoal.get(i / 2);
                            ModelOpsiSoal tmOpsiSoal = tmSoal.getModelOpsiSoal().get(idx);
                            if(tmOpsiSoal.isStatus()){
                                answer_benar++;
                            }
                        }
                    }
                    Log.d("RADIO_BUTTON", "Jawaban Benar: " + answer_benar);


                    simpanUye();

                }
            }
        });
        mLinearLayout.addView(btnSubmit);
    }

    public void simpanUye(){

        final String a = "aaaa";
        final String b = "AYYASH";
        final String c = "100000";







        StringRequest sR = new StringRequest(Request.Method.POST, "http://"+ambilIP+"/new_udj/simpanNilai.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(SoalActivity.this, response, Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(SoalActivity.this, Akhirnya.class);
                        intent.putExtra("answer_total", answer_soal);
                        intent.putExtra("answer_true", answer_benar);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SoalActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_ID_QUIZ, a.toString());
                params.put(KEY_NIS, b.toString());
                params.put(KEY_NILAI, c.toString());

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sR);


    }


}
