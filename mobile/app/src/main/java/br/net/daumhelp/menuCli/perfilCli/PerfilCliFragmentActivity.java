package br.net.daumhelp.menuCli.perfilCli;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import br.net.daumhelp.R;
import br.net.daumhelp.configretrofit.RetroFitConfig;
import br.net.daumhelp.model.Cidade;
import br.net.daumhelp.model.Cliente;
import br.net.daumhelp.model.Endereco;
import br.net.daumhelp.model.EnderecoViaCep;
import br.net.daumhelp.model.TipoUsuario;
import br.net.daumhelp.recursos.EncryptString;
import br.net.daumhelp.recursos.Mascara;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PerfilCliFragmentActivity extends Fragment {

    private PerfilCliViewModel perfilCliViewModel;
    private EditText etNome;
    private EditText etData;
    private EditText etCpf;
    private EditText etSenha;
    private EditText etSenhaConfirmacao;
    private EditText etEmail;
    private EditText etCep;
    private EditText etCidade;
    private EditText etLogradouro;
    private EditText etUf;
    private EditText etBairro;
    private Cliente cliente;
    private TextView tvNome;
    private TextView tvEditarDados;
    private TextView tvEditarLocal;
    private Button btnCep;
    private ImageButton btnEditar;
    private ImageButton btnSalvar;
    private ImageButton btnAjuda;
    private Endereco endereco;
    private Long idCidade;
    private EnderecoViaCep enderecoViaCep;
    private ImageView ivFotoCliente;
    private String tokenCliente;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        perfilCliViewModel = ViewModelProviders.of(this).get(PerfilCliViewModel.class);
        View root = inflater.inflate(R.layout.activity_fragment_perfil_cliente, container, false);
        getActivity().getWindow().setStatusBarColor(Color.parseColor("#77C9D4"));


        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //btnConfig = getView().findViewById(R.id.ic_config);

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#77C9D4"));

        etNome = getView().findViewById(R.id.et_nome_pro);
        etData = getView().findViewById(R.id.et_nasc_pro);
        etCpf = getView().findViewById(R.id.et_cpf_pro);
        etSenha = getView().findViewById(R.id.et_senha_pro);
        etSenhaConfirmacao = getView().findViewById(R.id.et_confirm_pro);
        etEmail = getView().findViewById(R.id.et_email_pro);
        etCep = getView().findViewById(R.id.et_cep_pro);
        etCidade = getView().findViewById(R.id.et_cidade_pro);
        etLogradouro = getView().findViewById(R.id.et_logradouro_pro);
        etUf = getView().findViewById(R.id.et_uf_pro);
        etBairro = getView().findViewById(R.id.et_bairro_pro);
        tvNome = getView().findViewById(R.id.tv_nome);
        tvEditarDados = getView().findViewById(R.id.tv_editar_dados);
        tvEditarLocal = getView().findViewById(R.id.tv_editar_local);
        btnCep = getView().findViewById(R.id.btn_gerar_cep);
        btnEditar = getView().findViewById(R.id.ib_editar);
        btnSalvar = getView().findViewById(R.id.ib_salvar);
        btnAjuda = getView().findViewById(R.id.ib_ajuda);
        ivFotoCliente = getView().findViewById(R.id.profile_image_cliente);



        desativarCamposDadosPessoais();
        desativarCamposDadosEndereco();

        Mascara maskCep = new Mascara("#####-###", etCep);
        etCep.addTextChangedListener(maskCep);

        Intent intent = getActivity().getIntent();

        if (intent.getSerializableExtra("tokenCliente") != null) {
            tokenCliente = (String) intent.getSerializableExtra("tokenCliente");
        }


        if (intent.getSerializableExtra("cliente") != null) {

            cliente = (Cliente) intent.getSerializableExtra("cliente");
            idCidade = cliente.getEndereco().getCidade().getIdCidade();
            carregarDados(cliente);
        }

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ativarCamposDadosPessoais();
            }
        });

        tvEditarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnCep.setTextColor(Color.parseColor("#77C9D4"));
                etCep.requestFocus(View.FOCUS_LEFT);
                etCep.setEnabled(true);
                btnCep.setEnabled(true);


                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etCep, InputMethodManager.SHOW_IMPLICIT);

                if(tvEditarLocal.getText().equals("Editar")){

                    tvEditarLocal.setText("Salvar");
                    btnCep.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String cep = etCep.getText().toString();
                            /*VALIDAÇÃO DO TAMANHO DO CEP*/
                            if(etCep.length() == 8 || etCep.length() == 9){
                                Call<EnderecoViaCep> call = new RetroFitConfig().getEnderecoService().buscarEnderecoViaCep(cep);
                                call.enqueue(new Callback<EnderecoViaCep>() {
                                    @Override
                                    public void onResponse(Call<EnderecoViaCep> call, Response<EnderecoViaCep> response) {
                                        if(response.code() == 404){
                                            etCep.setError("CPF inválido");
                                        }else{
                                            carregarEndereco(response.body());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<EnderecoViaCep> call, Throwable t) {
                                        Log.i("Retrofit Endereço", t.getMessage());
                                        etCep.setError("CEP inválido");
                                    }

                                });
                            }else{
                                etCep.setError("CEP inválido");
                            }
                        }
                    });

                }else if(tvEditarLocal.getText().equals("Salvar")){
                    btnCep.setTextColor(Color.parseColor("#b1b1b1"));
                    tvEditarLocal.setText("Editar");
                    etCep.setEnabled(false);

                }
            }
        });

        tvEditarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ativarCamposDadosPessoais();
                /*ABRIR TECLADO QUANDO CLICAR EM EDITAR DADOS*/
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etNome, InputMethodManager.SHOW_IMPLICIT);

                if(tvEditarDados.getText().equals("Editar")){
                    tvEditarDados.setText("Salvar");

                }else if(tvEditarDados.getText().equals("Salvar")){

                    /*VERIFICANDO SE AS SENHAS SÃO IGUAIS*/
                    if(etSenhaConfirmacao.getText().toString().equals(etSenha.getText().toString())) {

                        /*VALIDANDO OS CAMPOS*/
                        if (validar() == true) {
                            tvEditarDados.setText("Editar");
                            desativarCamposDadosPessoais();


                        }
                    }else{
                        Toast.makeText(getContext(), "As senhas não correspondem", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*MONTANDO O OBJETO PROFISSIONAL QUE SERÁ ATUALIZADO*/
                cliente.setNome(etNome.getText().toString());
                cliente.setDataNasc(etData.getText().toString());
                cliente.setCpf(etCpf.getText().toString());
                cliente.setEmail(etEmail.getText().toString());
                TipoUsuario tipoUsuario = new TipoUsuario();
                tipoUsuario.setIdTipoUsuario(2);
                tipoUsuario.setTipoDeUsuario('c');
                cliente.setTipoUsuario(tipoUsuario);

                if (etSenha.getText().equals(128)) {
                    cliente.setSenha(cliente.getSenha());
                }else{
                    cliente.setSenha(EncryptString.gerarHash(etSenha.getText().toString()));
                }

                Endereco endereco = new Endereco();
                endereco.setIdEndereco(cliente.getEndereco().getIdEndereco());
                endereco.setCep(etCep.getText().toString());
                endereco.setBairro(etBairro.getText().toString());
                endereco.setLogradouro(etLogradouro.getText().toString());
                Cidade cidade = new Cidade();
                cidade.setIdCidade(idCidade);
                endereco.setCidade(cidade);

                if (validar() == true){

                    /*CHAMADA PARA ATUALIZAR ENDEREÇO*/
                    Call<Endereco> call = new RetroFitConfig().getEnderecoService().atualizarEndereco(tokenCliente, cliente.getEndereco().getIdEndereco(), endereco);
                    call.enqueue(new Callback<Endereco>() {
                        @Override
                        public void onResponse(Call<Endereco> call, Response<Endereco> response) {

                            response.body();

                            /*CHAMADA PARA ATUALIZAR CLIENTE*/
                            Call<Cliente> call2 = new RetroFitConfig().getClienteService().atualizarCli(tokenCliente, cliente.getIdCliente(), cliente);
                            call2.enqueue(new Callback<Cliente>() {
                                @Override
                                public void onResponse(Call<Cliente> call2, Response<Cliente> response) {
                                    response.body();
                                    tvNome.setText(etNome.getText());
                                    Toast.makeText(getContext(), "Dados atualizados!", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onFailure(Call<Cliente> call2, Throwable t) {
                                    Log.i("CLIENTE", t.getMessage());
                                }
                            });
                        }
                        @Override
                        public void onFailure(Call<Endereco> call, Throwable t) {
                            Log.i("ENDERECO", t.getMessage());
                        }
                    });

                }

            }
        });


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
    }


    /*MÉTODO DE CARREGAR ENDEREÇO*/
    private void carregarEndereco(EnderecoViaCep enderecoViaCep){
        this.enderecoViaCep = enderecoViaCep;
        etUf.setText(enderecoViaCep.getUf());
        etBairro.setText(enderecoViaCep.getBairro());
        etLogradouro.setText(enderecoViaCep.getLogradouro());
        etCidade.setText(enderecoViaCep.getLocalidade());
        idCidade = enderecoViaCep.getIbge();

    }

    /*MÉTODO DE CARREGAR DADOS NO PERFIL*/
    private void carregarDados(Cliente cliente){
        etNome.setText(cliente.getNome());
        etData.setText(cliente.getDataNasc());
        etCpf.setText(cliente.getCpf());
        etEmail.setText(cliente.getEmail());
        etCep.setText(cliente.getEndereco().getCep());
        etCidade.setText(cliente.getEndereco().getCidade().getCidade());
        //etSenha.setText(cliente.getSenha());
        //etSenhaConfirmacao.setText(cliente.getSenha());
        etLogradouro.setText(cliente.getEndereco().getLogradouro());
        etUf.setText(cliente.getEndereco().getCidade().getMicrorregiao().getUf().getUf());
        etBairro.setText(cliente.getEndereco().getBairro());
        tvNome.setText(cliente.getNome().toUpperCase());
        String fotoCli = cliente.getFoto();
        Picasso.get().load("http://ec2-3-220-68-195.compute-1.amazonaws.com/" + fotoCli).resize(100,100).into(ivFotoCliente);

    }

    /*DESATIVA OS CAMPOS AO CLICAR EM SALVAR*/
    private void desativarCamposDadosPessoais(){
        etNome.setEnabled(false);
        etData.setEnabled(false);
        etCpf.setEnabled(false);
        etSenha.setEnabled(false);
        etSenhaConfirmacao.setEnabled(false);
        etEmail.setEnabled(false);

    }

    /*ATIVA OS CAMPOS AO CLICAR EM EDITAR*/
    private void ativarCamposDadosPessoais(){
        etNome.setEnabled(true);
        etData.setEnabled(true);
        etCpf.setEnabled(true);
        etSenha.setEnabled(true);
        etSenhaConfirmacao.setEnabled(true);
        etEmail.setEnabled(true);
        etNome.requestFocus(View.FOCUS_LEFT);
    }

    private boolean validar(){
        boolean validado = true;
        if(etNome.getText().toString().isEmpty()){
            etNome.setError("Digite seu nome por favor");
            validado = false;
        }
        if(etNome.getText().length()<=2){
            etNome.setError("O nome deve conter no min. 3 caracteres");
            validado = false;
        }
        if(etEmail.getText().toString().isEmpty()){
            validado = false;
        }
        if(etEmail.getText().length()<9){
            etEmail.setError("O e-mail deve conter no min. 9 caracteres");
            validado = false;
        }
        if(etCpf.getText().toString().isEmpty()){
            validado = false;
        }
        if(etCpf.getText().length()<14){
            etCpf.setError("CPF/CNPJ inválido");
            validado = false;
        }
        if(etSenha.getText().toString().isEmpty()){
            validado = false;
        }
        if(etSenha.getText().length()<8){
            etSenha.setError("A senha deve conter no min. 8 caracteres");
            validado = false;
        }
        if(etData.getText().toString().isEmpty()){
            validado = false;
        }
        if(etData.getText().length()<10){
            etData.setError("Data de nascimento inválida");
            validado = false;
        }

        return validado;
    }


    private void desativarCamposDadosEndereco(){
        etCep.setEnabled(false);
        btnCep.setEnabled(false);
        etBairro.setEnabled(false);
        etUf.setEnabled(false);
        etLogradouro.setEnabled(false);
        etCidade.setEnabled(false);
    }
}