package br.net.daumhelp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import br.net.daumhelp.configretrofit.RetroFitConfig;
import br.net.daumhelp.model.Cliente;
import br.net.daumhelp.model.Pedido;
import br.net.daumhelp.model.Profissional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

//    public ArrayList<Pedido> lista = new ArrayList<Pedido>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder( R.id.navigation_dashboard).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        Intent intent = getIntent();
        if (intent.getSerializableExtra("profissional") != null) {
            Profissional profissional = (Profissional) intent.getSerializableExtra("profissional");
        }

        if (intent.getSerializableExtra("tokenProfissional") != null) {
            String tokenProfissional = (String) intent.getSerializableExtra("tokenProfissional");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if (intent.getSerializableExtra("profissional") != null) {
            Profissional profissional = (Profissional) intent.getSerializableExtra("profissional");
        }
    }
}
