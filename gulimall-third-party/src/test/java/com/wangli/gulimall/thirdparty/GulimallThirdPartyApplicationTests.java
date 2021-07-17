package com.wangli.gulimall.thirdparty;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.wangli.gulimall.thirdparty.service.SmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GulimallThirdPartyApplicationTests {

	@Autowired
	OSS ossClient;
	@Autowired
	SmsService smsService;



	@Test
	public void testSendSms() {
		smsService.sendSms("18079960897","999999");
	}

	@Test
	public void testUpload1() throws FileNotFoundException {

		// 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
		InputStream inputStream = new FileInputStream("C:\\Users\\LeeNow\\Desktop\\xiaomi.png");
		// 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
		ossClient.putObject("leenow", "xx1.jpg", inputStream);

		// 关闭OSSClient。
		ossClient.shutdown();
		System.out.println("上传完成1...");
	}

	@Test
	public void testUpload() throws FileNotFoundException {
		// yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
		String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
		// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
		String accessKeyId = "xxx";
		String accessKeySecret = "xxx";

		// 创建OSSClient实例。
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

		// 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
		InputStream inputStream = new FileInputStream("C:\\Users\\LeeNow\\Desktop\\xiaomi.png");
		// 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
		ossClient.putObject("leenow", "xiaomi.png", inputStream);

		// 关闭OSSClient。
		ossClient.shutdown();
		System.out.println("上传完成...");
	}

	@Test
	public void contextLoads() {
	}

}
