package com.springbatch.demonstrativoorcamentario.writer;

import com.springbatch.demonstrativoorcamentario.dominio.GrupoLancamento;
import com.springbatch.demonstrativoorcamentario.dominio.Lancamento;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Configuration
public class DemonstrativoOrcamentarioWriterConfig {
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String GRUPO_LANCAMENTO_FORMAT = "[%d] %s - %s\n";
    public static final String LANCAMENTO_FORMAT = "\t [%s] %s - %s\n";
    //    @Bean
//    public ItemWriter<GrupoLancamento> demonstrativoOrcamentarioWriter() {
//        return itens -> {
//            System.out.println("\n");
//            System.out.println(String.format("SISTEMA INTEGRADO: XPTO \t\t\t\t DATA: %s", new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
//            System.out.println(String.format("MÓDULO: ORÇAMENTO \t\t\t\t\t HORA: %s", new SimpleDateFormat("HH:MM").format(new Date())));
//            System.out.println(String.format("\t\t\tDEMONSTRATIVO ORCAMENTARIO"));
//            System.out.println(String.format("----------------------------------------------------------------------------"));
//            System.out.println(String.format("CODIGO NOME VALOR"));
//            System.out.println(String.format("\t Data Descricao Valor"));
//            System.out.println(String.format("----------------------------------------------------------------------------"));
//
//            double totalGeral = 0.0;
//
//            for (GrupoLancamento grupo : itens) {
//                System.out.println(String.format("[%d] %s - %s", grupo.getCodigoNaturezaDespesa(), grupo.getDescricaoNaturezaDespesa(), NumberFormat.getCurrencyInstance().format(grupo.getTotal())));
//
//                totalGeral += grupo.getTotal();
//
//                for (Lancamento lancamento : grupo.getLancamentos()) {
//                    System.out.println(String.format("\t [%s] %s - %s", new SimpleDateFormat("dd/MM/yyyy").format(lancamento.getData()), lancamento.getDescricao(), NumberFormat.getCurrencyInstance().format(lancamento.getValor())));
//                }
//            }
//
//            System.out.println("\n");
//            System.out.println(String.format("\t\t\t\t\t\t\t  Total: %s", NumberFormat.getCurrencyInstance().format(totalGeral)));
//            System.out.println(String.format("\t\t\t\t\t\t\t  Código de Autenticação: %s", UUID.randomUUID()));
//        };
//    }


    @StepScope
    @Bean
    public FlatFileItemWriter<GrupoLancamento> demonstrativoOrcamentarioWriter
            (@Value("#{jobParameters['demonstrativoOrcamentario']}") Resource resource, DemonstrativoOrcamentarioFooter footerCallBack) {
        return new FlatFileItemWriterBuilder<GrupoLancamento>()
                .name("demonstrativoOrcamentarioWriter")
                .resource(resource)
                .lineAggregator(lineAggregator())
                .headerCallback(cabecalhoCallBack())
                .footerCallback(footerCallBack)
                .build();
    }

    private FlatFileHeaderCallback cabecalhoCallBack() {
        return writer -> {
            writer.append("\n");
            writer.append(String.format("SISTEMA INTEGRADO: XPTO \t\t\t\t DATA: %s\n", new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
            writer.append(String.format("MÓDULO: ORÇAMENTO \t\t\t\t\t HORA: %s\n", new SimpleDateFormat("HH:MM").format(new Date())));
            writer.append(String.format("\t\t\tDEMONSTRATIVO ORCAMENTARIO\n"));
            writer.append(String.format("----------------------------------------------------------------------------\n"));
            writer.append(String.format("CODIGO NOME VALOR\n"));
            writer.append(String.format("\t Data Descricao Valor\n"));
            writer.append(String.format("----------------------------------------------------------------------------\n"));
        };
    }

    private LineAggregator<GrupoLancamento> lineAggregator() {
        return grupoLancamento -> {

            //Para cada grupoLancamento, escreva concatenando CodigoNaturezaDespesa, DescricaoNaturezaDespesa e o total do grupo lancamento
            String formatGrupoLancamento = String.format(
                    GRUPO_LANCAMENTO_FORMAT,
                    grupoLancamento.getCodigoNaturezaDespesa(),
                    grupoLancamento.getDescricaoNaturezaDespesa(),
                    NumberFormat.getCurrencyInstance().format(grupoLancamento.getTotal()));

            StringBuilder stringBuilder = new StringBuilder();

            //Para cada lançamento, escrever em uma linha concatenando data, descrição e valor
            for (Lancamento lancamento : grupoLancamento.getLancamentos()) {
                stringBuilder.append(String.format(
                        LANCAMENTO_FORMAT,
                        new SimpleDateFormat(DD_MM_YYYY).format(lancamento.getData()),
                        lancamento.getDescricao(), NumberFormat.getCurrencyInstance().format(lancamento.getValor())));
            }


            String formatLancamento = stringBuilder.toString();
            return formatGrupoLancamento + formatLancamento;
        };
    }

}
