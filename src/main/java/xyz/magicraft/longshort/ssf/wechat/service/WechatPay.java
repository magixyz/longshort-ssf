//package xyz.magicraft.longshort.ssf.wechat.service;
//
//import java.io.Serializable;
//
//import java.time.Instant;
//import java.time.format.DateTimeFormatter;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
//import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
//import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
//import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
//import com.github.binarywang.wxpay.constant.WxPayConstants;
//import com.github.binarywang.wxpay.exception.WxPayException;
//import com.github.binarywang.wxpay.service.WxPayService;
//
//import lombok.extern.slf4j.Slf4j;
//import xyz.magicraft.longshort.ssf.wechat.WxPayProperties;
//
//@Service
//@Slf4j
//public class WechatPay {
//
//    private static final String PATTERN_FORMAT = "yyyyMMddHHmmss";
//    
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
//    
//	@Value("${wx.pay.notifyUrl}")
//    private String notifyUrl;
//	
//    @Autowired
//    private WxPayProperties wxPayProperties;
//    
//	@Autowired
//    private WxPayService wxPayService;
//
//	public Serializable order(String orderNo, Integer fee, String openId) {
//        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
//        orderRequest.setSignType(WxPayConstants.SignType.MD5);
//        orderRequest.setBody("儿童车租用");
//        orderRequest.setOutTradeNo(orderNo); //自己生成order_No
//        orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
////        orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(yuanMoney));//直接分
//        orderRequest.setTotalFee(fee);//直接分
//        orderRequest.setOpenid(openId); // 获取微信支付用户的openId
////        orderRequest.setSpbillCreateIp(IPUtils.getIpAddr(request));
//        Instant now = Instant.now();
//        Instant afterDate = now.plusSeconds(10 * 60);//10分钟后
//        orderRequest.setTimeStart( formatter.format(now));
//        orderRequest.setTimeExpire(formatter.format(afterDate));
//        orderRequest.setNotifyUrl(notifyUrl);
//        try {
//			return wxPayService.createOrder(orderRequest);
//		} catch (WxPayException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
//}
