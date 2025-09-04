package com.baizhanshopping.shopping_order_customer_api.controller;

import com.alibaba.fastjson2.JSON;
import com.baizhanshopping.shopping_common.pojo.Orders;
import com.baizhanshopping.shopping_common.pojo.Payment;
import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.OrderService;
import com.baizhanshopping.shopping_common.service.ZfbPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/payment")
public class PaymentController {
    @DubboReference(timeout = 5000)
    private ZfbPayService zfbPayService;
    @DubboReference
    private OrderService orderService;
    @PostMapping("/pcPay")
    public BaseRsult<String> pcPay(String orderId){
        Orders orders = orderService.findById(orderId);
        String codeUrl = zfbPayService.pcPay(orders);
        return BaseRsult.success(codeUrl);
    }
    /**
     * 该方法是用户扫码支付后支付宝调用的。
     * @return
     */
    @PostMapping("/success/notify")
    public BaseRsult successNotify(HttpServletRequest request){
        Map<String,Object> paramMap=new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String,String[]> newMap=new HashMap<>();
        newMap.putAll(parameterMap);
        paramMap.put("requestParameteMap",newMap);
        zfbPayService.checkSign(paramMap);
        String trade_status = request.getParameter("trade_status");// 订单状态
        String out_trade_no = request.getParameter("out_trade_no");// 订单编号
        if(trade_status.equals("TRADE_SUCCESS")){
            Orders orders = orderService.findById(out_trade_no);
            orders.setStatus(2);
            orders.setPaymentTime(new Date());
            orders.setPaymentType(2);
            orderService.update(orders);

            // 3.添加交易记录
            Payment payment = new Payment();
            payment.setOrderId(out_trade_no); // 订单编号
            payment.setTransactionId(out_trade_no); // 交易号
            payment.setTradeType("支付宝支付"); // 交易类型
            payment.setTradeState(trade_status); // 交易状态
            payment.setPayerTotal(orders.getPayment()); // 付款数
            payment.setContent(JSON.toJSONString(request.getParameterMap())); // 支付详情
            payment.setCreateTime(new Date()); // 支付时间
            zfbPayService.addPayment(payment);
        }
        return BaseRsult.success();
    }
}
