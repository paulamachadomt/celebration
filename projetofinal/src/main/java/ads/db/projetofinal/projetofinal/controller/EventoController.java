package ads.db.projetofinal.projetofinal.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ads.db.projetofinal.projetofinal.dao.EventoDAO;
import ads.db.projetofinal.projetofinal.dao.PessoaDAO;
import ads.db.projetofinal.projetofinal.model.Evento;
import ads.db.projetofinal.projetofinal.model.Pessoa;

@RestController
public class EventoController {

    // @PostMapping("/localdate")
    // public String testa(
    // @RequestParam("localDate")
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    // LocalDate localDate) {

    // System.out.println(localDate);

    // System.out.println("" + localDate.getDayOfMonth()
    // + localDate.getDayOfWeek() + localDate.getMonth() + localDate.getYear());

    // return "" + localDate;
    // }

    // TERNARIO --==> (condição) ? [true] : [false]

    @PostMapping("/cadastroEvento")
    public String doGet(
        @RequestParam("localidade") String local,
        @RequestParam("localDate") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, 
        String descricao,
        String nome) {

        EventoDAO cadastro = new EventoDAO();

        Evento evento = cadastro.gerarEvento(new Evento(local, data, descricao, nome));

        return evento.toString();
    }

    @GetMapping("/pesquisaSenhaEvento")
    public String doGet(Integer senha) {

        EventoDAO pesquisa = new EventoDAO();

        Evento evento = pesquisa.selectSenhaEvento(senha);

        return evento == null ? "Evento não encontrado" : "Evento cadastrado: " + evento.toString();
    }

    @GetMapping("/pesquisaCodEvento")
    public String pesquisaCod(Integer codigo) {

        EventoDAO pesquisa = new EventoDAO();

        Evento evento = pesquisa.selectCodEvento(codigo);

        return evento == null ? "Evento não encontrado" : "Evento cadastrado: " + evento.toString();
    }

    @PostMapping("/updateEvento")
    public String atualizaEvento(
        Integer codigo,
        Integer senha,
        @RequestParam("localidade") String local,
        @RequestParam("localDate") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, 
        String descricao,
        String nome) {

        System.out.println(codigo +""+ senha+""+ local+""+ data+""+""+ descricao+""+ nome);

        EventoDAO atualiza = new EventoDAO();

        Evento evento = new Evento(codigo, senha, local, data, descricao, nome);
        System.out.println(evento.toString());

        boolean resultado = atualiza.updateEvento(evento);

        return "ola," + evento.getNome() + "\n\n  " + resultado;
    }

    @GetMapping("/deleteEvento")
    public String deleteEvento(Integer codigo) {

        EventoDAO delete = new EventoDAO();

        boolean resultado = delete.deleteEvento(codigo);

        return resultado == true ? "Deletado com sucesso." : "Erro ao deletar";
    }

}
