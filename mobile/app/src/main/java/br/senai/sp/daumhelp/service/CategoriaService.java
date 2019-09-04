package br.senai.sp.daumhelp.service;

import java.util.List;

import br.senai.sp.daumhelp.model.Categoria;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriaService {

    @GET("categorias")
    Call<List<Categoria>> buscarCategorias();

}
