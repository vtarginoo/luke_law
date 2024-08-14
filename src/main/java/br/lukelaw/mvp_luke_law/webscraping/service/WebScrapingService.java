package br.lukelaw.mvp_luke_law.webscraping.service;

import br.lukelaw.mvp_luke_law.webscraping.config.WebDriverFactory;
import org.openqa.selenium.*;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class WebScrapingService {

    public String scrapePjeConsultaPublica(String numeroProcesso) {
        WebDriver driver = WebDriverFactory.createChromeDriver();
        String ultimaMovimentacao = "";

        try {
            // Acessa a página do PJE Consulta Pública do TJ-RJ
            driver.get("https://tjrj.pje.jus.br/1g/ConsultaPublica/listView.seam");

            // Adiciona o cookie rxvisitor manualmente
            Cookie rxVisitorCookie = new Cookie("rxvisitor", "1722960632647NRQOMJVLQSK7UM5RBBNRF8S5QGFHD1R7");
            driver.manage().addCookie(rxVisitorCookie);

            // Adiciona o valor no local storage
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.localStorage.setItem('rxvisitor', '1722960632647NRQOMJVLQSK7UM5RBBNRF8S5QGFHD1R7');");

            // Recarrega a página para que o cookie seja aplicado
            driver.navigate().refresh();

            // Identifica o campo de pesquisa e insere o número do processo
            WebElement searchField = driver.findElement(By.id("fPP:numProcesso-inputNumeroProcessoDecoration:numProcesso-inputNumeroProcesso"));
            searchField.sendKeys(numeroProcesso);

            // Simula o pressionamento da tecla Enter para submeter a busca
            searchField.sendKeys(Keys.ENTER);

            // Espera um pouco para a página de login carregar, se for o caso
            Thread.sleep(2000);

            // Verifica se a página de login foi carregada
            if (driver.getCurrentUrl().contains("login")) {
                System.out.println("Redirecionado para a página de login, voltando...");

                // Volta para a página anterior
                driver.navigate().back();

                // Reenvia a pesquisa
                searchField = driver.findElement(By.id("fPP:numProcesso-inputNumeroProcessoDecoration:numProcesso-inputNumeroProcesso"));
                searchField.sendKeys(numeroProcesso);
                searchField.sendKeys(Keys.ENTER);

                // Aguarda um tempo para a página carregar novamente
                Thread.sleep(2000);
            }

            // Espera explícita para garantir que o elemento esteja presente antes de acessá-lo
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement movimentacaoElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//tbody[@id='fPP:processosTable:tb']/tr[1]/td[3]")));

            // Captura a última movimentação
            ultimaMovimentacao = movimentacaoElement.getText();
            System.out.println("Última Movimentação: " + ultimaMovimentacao);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return ultimaMovimentacao;
    }
}


