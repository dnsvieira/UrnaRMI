package br.com.atividade6;

/**
 *
 * @author denis.vieira
 */
import java.io.Serializable;

public class Candidato implements Serializable {

    private String nome;
    private Integer numero;

    public Candidato(String nome, Integer numero) {
        this.nome = nome;
        this.numero = numero;
    }
   
    public String getNome() {
        return nome;
    }
    public Integer getNumero() {
        return numero;
    }
}