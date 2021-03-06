package fh_muenster.clubchamp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import fh_muenster.webservices.AQLClubChampWebServiceServiceSoapBinding;

/**
 * @author Carlo Eefting
 */
public class Musikwunsch extends AppCompatActivity {

    //private ArrayList<String> arrayList;
   // private ArrayAdapter<String> adapter;
    EditText lied;
    EditText inter;
    int r;
    SharedPreferences pref;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musikwunsch);
        lied = (EditText) findViewById(R.id.wunsch);
        inter = (EditText) findViewById(R.id.etInterpret);
        RatingBar ratB = (RatingBar) findViewById(R.id.rb);
        r = ratB.getNumStars();
        new RatingAsync().execute();

        //ListView wuensche = (ListView) findViewById(R.id.lvw);
        //String[] items = {"Test1","Test2","Test3"};
        //arrayList = new ArrayList<>(Arrays.asList(items));
       //adapter=new ArrayAdapter<String>(this, R.layout.activity_musikliste, R.id.txtv, arrayList);
        pref = getApplicationContext().getSharedPreferences("shared_preferences", 0);
        //wuensche.setAdapter(adapter);



        setupWunsch();
    }



    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menumain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1 :
                new LogoutAsync().execute();
                finish();

                break;

            case R.id.item3 :
                Toast.makeText(Musikwunsch.this,"Du bist bereits auf der Seite", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item4 :
                startActivity(new Intent (Musikwunsch.this, Musikliste.class));

                break;



        }
        return true;
    }
    /**
     *
     */
    public void setupWunsch() {
        Button wunsch = (Button) findViewById(R.id.button_wabgeben);


        View.OnClickListener myListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lied.getText().length() != 0 && lied.getText().toString() != "" && inter.getText().toString() != "" && inter.getText().length() != 0){
                    new WunschAsync().execute();

                }
                else{


                }

            }

        };
        wunsch.setOnClickListener(myListener);

    }

    /**
     * @author Carlo Eefting
     */

    class LogoutAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        /**
         *
         * @param strings
         * @return
         */

        @Override
        protected String doInBackground(String... strings) {
            try {
                AQLClubChampWebServiceServiceSoapBinding service = new AQLClubChampWebServiceServiceSoapBinding();
                try {
                    return service.logout(pref.getString("Session", null));

                } catch (Exception e) {
                    return " ";
                }
            }


            catch(Exception e){return " ";
            }
        }

        /**
         *
         * @param result
         */

        protected void onPostExecute(String result) {
            Log.i("LOG: ", result);
            if(result.equals(" ")){

                Toast.makeText(Musikwunsch.this,"Logged out", Toast.LENGTH_LONG).show();
            }
            else {
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(Musikwunsch.this,"Logged out", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Musikwunsch.this, MainActivity.class));
                finish();
            }
        }
    }

    /**
     * @author Carlo Eefting
     */
    class RatingAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        /**
         *
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                AQLClubChampWebServiceServiceSoapBinding service = new AQLClubChampWebServiceServiceSoapBinding();
                try {
                    service.clubBewerten(r,pref.getString("Session",null));


                } catch (Exception e) {
                    return null;
                }
            }


            catch(Exception e){
                return null;
            }
            return null;
        }


        protected void onPostExecute(Void result) {


        }
    }

    /**
     * @author Carlo Eefting
     */
    class WunschAsync extends AsyncTask<String, String, String> {

        private String l = lied.getText().toString();
        private String i = inter.getText().toString();
        @Override
        protected void onPreExecute() {

        }

        /**
         *
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            try {
                AQLClubChampWebServiceServiceSoapBinding service = new AQLClubChampWebServiceServiceSoapBinding();
                try {
                    return service.musikWuenschen(pref.getString("Session", null),l ,i);

                } catch (Exception e) {
                    return " ";
                }
            }


            catch(Exception e){return " ";
            }
        }

        /**
         *
         * @param result
         */
        protected void onPostExecute(String result) {
            Log.i("LOG: ", result);
            if(result.equals(" ")){

                Toast.makeText(Musikwunsch.this,"fehlgeschlagen", Toast.LENGTH_LONG).show();
            }
            else {


                Toast.makeText(Musikwunsch.this,result, Toast.LENGTH_LONG).show();
                lied.setText("");
                inter.setText("");


            }
        }
    }

}
