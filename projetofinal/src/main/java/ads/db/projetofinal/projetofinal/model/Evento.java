package ads.db.projetofinal.projetofinal.model;

import java.time.LocalDate;
import java.util.ArrayList;

import lombok.Data;

@Data
public class Evento {
    private Integer codigo;
    private Integer senha;
    private String local;
    private LocalDate data;
    private String descricao;
    private String nome;

    private ArrayList<Pessoa> convidados;
    private ArrayList<ComesBebes> comesBebes;

    // the start event object
    public Evento(String local, LocalDate data, String descricao, String nome) {
        this.local = local;
        this.data = data;
        this.descricao = descricao;
        this.nome = nome;
    }

    // the final event object
    public Evento(Integer codigo, Integer senha, String local, LocalDate data, String descricao, String nome) {
        this.codigo = codigo;
        this.senha = senha;
        this.local = local;
        this.data = data;
        this.descricao = descricao;
        this.nome = nome;
    }

    // gerar senha de evento 
    public void gerarSenhaEvento(int codigo, LocalDate data){
        
        // ==---> senha é: [codigo] + [ano] + [dia]
        String concatSenha = "" + codigo + "" + data.getYear() + "" + data.getDayOfMonth() + "";

        this.senha = Integer.parseInt(concatSenha);
    }

}