package diego.br.linhadotempo;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by dpturibio on 10/11/15.
 */
public class TercActivity extends MainActivity  {
    private AccessToken accessToken;
    private CallbackManager callbackManager3;
    private FacebookCallback<LoginResult> FacebookCallback3;
    private LoginManager loginManager3;
    private Button postButton;
    private LinearLayout Ll3;
    TimeLine reg;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        Bundle extras = getIntent().getExtras();
        String dataRcvd = (String) extras.get("dados");
        reg = separateString(dataRcvd); //separa a string recebida
        PopularTerceiraJanela(reg);

        facebookSDKInitialize();
        callbackManager3 = CallbackManager.Factory.create();
        getLoginDetails3();
    }

    //------------------------------------+
    //FUNÇÃO PARA SEPARAR STRING RECEBIDA |
    //------------------------------------+
    public TimeLine separateString(String s){
        TimeLine t = new TimeLine();
        int ind11, ind12, s1, ind21, ind22, s2, ind31, ind32, s3, ind41, ind42, ind51, ind52, ind61, ind62, ind71, ind72, ind81, ind82, ind91;

        ind11 = s.indexOf("#id#");
        ind12 = "#id#".length()+ind11;

        ind21 = s.indexOf("#title#");
        ind22 = "#title#".length()+ind21;

        ind31 = s.indexOf("#data#");
        ind32 = "#data#".length()+ind31;

        ind41 = s.indexOf("#description#");
        ind42 = "#description#".length()+ind41;

        ind51 = s.indexOf("#updated#");
        ind52 = "#updated#".length()+ind51;

        ind61 = s.indexOf("#avatarlarge#");
        ind62 = "#avatarlarge#".length()+ind61;

        ind71 = s.indexOf("#avatarthumb#");
        ind72 = "#avatarthumb#".length()+ind71;

        ind81 = s.indexOf("#userinfo#");
        ind82 = "#userinfo#".length()+ind81;

        ind91 = s.length()-1;

        t.setId(s.substring(ind12, ind21));

        t.setTitle(s.substring(ind22, ind31));
        t.setDate2(s.substring(ind32, ind41));
        t.setDescription(s.substring(ind42, ind51));
        t.setUpdated_at2(s.substring(ind52, ind61));
        t.setAvatar_url_large2(s.substring(ind62, ind71));
        t.setAvatar_url_thumb2(s.substring(ind72, ind81));
        t.setUser_info_author(s.substring(ind82, ind91));

        return t;
    }
    //------------------------------------+
    //FUNÇÃO PARA POPULAR TERCEIRA JANELA |
    //------------------------------------+
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void PopularTerceiraJanela(final TimeLine timeLine){


        Ll3 = (LinearLayout) findViewById(R.id.Ll03);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ViewGroup.LayoutParams param3 = new ViewGroup.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);

        params.gravity = Gravity.CENTER;
        param2.gravity = Gravity.CENTER;
        imgParam.gravity = Gravity.CENTER;

        //agora a criacao do Linear Layout vertical que será inserido do lado direito do Llhz
        LinearLayout LlVtc = new LinearLayout(this);
        LlVtc.setOrientation(LinearLayout.VERTICAL);
        LlVtc.setLayoutParams(param3);
        Ll3.addView(LlVtc);

        TextView txtTitle = new TextView(this);
        txtTitle.setText(timeLine.getTitle());
        txtTitle.setTextColor(Color.rgb(0, 0, 0));
        txtTitle.setTypeface(Typeface.DEFAULT_BOLD);
        txtTitle.setAllCaps(true);
        txtTitle.setLayoutParams(params);
        txtTitle.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
        LlVtc.addView(txtTitle);

        TextView txtTitle2 = new TextView(this);
        txtTitle2.setText("__________________________________________");
        txtTitle2.setLayoutParams(params);
        LlVtc.addView(txtTitle2);

        TextView txtDate = new TextView(this);
        txtDate.setText(timeLine.getDate());
        txtDate.setLayoutParams(params);
        LlVtc.addView(txtDate);

        TextView txtTitle3 = new TextView(this);
        txtTitle3.setText("__________________________________________");
        txtTitle3.setLayoutParams(params);
        LlVtc.addView(txtTitle3);

        TextView txtDcpt = new TextView(this);
        txtDcpt.setText("\n" + timeLine.getDescription() + "\n \n");
        txtDcpt.setLayoutParams(param2);
        txtDcpt.setGravity(Gravity.CENTER);
        LlVtc.addView(txtDcpt);


        ImageView imgThumb = buscaImagem(timeLine.getAvatar_url_large());
        imgThumb.setLayoutParams(imgParam);
        LlVtc.addView(imgThumb);

        TextView txtLstUpdt = new TextView(this);
        txtLstUpdt.setText("\n Última Atualização: " + timeLine.getUpdated_at());
        txtLstUpdt.setLayoutParams(param2);
        txtLstUpdt.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
        //txtLstUpdt.setWidth(1);
        LlVtc.addView(txtLstUpdt);

        postButton = new Button(this);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostOnFb(timeLine);

            }
        });
        postButton.setText("Postar no meu perfil");
        postButton.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
        LlVtc.addView(postButton);
    }

    protected void getLoginDetails3(){
        FacebookCallback3 = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("Login efetuado com sucesso: " + loginResult.toString());
                System.out.println("User ID: " + loginResult.getAccessToken().getUserId() + "\n" +
                                   "Auth Token: " + loginResult.getAccessToken().getToken());

            }
            @Override
            public void onCancel() {
                System.out.println("Login CANCELADO");
            }

            @Override
            public void onError(FacebookException e) {
                System.out.println("Facebook login failed error: " + e);
            }
        };


        loginManager3 = LoginManager.getInstance();
        loginManager3.registerCallback(callbackManager3, FacebookCallback3);
        loginManager3.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
    }

    public void PostOnFb(TimeLine timeLine){
        accessToken = AccessToken.getCurrentAccessToken();

        if(accessToken==null){
            System.out.println("NAO ESTA LOGADO");
        }else {
            System.out.println("SIM ESTA LOGADO");
        }
        System.out.println("ACESS-TOKEN: "+accessToken);
        System.out.println("CALLBACKMANAGER: "+ callbackManager3);
        System.out.println("FACEBOOKCALLBCK: "+ FacebookCallback3);
        final Bundle params = new Bundle();
        params.putString("name", "Linha do tempo - " + timeLine.getTitle());
        params.putString("caption", timeLine.getTitle());
        params.putString("description", timeLine.getDescription());
        params.putString("link","http://eic.ifsc.usp.br:3001/date/" + timeLine.getDate().substring(0, 2) + "-" + timeLine.getDate().substring(3, 5)+"-"+timeLine.getDate().substring(6,10) + "?");
        params.putString("picture", timeLine.getAvatar_url_large());

        GraphRequest request = new GraphRequest(accessToken, "me/feed", params, HttpMethod.POST, null);

        GraphResponse response = request.executeAndWait();
        System.out.println("GraphResponse: " + response);

        FacebookRequestError error = response.getError();

        if(error!=null){//caso ocorra algum erro, tentar mais uma vez
            request = new GraphRequest(accessToken, "me/feed", params, HttpMethod.POST, null);
            response = request.executeAndWait();
            System.out.println("GraphResponse: " + response);
            error = response.getError();
            System.out.println("TENTOU MAIS UMA VEZ!");
        }

        if (error != null) {
            Toast.makeText(TercActivity.this, "Erro ao publicar", Toast.LENGTH_SHORT).show();
            System.out.println("ERRO AO POSTAR: " + response.getError());
        }else
        {
            Toast.makeText(TercActivity.this, "Postado no Facebook", Toast.LENGTH_SHORT).show();
            System.out.println("Postado no facebook com sucesso!");
        }
    }
}
