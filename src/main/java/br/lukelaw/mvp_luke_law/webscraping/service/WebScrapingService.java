package br.lukelaw.mvp_luke_law.webscraping.service;

import br.lukelaw.mvp_luke_law.webscraping.config.WebDriverFactory;
import br.lukelaw.mvp_luke_law.webscraping.entity.Movimento;
import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import br.lukelaw.mvp_luke_law.webscraping.utils.WebScrapingUtil;
import org.openqa.selenium.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebScrapingService {

    @Autowired
    MovimentoService movimentoService;

    @Autowired
    WebScrapingUtil webScrapingUtil;

    public Processo scrapePjeUltimoMov(String numProcesso) {
        WebDriver driver = WebDriverFactory.createChromeDriver();
        String pjeUrl = "https://tjrj.pje.jus.br/1g/ConsultaPublica/listView.seam";

        Processo processoCapturado = null;
        Movimento ultimoMovimento = null;
        List<Movimento> movimentos = new ArrayList<>();

        try {

            System.out.println("Iniciando scraping do PJE para o processo: " + numProcesso);

            //última movimentação como string
            String movimentoScrape = webScrapingUtil.ScrapingUltimaMov(driver, pjeUrl, numProcesso);

            System.out.println("Movimentação capturada: " + movimentoScrape);

            // Transforma a string capturada em um objeto Movimento
            ultimoMovimento = movimentoService.criarMovimento(movimentoScrape);

            System.out.println("Movimento criado: " + ultimoMovimento);
            // Insere o último movimento na lista
            movimentos.add(ultimoMovimento);

            // Transforma movimento em um processo
            processoCapturado = new Processo(numProcesso, "TJRJ", "Pje", "1ªInstancia",
                    movimentos, ultimoMovimento.dataHora());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return processoCapturado;
    }
}


