package com.baizhanshopping.shopping_pay_service.service;

import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.baizhanshopping.shopping_common.pojo.Orders;
import com.baizhanshopping.shopping_common.pojo.Payment;
import com.baizhanshopping.shopping_common.result.BusException;
import com.baizhanshopping.shopping_common.result.CodeEnum;
import com.baizhanshopping.shopping_common.service.ZfbPayService;
import com.baizhanshopping.shopping_pay_service.ZfbPayConfig;
import com.baizhanshopping.shopping_pay_service.mapper.PaymentMapper;
import com.baizhanshopping.shopping_pay_service.util.ZfbVerifierUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
@Slf4j
@DubboService
public class ZfbPayServiceImpl implements ZfbPayService {
    @Autowired
    private ZfbPayConfig zfbPayConfig;
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private PaymentMapper paymentMapper;

    /**
     * 生成二维码连接的方法
     * @param orders 订单对象
     * @return
     */
    @Override
    public String pcPay(Orders orders) {
        if(orders.getStatus()!=1){
            throw new BusException(CodeEnum.ORDER_STATUS_ERROR);
        }
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(zfbPayConfig.getNotifyUrl()+zfbPayConfig.getPcNotify());
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orders.getId()); // 订单编号
        bizContent.put("total_amount", orders.getPayment()); // 订单金额
        bizContent.put("subject", orders.getCartGoods().get(0).getGoodsName()); //订单标题
        request.setBizContent(bizContent.toString());
        try {
            AlipayTradePrecreateResponse execute = alipayClient.execute(request);
            if (!execute.isSuccess()) {
                log.error("支付宝返回失败, code={}, msg={}, subCode={}, subMsg={}",
                        execute.getCode(), execute.getMsg(),
                        execute.getSubCode(), execute.getSubMsg());
            }
            return execute.getQrCode();
        } catch (AlipayApiException e) {
            throw new BusException(CodeEnum.QR_CODE_ERROR);
        }
    }

    /**
     * 验签方法
     * @param paramMap 支付相关参数
     */
    @Override
    public void checkSign(Map<String, Object> paramMap) {
        Map<String, String[]> requestParameterMap = (Map<String, String[]>) paramMap.get("requestParameteMap");
        boolean valid = ZfbVerifierUtils.isValid(requestParameterMap, zfbPayConfig.getPublicKey());
        if(!valid){
            throw new BusException(CodeEnum.CHECK_SIGN_ERROR);
        }
    }

    @Override
    public void addPayment(Payment payment) {
        paymentMapper.insert(payment);
    }
}
