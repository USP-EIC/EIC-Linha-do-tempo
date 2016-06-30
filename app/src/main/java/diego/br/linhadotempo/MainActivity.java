package diego.br.linhadotempo;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by dpturibio on 29/09/15.
 */
public class MainActivity extends AppCompatActivity {

    private Boolean logado=false;
    private TextView txtdate;
    private Button btn_go;
    private Button btnFacebook;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private String birthdayfb;
    private int day;
    private int month;
    private int year;
    private int arraySize;
    private TimeLine[] timeLine;

    Calendar calendario = Calendar.getInstance();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        facebookSDKInitialize();
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        callbackManager = CallbackManager.Factory.create();
        btnFacebook = (Button) findViewById(R.id.btn_testefb);

        updateWithToken(AccessToken.getCurrentAccessToken());
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        txtdate = (TextView) findViewById(R.id.txt_date);
        txtdate.setText("Selecionar Data");
        btn_go = (Button) findViewById(R.id.btn1);
        btn_go.setEnabled(false);
        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        MainActivity.this,
                        listener,
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    protected void OnDestroy(){
        super.onDestroy();
        loginManager.getInstance().logOut();
    }
    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            txtdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            btn_go.setEnabled(true);
            btn_go.setBackgroundResource(R.drawable.screen_border);
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
        txtdate.setText("Selecionar Data");
        btn_go.setEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    private Bundle getFacebookData(JSONObject object) throws JSONException, MalformedURLException {

            Bundle bundle = new Bundle();
            String id = object.getString("id");


            URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
            Log.i("profile_pic", profile_pic + "");
            bundle.putString("profile_pic", profile_pic.toString());

                bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            birthdayfb = bundle.getString("birthday");
            return bundle;
    }

    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            System.out.println("esta logado <> nulo");
            logado=true;

        } else {
            System.out.println("nao ta logado == nulo");
            logado=false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && resultCode == RESULT_OK){
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
            Log.e("data", data.toString());
        }

    }

    //--------------------------------------------------------------------------------+
    //BOTAO inicial(Processamento de aquisição de dados e população de dados em array |
    //--------------------------------------------------------------------------------+
    public void sendData(View v) {
        Intent i = new Intent();
        i.setClass(this, SecActivity.class);
        i.putExtra("data", txtdate.getText());
        //btn_go.setEnabled(false);
        txtdate.setText("Aguarde...");
        startActivity(i);
    }

    //--------------------------------------+
    //FUNÇÃO PARA BUSCAR IMAGEM NO SERVIDOR |
    //--------------------------------------+
    public ImageView buscaImagem(String urlSrv) {
        ImageView imgThumb = new ImageView(this);
        try {//acessa servidor para buscar imagem
            URL url1 = new URL(urlSrv);
            HttpURLConnection conexao = (HttpURLConnection) url1.openConnection();
            conexao.setConnectTimeout(10000);
            conexao.setReadTimeout(10000);
            conexao.setRequestMethod("GET");
            conexao.setDoInput(true);
            conexao.connect();
            InputStream is = conexao.getInputStream();
            Bitmap img = BitmapFactory.decodeStream(is);
            imgThumb.setImageBitmap(img);

            conexao.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgThumb;
    }

    //--------------------------+
    //BOTAO data primeira JANELA|
    //--------------------------+
    public void btnVoltarInicio(View v) {
        setContentView(R.layout.activity_main);
        txtdate.setClickable(true);
        txtdate = (TextView) findViewById(R.id.txt_date);
        btn_go = (Button) findViewById(R.id.btn1);

        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        MainActivity.this,
                        listener,
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    //------------------------------+
    //BOTAO CREDITOS JANELA creditos|
    //------------------------------+
    public void btnCreditos(View v) {
        //setContentView(R.layout.creditos);
        //mostrarJanelaCreditos(timeLine);

        Intent k = new Intent();
        k.setClass(this, CreditosActivity.class);
        startActivity(k);
    }

    public void botaosairfunc(View v) {

        loginManager.getInstance().logOut();
        System.exit(0);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://diego.br.linhadotempo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://diego.br.linhadotempo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //------------------+
    //CLASSE TIMELINE   |
    //------------------+
    public class TimeLine {


        private final String server = "http://143.107.230.59";
        private String id;
        private String title;
        private String date;
        private String description;
        private String updated_at;
        private String avatar_url_large;
        private String avatar_url_thumb;
        private String user_info_author;

        //Set Methods
        public void setId(String a) {
            id = a;
        }

        public void setTitle(String a) {
            title = a;
        }

        public void setDate(String a) {
            date = a.substring(8, 10) + "/" + a.substring(5, 7) + "/" + a.substring(0, 4);
        }

        public void setDate2(String a) {
            date = a;
        }

        public void setDescription(String a) {
            description = a;
        }

        public void setUpdated_at(String a) {
            updated_at = a.substring(8, 10) + "/" + a.substring(5, 7) + "/" + a.substring(0, 4) + " @ " + a.substring(11, 16);
        }

        public void setUpdated_at2(String a) {
            updated_at = a;
        }

        public void setAvatar_url_large(String a) {
            avatar_url_large = server + a;
        }

        public void setAvatar_url_large2(String a) {
            avatar_url_large = a;
        }

        public void setAvatar_url_thumb(String a) {
            avatar_url_thumb = server + a;
        }

        public void setAvatar_url_thumb2(String a) {
            avatar_url_thumb = a;
        }

        public void setUser_info_author(String a) {
            user_info_author = a;
        }

        //Get Methods
        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getAvatar_url_large() {
            return avatar_url_large;
        }

        public String getAvatar_url_thumb() {
            return avatar_url_thumb;
        }

        public String getUser_info_author() {
            return user_info_author;
        }
    }

    //-------------------+
    //CLASSE ACESSAR URL |
    //-------------------+
    public class AcessarURL {

        private String UrlContent;

        public void AcessarUrl(String args) {

            if (args.equals("")) {
                System.out.println("Não foi especificado nenhuma URL.");
                // Fechando aplicação.
                System.exit(1);
            }

            // Pegando a url passada como parametro.
            String urlName = args;
            System.out.println(urlName);

            try {

                URL url = new URL(urlName);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line = null;
                limpaVariavel();
                while ((line = in.readLine()) != null) {
                    if (line != null) concatena(line);
                }
                in.close();
                urlConnection.disconnect();

            } catch (MalformedURLException e) {
                System.out.println("Erro ao criar URL. Formato inválido.");
                System.exit(1);
            } catch (IOException e2) {
                System.out.println("Erro ao acessar URL:" + e2.getMessage() + ", " + e2.getCause());
                System.exit(1);
            }

        }

        public void concatena(String a) {
            this.UrlContent += a;
        }

        public void limpaVariavel() {
            this.UrlContent = "";
        }

        public String ConteudoUrl() {
            return UrlContent;
        }
    }
    //---------------------+
    //Alerta de nao existe |
    //---------------------+

    public void alertdialoginvDate() {
        AlertDialog AlertDialog;
        AlertDialog = new AlertDialog.Builder(this).create();
        AlertDialog.setTitle("Data Inválida");
        AlertDialog.setMessage("Não existem registros para esta data!");
        AlertDialog.show();
    }

    public void Logar_fb(View view){
        Collection<String> permicoes = Arrays.asList("user_birthday");
        gerenciador_Login();
        loginManager.getInstance().logInWithReadPermissions(this, permicoes);
        loginManager = LoginManager.getInstance();
    }

    public void gerenciador_Login() {

        loginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        login_result.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                Log.v("LoginActivity", response.toString());
                                try {
                                    Bundle bFacebookData = getFacebookData(object);
                                    birthdayfb = birthdayfb.substring(3,5)+"-"+birthdayfb.substring(0,2)+"-"+birthdayfb.substring(6,10);
                                    Intent i = new Intent();
                                    i.setClass(getApplicationContext(), SecActivity.class);
                                    i.putExtra("data", "F" + birthdayfb);//adiciona F ao inicio da data para saber que é proveniente do facebook
                                    txtdate.setText("Aguarde...");
                                    startActivity(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "birthday");
                request.setParameters(parameters);
                request.executeAsync();

                System.out.println("User ID: " + login_result.getAccessToken().getUserId() + "\n" +
                        "Auth Token: " + login_result.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                // App code
                Log.e("dd", "facebook login canceled");
                System.out.println("Facebook login canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("dd", "facebook login failed error");
                System.out.println("ERRO: " + exception);
            }
        });
    }
}

