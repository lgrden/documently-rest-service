package io.wegetit.documently.config;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.mongodb.lang.NonNull;

@Configuration
public class MongoConfig {

	@Bean
	public MongoCustomConversions mongoCustomConversions() {
		List<Converter> converters = new ArrayList<>();
		converters.add(new Converter<LocalDate, Date>() {
			@Override
			public Date convert(@NonNull LocalDate source) {
				return new Date(source.atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
			}
		});
		converters.add(new Converter<Date, LocalDate>() {
			@Override
			public LocalDate convert(@NonNull Date source) {
				return source.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
			}
		});
		return new MongoCustomConversions(converters);	}
}
