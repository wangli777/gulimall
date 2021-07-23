package com.wangli.gulimall.auth;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class GulimallAuthServerApplicationTests {
	@Test
	public void test() {
		Integer time = 1627046260;
		Date date = new Date(1627046260 * 1000);
		Date date1 = new Date(1627046260 * 1000L);
		Date date2 = new Date(1627046260000L);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date2));

	}


	@Test
	public void contextLoads() {

	}

}
