package br.net.daumhelp.service;

import java.util.List;

import br.net.daumhelp.model.Subcategoria;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SubcategoriaService {

    @GET("/subcategorias/categoria/{idCategoria}")
    Call<List<Subcategoria>> buscarSubcategorias(@Path("idCategoria") int idCategoria);

}
