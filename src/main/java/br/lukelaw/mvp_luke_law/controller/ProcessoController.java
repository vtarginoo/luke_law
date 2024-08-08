package br.lukelaw.mvp_luke_law.controller;


import br.lukelaw.mvp_luke_law.entity.Processo;
import br.lukelaw.mvp_luke_law.service.ProcessoService;
import br.lukelaw.mvp_luke_law.task.ProcessoTask;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/processos")
public class ProcessoController {

    @Autowired
    ProcessoService processoService;


    @GetMapping("/{numProcesso}")
    public ResponseEntity<Processo> capturarInfoProcesso (@PathVariable String numProcesso) {

        Processo response = processoService.realizarRequisicao(numProcesso);
       // processoService.monitoramentoMovimentoDeProcesso();

        return ResponseEntity.ok(response);

    }

}
