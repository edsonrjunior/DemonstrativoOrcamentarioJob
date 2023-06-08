package com.springbatch.demonstrativoorcamentario.reader;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.springbatch.demonstrativoorcamentario.dominio.GrupoLancamento;
import com.springbatch.demonstrativoorcamentario.dominio.Lancamento;

@Configuration
public class BancoLancamentoReaderConfig {
	@Bean
	public JdbcCursorItemReader<GrupoLancamento> bancoLancamentoReader(
			@Qualifier("appDataSource") DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<GrupoLancamento>()
				.name("bancoLancamentoReader")
				.dataSource(dataSource)
				.sql("select * from lancamento")
				.rowMapper(rowMapper())
				.build();
	}

	private RowMapper<GrupoLancamento> rowMapper() {
		return new RowMapper<GrupoLancamento>() {
			@Override
			public GrupoLancamento mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				GrupoLancamento grupo = new GrupoLancamento();
				grupo.setCodigoNaturezaDespesa(resultSet.getInt("codigoNaturezaDespesa"));
				grupo.setDescricaoNaturezaDespesa(resultSet.getString("descricaoNaturezaDespesa"));
				grupo.setLancamentoTmp(new Lancamento());
				grupo.getLancamentoTmp().setData(resultSet.getDate("dataLancamento"));
				grupo.getLancamentoTmp().setDescricao(resultSet.getString("descricaoLancamento"));
				grupo.getLancamentoTmp().setValor(resultSet.getDouble("valorLancamento"));
				return grupo;
			}
		};
	}
}
