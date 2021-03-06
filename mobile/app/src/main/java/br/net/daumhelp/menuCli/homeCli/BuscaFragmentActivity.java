package br.net.daumhelp.menuCli.homeCli;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import br.net.daumhelp.PerfilProfissionalBuscaActivity;
import br.net.daumhelp.R;
import br.net.daumhelp.adapter.ListaAdapterBusca;
import br.net.daumhelp.configretrofit.RetroFitConfig;
import br.net.daumhelp.model.Cliente;
import br.net.daumhelp.model.Profissional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscaFragmentActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private TextView tvNome;
    private BuscaViewModel buscaViewModel;
    private Cliente cliente;
    private ListaAdapterBusca listaProfissional;
    private SwipeRefreshLayout mSwipeToRefresh;
    private Button btnFiltro;
    private Dialog alertDialog;
    private String tokenCliente;

    private ListView listView;

    ArrayList<Profissional> lista = new ArrayList<Profissional>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        buscaViewModel = ViewModelProviders.of(this).get(BuscaViewModel.class);

        View root = inflater.inflate(R.layout.activity_fragment_busca, container, false);

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#77C9D4"));

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();
        progressDialog.setContentView(R.layout.layout_progressbar);


        btnFiltro = getView().findViewById(R.id.btn_filtro);
        listView =  getView().findViewById(R.id.lv_busca_pro);
        mSwipeToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_container);
        mSwipeToRefresh.setOnRefreshListener(this);

        alertDialog = new Dialog(getContext());

        Intent intent = getActivity().getIntent();

        if (intent.getSerializableExtra("tokenCliente") != null) {
            tokenCliente = (String) intent.getSerializableExtra("tokenCliente");
        }

        if(intent.getSerializableExtra("cliente") != null) {
            cliente = (Cliente) intent.getSerializableExtra("cliente");
        }


        btnFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.setContentView(R.layout.adapter_filtro_profissional);
                alertDialog.show();
            }
        });

        final ListaAdapterBusca listaProfissional = new ListaAdapterBusca(getContext(), lista, cliente, tokenCliente);

        int idMicroCliente = cliente.getEndereco().getCidade().getMicrorregiao().getIdMicro();

        tvNome = getView().findViewById(R.id.tv_nome_profissional);

        Call<List<Profissional>> call = new RetroFitConfig().getProfissionalService().buscarProfissionalMicro(tokenCliente, idMicroCliente);
        call.enqueue(new Callback<List<Profissional>>() {
            @Override
            public void onResponse(Call<List<Profissional>> call, Response<List<Profissional>> response) {

                lista = (ArrayList<Profissional>) response.body();
                if (lista != null) {
                    for (Profissional p : lista) {
                        listaProfissional.add(p);
                    }
                    listView.setAdapter(listaProfissional);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Profissional>> call, Throwable t) {
                Log.i("PROFISSIONAIS BUSCA", t.getMessage());
                progressDialog.dismiss();
            }

        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onRefresh() {

        int idMicroCliente = cliente.getEndereco().getCidade().getMicrorregiao().getIdMicro();
        Call<List<Profissional>> call = new RetroFitConfig().getProfissionalService().buscarProfissionalMicro(tokenCliente, idMicroCliente);
        call.enqueue(new Callback<List<Profissional>>() {
            @Override
            public void onResponse(Call<List<Profissional>> call, Response<List<Profissional>> response) {

                lista = (ArrayList<Profissional>) response.body();

                if(lista != null){

                    listaProfissional = new ListaAdapterBusca(getContext(), lista, cliente, tokenCliente);
                    ListView listView = (ListView) getView().findViewById(R.id.lv_busca_pro);

                    listView.setAdapter(listaProfissional);

                    mSwipeToRefresh.setRefreshing(false);
                }else{

                    mSwipeToRefresh.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<List<Profissional>> call, Throwable t) {
                Log.i("PROFISSIONAIS BUSCA", t.getMessage());
            }

        });
    }
}