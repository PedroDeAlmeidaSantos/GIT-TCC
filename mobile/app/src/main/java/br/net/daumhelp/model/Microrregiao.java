package br.net.daumhelp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Microrregiao implements Serializable {

    private UF uf;

    private int idMicro;

    public int getIdMicro() {
        return idMicro;
    }

    public void setIdMicro(int idMicro) {
        this.idMicro = idMicro;
    }

    public UF getUf() {
        return uf;
    }

    public void setUf(UF uf) {
        this.uf = uf;
    }

    @Override
    public String toString() {
        return "Microrregiao{" +
                "uf=" + uf +
                ", idMicro=" + idMicro +
                '}';
    }
}
