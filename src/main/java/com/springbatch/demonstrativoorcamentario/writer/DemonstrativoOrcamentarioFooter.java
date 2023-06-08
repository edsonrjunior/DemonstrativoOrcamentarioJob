package com.springbatch.demonstrativoorcamentario.writer;

import com.springbatch.demonstrativoorcamentario.dominio.GrupoLancamento;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

@Component
public class DemonstrativoOrcamentarioFooter implements FlatFileFooterCallback {
    double totalGeral = 0.0;

    @Override
    public void writeFooter(Writer writer) throws IOException {


        writer.append("\n");
        writer.append(String.format("\t\t\t\t\t\t\t  Total: %s", NumberFormat.getCurrencyInstance().format(totalGeral)));
        writer.append(String.format("\t\t\t\t\t\t\t  Código de Autenticação: %s", UUID.randomUUID()));
    }

    //Isso é um listener, uma forma de capturar os eventos para processa-los
    //Necessário add um listener no step registrando-o
    @BeforeWrite
    public void beforeWrite(List<GrupoLancamento> grupos) {
        for (GrupoLancamento grupoLancamento : grupos) {
            totalGeral += grupoLancamento.getTotal();

        }
    }

}
