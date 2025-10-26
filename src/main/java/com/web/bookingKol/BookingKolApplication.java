package com.web.bookingKol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookingKolApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingKolApplication.class, args);
	}

}
