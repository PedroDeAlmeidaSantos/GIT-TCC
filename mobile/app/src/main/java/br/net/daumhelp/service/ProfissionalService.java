package br.net.daumhelp.service;

import br.net.daumhelp.model.Confirmacao;
import br.net.daumhelp.model.Profissional;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProfissionalService {

    @POST("profissionais")
    Call<Profissional> cadastrarProfissional(@Body Profissional profissional);

    @POST("profissionais/confirmacao")
    Call<Confirmacao> confirmarEmail(@Body Confirmacao confirmacao);


    @PUT("profissionais/atualizar/id/{idPro}")
    Call<Profissional> atualizarPro(@Path("idPro")int idPro, @Body Profissional profissional);

    @GET("profissionais/id/{id}")
    Call<Profissional> buscarProfissional(@Path("id") int id);

}
