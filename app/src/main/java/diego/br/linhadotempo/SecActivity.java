package diego.br.linhadotempo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dpturibio on 10/11/15.
 */
public class SecActivity extends MainActivity {

    private String isFacebook;
    private LinearLayout Ll2, Ll4;
    private int arraySize;
    private String chosedData;
    private TimeLine[] timeLine;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        setFbstate(((String) extras.get("data")));
        setData(((String) extras.get("data")));

        setContentView(R.layout.activity_2);

        Ll2 = (LinearLayout) findViewById(R.id.Ll02);
        Ll4 = (LinearLayout) findViewById(R.id.Ll04);
        sendData();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private boolean isFb(){
        if (isFacebook.contentEquals("F")){
            return true;
        }
        return false;
    }
    private int getArraySize(){
        if(isFb() & (arraySize!=0) ){
            return 1;
        }
        return arraySize;
    }
    private void setArraySize(int t){
        arraySize=t;
    }

    public String getData(){
        return chosedData;
    }
    private void setData(String s){
        if(isFb()) {//caso venha um F no inicio da string, a mesma deve utilizar data do facebook
            chosedData = s.substring(1,11);//retira o F da string
        }else{
            chosedData = s;
        }
    }
   private void setFbstate(String s){
        isFacebook = s.substring(0, 1);
    }
    //-----------------------------------+
    //FUNÇÃO PARA POPULAR SEGUNDA JANELA |
    //-----------------------------------+
    public void popularSegundaJanela(final TimeLine[] timeLine) {

        Ll2 = (LinearLayout) findViewById(R.id.Ll02);
        Ll4 = (LinearLayout) findViewById(R.id.Ll04);

        GradientDrawable shape0 = new GradientDrawable();
        shape0.setStroke(1, Color.rgb(0, 0, 0)); //Borda de 2dip, cor definida em RGB
        shape0.setColor(Color.WHITE);
        Ll4.setBackground(shape0);
        for (int i = 0; i < getArraySize(); i++) {
            final TextView txtTitulo = new TextView(this);
            final TextView txtvw2 = new TextView(this);
            final TextView txtdata = new TextView(this);
            TextView.OnClickListener txtClickLnr = new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dados;
                    int j = v.getId();
                    dados = "#id#" + timeLine[j].getId() +
                            "#title#" + timeLine[j].getTitle() +
                            "#data#" + timeLine[j].getDate() +
                            "#description#" + timeLine[j].getDescription() +
                            "#updated#" + timeLine[j].getUpdated_at() +
                            "#avatarlarge#" + timeLine[j].getAvatar_url_large() +
                            "#avatarthumb#" + timeLine[j].getAvatar_url_thumb() +
                            "#userinfo#" + timeLine[j].getUser_info_author();

                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), TercActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    i.putExtra("dados", dados);
                    startActivityForResult(i, 1);
                }
            };
            txtTitulo.setOnClickListener(txtClickLnr);
            txtvw2.setOnClickListener(txtClickLnr);
            txtdata.setOnClickListener(txtClickLnr);
            ImageView imgThumb = buscaImagem(timeLine[i].getAvatar_url_thumb());

            imgThumb.setOnClickListener(txtClickLnr);

            if (isFb()){//adicionar texto explicativo para facebook
                LinearLayout Ll2_1 = new LinearLayout(this);
                Ll2_1.setOrientation(LinearLayout.HORIZONTAL);
                GradientDrawable shape = new GradientDrawable();
                shape.setColor(Color.WHITE);
                Ll2_1.setBackground(shape);
                Ll2.addView(Ll2_1);//adiciona um linearLayout horizontal dentro do vertical
                TextView txtFb = new TextView(this);
                txtTitulo.setTextColor(Color.rgb(0, 0, 150));
                txtFb.setText("Evento científico mais próximo, anterior a sua data de nascimento: " + getData() + "\nClique para obter mais detalhes.\n");
                txtFb.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                Ll2_1.addView(txtFb);
            }

            LinearLayout Ll2_1 = new LinearLayout(this);
            Ll2_1.setOrientation(LinearLayout.HORIZONTAL);

            GradientDrawable shape = new GradientDrawable();//cria um novo shape para alterar a cor
            if (i % 2 == 0) { //varia as cores dos linearLayout horizontais
                shape.setColor(Color.parseColor("#1ABC9C"));
            } else {
                shape.setColor(Color.WHITE);
            }
            shape.setStroke(1, Color.rgb(0, 0, 0)); //Borda de 1dip, cor definida em RGB
            Ll2_1.setBackground(shape);
            Ll2.addView(Ll2_1);//adiciona um linearLayout horizontal dentro do vertical

            imgThumb.setScaleX((float) 0.8);
            imgThumb.setScaleY((float) 0.8);
            imgThumb.setAdjustViewBounds(true);
            imgThumb.setCropToPadding(true);
            imgThumb.setMaxWidth(100);
            imgThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgThumb.setId(i);
            Ll2_1.addView(imgThumb);//adiciona imagem no layout horizontal
            LinearLayout Ll2_2 = new LinearLayout(this);
            Ll2_2.setOrientation(LinearLayout.VERTICAL);
            Ll2_1.addView(Ll2_2);

            txtTitulo.setText("\n" + timeLine[i].getTitle());
            txtTitulo.setTextColor(Color.rgb(0, 0, 0));
            txtTitulo.setTypeface(Typeface.DEFAULT_BOLD);
            txtTitulo.setAllCaps(true);
            txtTitulo.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
            txtTitulo.setId(i);
            if(timeLine[i].getDescription().length() >101){
                txtvw2.setText(timeLine[i].getDescription().substring(0, 100) + "...");
            }else{
                txtvw2.setText(timeLine[i].getDescription());
            }
            txtvw2.setClickable(true);
            txtvw2.setId(i);
            txtdata.setText(timeLine[i].getDate() + "\n");
            txtdata.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
            txtdata.setId(i);
            Ll2_2.addView(txtTitulo);
            Ll2_2.addView(txtvw2);
            Ll2_2.addView(txtdata);

        }
    }

    //--------------------------------------------------------------------------------+
    //BOTAO inicial(Processamento de aquisição de dados e população de dados em array |
    //--------------------------------------------------------------------------------+
    public void sendData() {



        AcessarURL url = new AcessarURL();
        url.AcessarUrl("http://eic.ifsc.usp.br:3001/search/" + getData() + ".json");
        System.out.println(url.ConteudoUrl());
        //TimeLine[] timeLine = new TimeLine[10];//vetor de 10 posiçoes das requisiçoes json
        if (timeLine == null) {
            System.out.println("######### Instanciando timeline pela primeira vez #############");
            timeLine = new TimeLine[10];//vetor de 10 posiçoes das requisiçoes json
            for (int i = 0; i < 10; i++) {//inicializa o vetor
                timeLine[i] = new TimeLine();
            }
        }

        TrataJson jsonresult = new TrataJson();//cria objeto que será populado
        jsonresult.getCampoValue(url.ConteudoUrl(), timeLine);//metodo que acessa json e popula vetor timeLine[10]

        if(getArraySize() == 0) {//se nao encontrar nada para data posterior, tentar anterior
            url.AcessarUrl("http://eic.ifsc.usp.br:3001/search/" + getData() + ".json?after=true");
            jsonresult.getCampoValue(url.ConteudoUrl(), timeLine);//metodo que acessa json e popula vetor timeLine[10]
        }
            if (getArraySize() == 0) {
                alertdialoginvDate();
                TextView txtWarn = new TextView(this);
                txtWarn.setText("Nenhum registro posterior à data selecionada. Retorne e escolha outra data.");
                txtWarn.setAllCaps(true);
                txtWarn.setGravity(View.TEXT_ALIGNMENT_CENTER);
                Ll2.addView(txtWarn);
            } else {
                popularSegundaJanela(timeLine);
            }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Sec Page", // TODO: Define a title for the content shown.
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
                "Sec Page", // TODO: Define a title for the content shown.
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

    //-----------------+
    //CLASSE TRATA JSON |
    //------------------+
    public class TrataJson {
        public void getCampoValue(String jstring, TimeLine[] timeLine) {
            try {
                jstring = "{\"data\":" + jstring + "}";
                System.out.println(jstring);
                JSONObject jsonObject = new JSONObject(jstring);
                JSONObject sP;//pegar subparametro
                JSONArray results = jsonObject.getJSONArray("data");
                setArraySize(results.length());
                System.out.printf("STEP Arraysize %d\n", getArraySize());
                for (int i = 0; i < results.length(); i++) {
                    JSONObject f = results.getJSONObject(i);
                    timeLine[i].setId(f.getString("id"));
                    timeLine[i].setTitle(f.getString("title"));
                    timeLine[i].setDate(f.getString("date"));
                    timeLine[i].setDescription(f.getString("description"));
                    timeLine[i].setUpdated_at(f.getString("updated_at"));
                    timeLine[i].setAvatar_url_large(f.getString("avatar_url_large"));
                    timeLine[i].setAvatar_url_thumb(f.getString("avatar_url_thumb"));
                    timeLine[i].setUser_info_author(f.getString("user_info"));//subchave user_info
                    sP = new JSONObject(timeLine[i].getUser_info_author());//user_info contem email,nome,etc
                    timeLine[i].setUser_info_author(sP.getString("name"));//pegando apenas o nome do author
                    System.out.println(timeLine[i].getId());
                    System.out.println(timeLine[i].getTitle());
                    System.out.println(timeLine[i].getDate());
                    System.out.println(timeLine[i].getDescription());
                    System.out.println(timeLine[i].getUpdated_at());
                    System.out.println(timeLine[i].getAvatar_url_large());
                    System.out.println(timeLine[i].getAvatar_url_thumb());
                    System.out.println(timeLine[i].getUser_info_author());
                }
            } catch (JSONException e) {
                System.out.println(e);
            }
        }
    }


}
