package com.wangli.gulimall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.wangli.common.utils.R;
import com.wangli.common.vo.resp.MemberRespVo;
import com.wangli.gulimall.order.feign.CartFeignService;
import com.wangli.gulimall.order.feign.MemberFeignService;
import com.wangli.gulimall.order.feign.WmsFeignService;
import com.wangli.gulimall.order.interceptor.LoginInterceptor;
import com.wangli.gulimall.order.vo.MemberAddressVo;
import com.wangli.gulimall.order.vo.OrderConfirmVo;
import com.wangli.gulimall.order.vo.OrderItemVo;
import com.wangli.gulimall.order.vo.SkuStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangli.common.utils.PageUtils;
import com.wangli.common.utils.Query;

import com.wangli.gulimall.order.dao.OrderDao;
import com.wangli.gulimall.order.entity.OrderEntity;
import com.wangli.gulimall.order.service.OrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private MemberFeignService memberFeignService;

    @Autowired
    private CartFeignService cartFeignService;

    @Autowired
    private WmsFeignService wmsFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 订单确认页返回需要用的数据
     * @return
     */
    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        //构建OrderConfirmVo
        OrderConfirmVo confirmVo = new OrderConfirmVo();

        //当前登录用户
        MemberRespVo memberRespVo = LoginInterceptor.LOGIN_USER.get();

        //获取当前线程请求头信息(解决Feign异步调用丢失请求头问题)
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        //异步获取并设置收件地址
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {

            //将请求request设置到当前线程中
            RequestContextHolder.setRequestAttributes(requestAttributes);


            List<MemberAddressVo> address = memberFeignService.getAddress(memberRespVo.getId());
            confirmVo.setMemberAddressVos(address);
        }, executor);

        //异步获取并设置当前用户购物车中选中的商品
        CompletableFuture<Void> cartFuture = CompletableFuture.runAsync(() -> {

            //将请求request设置到当前线程中
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //这里使用Feign远程调用，并没有传当前用户信息，需要购物车模块自动获取当前登录用户信息来查询其购物车
            //Feign远程调用要想被调用系统能感知到当前登录用户，需要给feign调用请求的请求头中设置上当前请求的请求头（主要是Cookie）
            //需要使用Feign拦截器
            List<OrderItemVo> cartItems = cartFeignService.getCurrentCartItems();
            confirmVo.setItems(cartItems);
        }, executor).thenRunAsync(()->{
            List<OrderItemVo> items = confirmVo.getItems();
            //获取全部商品的id
            List<Long> skuIds = items.stream()
                    .map((OrderItemVo::getSkuId))
                    .collect(Collectors.toList());

            //远程查询商品库存信息
            R skuHasStock = wmsFeignService.getSkuHasStock(skuIds);
            List<SkuStockVo> skuStockVos = skuHasStock.getData("data", new TypeReference<List<SkuStockVo>>() {});

            if (skuStockVos != null && skuStockVos.size() > 0) {
                //将skuStockVos集合转换为map
                Map<Long, Boolean> skuHasStockMap = skuStockVos.stream().collect(Collectors.toMap(SkuStockVo::getSkuId, SkuStockVo::getHasStock));
                confirmVo.setStocks(skuHasStockMap);
            }
        },executor);

        //3、查询用户积分
        Integer integration = memberRespVo.getIntegration();
        confirmVo.setIntegration(integration);

        //4、价格数据自动计算

        //TODO 5、防重令牌(防止表单重复提交)


        CompletableFuture.allOf(addressFuture, cartFuture).get();

        return confirmVo;
    }

}