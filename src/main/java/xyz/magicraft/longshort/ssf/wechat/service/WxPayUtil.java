package xyz.magicraft.longshort.ssf.wechat.service;

import java.io.IOException;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request.Payer;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;

import lombok.extern.slf4j.Slf4j;
import xyz.magicraft.longshort.ssf.wechat.WxPayProperties;

@Component
@Slf4j
public class WxPayUtil {
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private WxPayProperties wxPayProperties;

    /**
     * 统一支付下单接口
     *
     * @param outTradeNo     商户订单号
     * @param totalFee       下单金额(单位:分)
     * @param productContent 商品描述
     */
    public Map<String, String> createOrder(String outTradeNo, Integer totalFee, String productContent,String openid) {
    	
    	Payer payer = new Payer();
    	payer.setOpenid(openid);
    	
    	
        WxPayUnifiedOrderV3Request wxPayUnifiedOrderV3Request = new WxPayUnifiedOrderV3Request();
        wxPayUnifiedOrderV3Request.setDescription(productContent);
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(totalFee);
        wxPayUnifiedOrderV3Request.setAmount(amount);
        
        System.out.println("pay-order util: " + outTradeNo);
        
        wxPayUnifiedOrderV3Request.setOutTradeNo(outTradeNo);
        wxPayUnifiedOrderV3Request.setNotifyUrl(wxPayProperties.getReturnUrl());
        wxPayUnifiedOrderV3Request.setPayer(payer);
        try {
            WxPayUnifiedOrderV3Result.JsapiResult jsapiResult = wxPayService.createOrderV3(TradeTypeEnum.JSAPI, wxPayUnifiedOrderV3Request);
            return this.startWXPay(jsapiResult);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 商户订单号查询订单  微信官方文档: https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_2.shtml
     *
     * @param outTradeNo     商户订单号
     * @return  WxPayOrderQueryV3Result 返回对象
     */
    public WxPayOrderQueryV3Result selectOrder(String outTradeNo) {
        WxPayOrderQueryV3Request wxPayOrderQueryV3Request = new WxPayOrderQueryV3Request();
        wxPayOrderQueryV3Request.setOutTradeNo(outTradeNo);
        try {
            return wxPayService.queryOrderV3(wxPayOrderQueryV3Request);
        } catch (WxPayException e) {
           throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 退款调用
     *
     * @param outTradeNo     商户订单号
     * @param outRefundNo    商户退款单号
     * @param reason    退款原因
     * @param refund    退款金额(单位:分)
     * @param total    原订单金额(单位:分)
     * @return WxPayRefundV3Result 返回值参考: https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_9.shtml
     */
    public WxPayRefundV3Result refundOrder(String outTradeNo, String outRefundNo, String reason,Integer refund,Integer total) {
        WxPayRefundV3Request wxPayRefundV3Request = new WxPayRefundV3Request();
        wxPayRefundV3Request.setOutTradeNo(outTradeNo);
        wxPayRefundV3Request.setOutRefundNo(outRefundNo);
        wxPayRefundV3Request.setReason(reason);
        wxPayRefundV3Request.setNotifyUrl(wxPayProperties.getRefundUrl());
        WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount();
        amount.setRefund(refund);
        amount.setTotal(total);
        amount.setCurrency("CNY");
        wxPayRefundV3Request.setAmount(amount);
        try {
            return  wxPayService.refundV3(wxPayRefundV3Request);
        } catch (WxPayException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 封装二次加签数据
     *
     * @param jsapiResult 预下单数据
     * @return Map 返回值
     */
    public Map<String, String> startWXPay(WxPayUnifiedOrderV3Result.JsapiResult jsapiResult) {
        Map<String, String> map = new HashMap<>();
        //时间戳
        long timestamp = System.currentTimeMillis() / 1000;
        String token = getSign(jsapiResult.getNonceStr(), jsapiResult.getAppId(), jsapiResult.getPackageValue(), timestamp);
        map.put("appid", jsapiResult.getAppId());
        map.put("package", jsapiResult.getPackageValue());
        map.put("noncestr", jsapiResult.getNonceStr());
        map.put("timestamp", String.valueOf(timestamp));
        map.put("sign", token);
        return map;
    }

    /**
     * 获取签名
     *
     * @param nonceStr  随机数
     * @param appId     微信公众号或者小程序等的appid
     * @param prepay_id 预支付交易会话ID
     * @param timestamp 时间戳 10位
     * @return String 新签名
     */
    String getSign(String nonceStr, String appId, String packagestr, long timestamp) {

        //从下往上依次生成
        String message = buildMessage(appId, timestamp, nonceStr, packagestr);
        //签名
        try {
            return sign(message.getBytes("utf-8"));
        } catch (IOException e) {
            throw new RuntimeException("签名异常,请检查参数或商户私钥");
        }
    }

    String sign(byte[] message) {
        try {
            //签名方式
            Signature sign = Signature.getInstance("SHA256withRSA");
            //私钥，通过MyPrivateKey来获取，这是个静态类可以接调用方法 ，需要的是_key.pem文件的绝对路径配上文件名
            sign.initSign(wxPayService.getConfig().getPrivateKey());
            sign.update(message);
            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (Exception e) {
            throw new RuntimeException("签名异常,请检查参数或商户私钥");
        }
    }

    /**
     * 按照前端签名文档规范进行排序，\n是换行
     *
     * @param nonceStr  随机数
     * @param appId     微信公众号或者小程序等的appid
     * @param prepay_id 预支付交易会话ID
     * @param timestamp 时间戳 10位
     * @return String 新签名
     */
    String buildMessage(String appId, long timestamp, String nonceStr, String packagestr) {
        return appId + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + packagestr + "\n";
    }
    
   public  WxPayNotifyV3Result payNotify(String notifyData, SignatureHeader header) {
    	try {
			return wxPayService.parseOrderNotifyV3Result(notifyData, header);
		} catch (WxPayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
    }
   
   
  public  WxPayRefundNotifyV3Result refundNotify(String notifyData, SignatureHeader header) {
   	try {
			return wxPayService.parseRefundNotifyV3Result(notifyData, header);
		} catch (WxPayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
   }
}
