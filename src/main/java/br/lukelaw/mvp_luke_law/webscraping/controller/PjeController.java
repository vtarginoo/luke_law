package br.lukelaw.mvp_luke_law.webscraping.controller;

import br.lukelaw.mvp_luke_law.webscraping.service.WebScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pje")
public class PjeController {

    @Autowired
    private WebScrapingService webScrapingService;

    @GetMapping("/consulta")
    public String consultarUltimaMovimentacao() {

        String numeroProcesso = "0809129-51.2024.8.19.0001";

        return webScrapingService.scrapePjeConsultaPublica(numeroProcesso);
    }
}



