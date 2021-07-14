package com.wangli.gulimall.product;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangli.gulimall.product.dao.AttrGroupDao;
import com.wangli.gulimall.product.dao.SkuSaleAttrValueDao;
import com.wangli.gulimall.product.entity.BrandEntity;
import com.wangli.gulimall.product.service.BrandService;
import com.wangli.gulimall.product.vo.web.SkuItemSaleAttrVo;
import com.wangli.gulimall.product.vo.web.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    //    @Autowired
//    OSSClient ossClient;
//

    @Test
    public void test() {
        List<SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrGroupDao.getAttrGroupWithAttrsBySpuId(30L, 225L);
        attrGroupWithAttrsBySpuId.forEach(System.out::println);
    }

    @Test
    public void test2() {
        List<SkuItemSaleAttrVo> saleAttrBySpuId = skuSaleAttrValueDao.getSaleAttrBySpuId(3L);
        System.out.println(saleAttrBySpuId);
    }

    @Test
    public void testRedisson() {
        log.info("创建出的redissonClient：{}", redissonClient);
    }

    @Test
    public void testRedis() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("hello", "Wolrd_" + UUID.randomUUID().toString());

        String hello = ops.get("hello");
        log.info("之前设置到redis中的值为：{}", hello);
    }


//    @Test
//    public void testUpload1() throws FileNotFoundException {
//
//        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
//        InputStream inputStream = new FileInputStream("C:\\Users\\LeeNow\\Desktop\\xiaomi.png");
//        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
//        ossClient.putObject("leenow", "xx1.jpg", inputStream);
//
//        // 关闭OSSClient。
//        ossClient.shutdown();
//        System.out.println("上传完成1...");
//    }
//
//    @Test
//    public void testUpload() throws FileNotFoundException {
//        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
//        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
//        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//        String accessKeyId = "xxx";
//        String accessKeySecret = "xxx";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
//        InputStream inputStream = new FileInputStream("C:\\Users\\LeeNow\\Desktop\\xiaomi.png");
//        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
//        ossClient.putObject("leenow", "xiaomi.png", inputStream);
//
//        // 关闭OSSClient。
//        ossClient.shutdown();
//        System.out.println("上传完成...");
//    }

    @Test
    public void save() {
        BrandEntity entity = new BrandEntity();
        entity.setName("小米");
        brandService.save(entity);
        System.out.println("保存成功");
    }

    @Test
    public void update() {
        BrandEntity entity = new BrandEntity();
        entity.setBrandId(1L);
        entity.setDescript("hello");
        brandService.updateById(entity);
        System.out.println("更新成功");
    }

    @Test
    public void search() {
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 5L));
        list.forEach(System.out::println);
    }

}
