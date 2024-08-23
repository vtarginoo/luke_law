package br.lukelaw.mvp_luke_law.messaging.templates;

import br.lukelaw.mvp_luke_law.messaging.dto.AnaliseDeMovimento;
import br.lukelaw.mvp_luke_law.webscraping.entity.Movimento;
import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import br.lukelaw.mvp_luke_law.xSimulateBD.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class MessageTemplate {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    public String mensagemBodyAtiva(AnaliseDeMovimento analiseDeMovimento, Processo requestProcesso, String advogadoNome) {
        var ultimoMovimento = analiseDeMovimento.getProcesso().getMovimentos().get(0);
        String dataHoraFormatada = ultimoMovimento.dataHora().format(formatter);

        return  "*⚠️ Alerta de Movimentação no Processo*\n\n" +
                "Olá, *" + advogadoNome.toUpperCase() + "*!\n" +
                "👥 *Partes:* " + requestProcesso.getPartesEnvolvidas() + "\n" +
                "📄 *Processo:* " + requestProcesso.getNumeroProcesso() + "\n" +
                "🏛️ *Tribunal:* " + requestProcesso.getTribunal() + "\n" +
                "🖥️ *Sistema:* " + requestProcesso.getSistema() + "\n\n" +
                "*Última Movimentação:*\n" +
                "🔍 *Tipo:* " + ultimoMovimento.nome() + "\n" +
                "🕒 *Data e Hora:* " + dataHoraFormatada + "\n" +
                "⏳ *Horas desde a Última Movimentação:* " + analiseDeMovimento.getHorasDesdeUltimoMovimento() + " horas\n\n" +
                "⚖️ Por favor, verifique os detalhes no sistema.";
    }

    public String mensagemBodyPassivaGenerico(Processo requestProcesso) {
        Movimento movimentoProcesso = requestProcesso.getMovimentos().get(0);
        String dataHoraFormatada = movimentoProcesso.dataHora().format(formatter);
        LocalDateTime agora = LocalDateTime.now();
        long horasDesdeUltimoMovimento = ChronoUnit.HOURS.between(movimentoProcesso.dataHora(), agora);

        return  "*ℹ️ Segue a última movimentação de seu processo:*\n\n" +
                "👥 *Partes:* " + requestProcesso.getPartesEnvolvidas() + "\n" +
                "📄 *Processo:* " + requestProcesso.getNumeroProcesso() + "\n" +
                "🏛️ *Tribunal:* " + requestProcesso.getTribunal() + "\n" +
                "🖥️ *Sistema:* " + requestProcesso.getSistema() + "\n\n" +
                "*Última Movimentação:*\n" +
                "🔍 *Tipo:* " + movimentoProcesso.nome() + "\n" +
                "🕒 *Data e Hora:* " + dataHoraFormatada + "\n" +
                "⏳ *Horas desde a Última Movimentação:* " + horasDesdeUltimoMovimento + " horas\n\n" +
                "⚖️ Por favor, verifique os detalhes no sistema.";
    }

    public String mensagemBodyPassivaIdentificado(Processo requestProcesso, User adv) {
        Movimento movimentoProcesso = requestProcesso.getMovimentos().get(0);
        String dataHoraFormatada = movimentoProcesso.dataHora().format(formatter);
        LocalDateTime agora = LocalDateTime.now();
        long horasDesdeUltimoMovimento = ChronoUnit.HOURS.between(movimentoProcesso.dataHora(), agora);

        String advogadoNome = adv.nome();

        return  "*ℹ️ Segue a última movimentação de seu processo:*\n\n" +
                "Olá, *" + advogadoNome.toUpperCase() + "*!\n" +
                "👥 *Partes:* " + requestProcesso.getPartesEnvolvidas() + "\n" +
                "📄 *Processo:* " + requestProcesso.getNumeroProcesso() + "\n" +
                "🏛️ *Tribunal:* " + requestProcesso.getTribunal() + "\n" +
                "🖥️ *Sistema:* " + requestProcesso.getSistema() + "\n\n" +
                "*Última Movimentação:*\n" +
                "🔍 *Tipo:* " + movimentoProcesso.nome() + "\n" +
                "🕒 *Data e Hora:* " + dataHoraFormatada + "\n" +
                "⏳ *Horas desde a Última Movimentação:* " + horasDesdeUltimoMovimento + " horas\n\n" +
                "⚖️ Por favor, verifique os detalhes no sistema.";
    }
}





