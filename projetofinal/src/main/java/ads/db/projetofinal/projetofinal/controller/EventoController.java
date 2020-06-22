package ads.db.projetofinal.projetofinal.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ads.db.projetofinal.projetofinal.model.Evento;
import ads.db.projetofinal.projetofinal.model.Pessoa;
import ads.db.projetofinal.projetofinal.model.Convidado;

@RestController
public class EventoController extends UtilEvento{

    @RequestMapping(
        method = RequestMethod.GET, 
        produces = "application/json", 
        value = "/eventos"
        )
    public List<Evento> getHome(
            @CookieValue(value = "nome", defaultValue = "null") String nome,
            @CookieValue(value = "cpf", defaultValue = "null") String cpf, 
            HttpServletResponse response
                ) {
                Pessoa pessoa = new Pessoa(cpf, nome.toLowerCase());
                List<Evento> eventos = new ArrayList<>();
                if (new UtilLogin().autenticarLogin(pessoa)) {
                    eventos = carregaEventosConvidado(pessoa);
                }
                response.addCookie(killCookie("codigo_evento", ""));  // remove cookie autentica acesso ao evento 
                response.addCookie(killCookie("criador_evento", "")); // remove cookie autentica acesso a edição de evento                                                 
                return eventos;
            }

    @RequestMapping(
        method = RequestMethod.POST, 
        produces = "application/json", 
        value = "/eventos"
        )
    public Evento createEvento(
            @CookieValue(value = "cpf", defaultValue = "null") String cpf,
            @RequestBody Evento evento, // JSON : {"local": "Meu primeiro evento de teste 2","data": "2020-10-20","nome": "Festa Pandemia 2"}
            HttpServletResponse response
            // @RequestParam String local, // by simple form
            // @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, // by simple form
            // @RequestParam String nome,  // by simple form
                ) {
                // Evento evento = new Evento(local, data, nome);
                if (UtilCheck.loginIsAuthenticated(cpf)) {
                    evento = cadastrarEvento(evento, cpf);
                    if (evento.getCodigo() != null) {
                        response.addCookie(getCookie("codigo_evento", ""+evento.getCodigo()));
                        response.addCookie(getCookie("criador_evento", ""+true));
                    }
                }
                return evento; 
            }

    @RequestMapping(
        method = RequestMethod.GET, 
        produces = "application/json", 
        value = "/eventos/{codigoEvento}"
        )
    public Evento readEvento(
            @CookieValue(value = "cpf", defaultValue = "null") String cpf,
            @PathVariable Integer codigoEvento,
            HttpServletResponse response
                ) {
                Evento evento = null;
                Convidado convidado = null;
                if (UtilCheck.loginIsAuthenticated(cpf)) {
                    evento = carregarEvento(codigoEvento);
                    convidado = verificaECarregaConvidadoDoEvento(evento, cpf);
                    if (convidado == null) {
                        evento = new Evento();
                        response.addCookie(killCookie("codigo_evento", ""));  // remove cookie autentica acesso ao evento 
                        response.addCookie(killCookie("criador_evento", "")); // remove cookie autentica acesso a edição de evento 
                    } else {
                        response.addCookie(getCookie("codigo_evento", ""+evento.getCodigo()));
                        response.addCookie(getCookie("criador_evento", ""+convidado.getCriadorEvento()));
                    }
                }
                return evento;
            }

    @RequestMapping(
        method = RequestMethod.PUT, 
        produces = "application/json", 
        value = "/eventos/{codigoEvento}"
        )
    public Boolean updateEvento(
            @CookieValue(value = "cpf", defaultValue = "null") String cpf,
            @CookieValue(value = "codigo_evento", defaultValue = "null") String codigo_evento,
            @CookieValue(value = "criador_evento", defaultValue = "null") String criador_evento,
            @RequestBody Evento evento, // JSON : {"local": "Meu primeiro evento de teste 2","data": "2020-10-20","nome": "Festa Pandemia 2","descricao": "alguma descricao BBBB"}
            @PathVariable Integer codigoEvento
            // @RequestParam String local, // by simple form
            // @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, // by simple form
            // @RequestParam String nome,  // by simple form
            // @RequestParam String descricao, // by simple form 
                ) {
                // Evento evento = new Evento(codigoEvento, local, data, nome, descricao);
                boolean resultado = false;
                if (UtilCheck.loginIsAuthenticated(cpf) 
                &&  UtilCheck.codigoEventoIsAuthenticated(codigo_evento, codigoEvento)
                &&  UtilCheck.criadorEventoIsAuthenticated(criador_evento)
                    ) {
                        evento.setCodigo(codigoEvento);
                        resultado = atualizarEvento(evento);
                    }
                return resultado;
            }

    @RequestMapping(
        method = RequestMethod.DELETE, 
        produces = "application/json", 
        value = "/eventos/{codigoEvento}"
        )
    public boolean deleteEvento(
            @CookieValue(value = "cpf", defaultValue = "null") String cpf,
            @CookieValue(value = "codigo_evento", defaultValue = "null") String codigo_evento,
            @CookieValue(value = "criador_evento", defaultValue = "null") String criador_evento,
            @PathVariable Integer codigoEvento
                ) {
                boolean resultado = false;
                if (UtilCheck.loginIsAuthenticated(cpf) 
                &&  UtilCheck.codigoEventoIsAuthenticated(codigo_evento, codigoEvento)
                &&  UtilCheck.criadorEventoIsAuthenticated(criador_evento)
                    ) {
                    resultado = verificaEDeletaEventoERegistros(codigoEvento);
                }
            return resultado;
        }

}
