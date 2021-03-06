package br.net.daumhelp.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Subcategoria implements Serializable {

    private Long idSubcategoria;
    private Categoria categoria;

    @JsonAlias("subcategoria")
    private String subcategoria;

    public Long getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setIdSubcategoria(Long idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return subcategoria;
    }
}
