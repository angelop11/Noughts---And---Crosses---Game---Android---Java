package com.exampleee.angello.michi;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import android.app.Activity;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;

import com.exampleee.angello.michi.Publicidad;


public class MainActivity extends Activity {

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-4026121602556875~3547909068");

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        //Iniciamo el array casillas que identifica cada casilla y la almacena en el array

        CASILLAS = new int[9];

        CASILLAS[0]=R.id.a1;

        CASILLAS[1]=R.id.a2;

        CASILLAS[2]=R.id.a3;

        CASILLAS[3]=R.id.b1;

        CASILLAS[4]=R.id.b2;

        CASILLAS[5]=R.id.b3;

        CASILLAS[6]=R.id.c1;

        CASILLAS[7]=R.id.c2;

        CASILLAS[8]=R.id.c3;

    }

    public void aJugar(View vista){

        ImageView imagen;

        for(int cadaCasilla:CASILLAS){

            imagen=(ImageView)findViewById(cadaCasilla);

            imagen.setImageResource(R.drawable.casilla);

        }

        jugadores=1;

        RadioGroup configDificultad = (RadioGroup)findViewById(R.id.configD);

        int id= configDificultad.getCheckedRadioButtonId();

        int dificultad = 0;

        if(id==R.id.normal){

            dificultad=1;

        }else if(id==R.id.imposible){
            dificultad=2;
        }

        partida = new Partida(dificultad);

        ((Button)findViewById(R.id.unJug)).setEnabled(false);

        ((RadioGroup)findViewById(R.id.configD)).setAlpha(0);



    }

    public void toque(View mivista){
        //mivista es la variable de las casilla pulsadas

        if(partida==null){
            return;
        }
        //solo lo establece en 0 pero despues le asigna un valor
        //dependiendo de la casilla presionada
        int casilla=0;

        for(int i = 0; i<9;i++){

            if(mivista.getId() == CASILLAS[i]){

                casilla=i;

                break;
            }

        }

        /*Toast toast=Toast.makeText(this, "Has pulsado la tecla "+ casilla, Toast.LENGTH_LONG);

        toast.setGravity(Gravity.CENTER, 0 , 0);

        toast.show();*/

        if(partida.comprueba_casilla(casilla)==false){

            return;
        }

        marca(casilla);

        int resultado=partida.turno();

        if(resultado>0){

            termina(resultado);
            return;
        }

        casilla=partida.ia();

        while(partida.comprueba_casilla(casilla)!=true){


            casilla=partida.ia();

        }



        marca(casilla);

        resultado=partida.turno();

        if(resultado>0){

            termina(resultado);
        }

    }

    private void termina(int resultado){

        String mensaje;

        if(resultado==1){

            mensaje="Ganan los circulos";

        }else if(resultado==2){

            mensaje ="Ganan las aspas";

        }else{

            mensaje="Empate";

        }

        Toast toast = Toast.makeText(this, mensaje,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();

        showInterstitial();

        partida=null;

        ((Button)findViewById(R.id.unJug)).setEnabled(true);
        ((RadioGroup)findViewById(R.id.configD)).setAlpha(1);


    }




    private void marca(int casilla){

        ImageView imagen;

        imagen = (ImageView)findViewById(CASILLAS[casilla]);

        if(partida.jugador==1){

            imagen.setImageResource(R.drawable.circulo);
        }else{

            imagen.setImageResource(R.drawable.aspa);
        }


    }

    private int jugadores;

    private int[] CASILLAS;

    private Partida partida;

    //funciones publicidad-------------------------------------------------------------------------------------

    private static final String TOAST_TEXT = "Test ads are being shown. "
            + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";

    private static final int START_LEVEL = 1;
    private int mLevel;
    private Button mNextLevelButton;
    private TextView mLevelTextView;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publicidad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        /*interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mNextLevelButton.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mNextLevelButton.setEnabled(true);
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                goToNextLevel();
            }
        });*/
        return interstitialAd;
    }

    public void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Error conexion", Toast.LENGTH_SHORT).show();
            goToNextLevel();
        }
    }

    public void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }


}
