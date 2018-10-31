package com.example.android.trivialdrivesample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.View;

import com.google.pay.GooglePay;
import com.google.pay.OnGooglePayStatusListener;
import com.google.pay.OrderParam;
import com.google.pay.Purchase;
import com.google.pay.RandomString;

public class InappActivity extends Activity {
    GooglePay googlePay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inapp);
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApjWy+r9s6ncuh2l8OK59KrvySuTUQi5Zc1Sel/y2nVXh+7rEAVNV+Ndz75eJeT+mA3Y3uzRAfCuRR6lziyhE+5Jj330JtoWvi4SNJghVMSTs/uxK1B/Jg1GVUsYzC93QciBIEch22hCZWI93Gjq5UJ3OC5uy45YwIS4bYnjv2n7H37QSlfE1pzlNq8HktULpfD1lA6Sdc8NNRDl3c5OfUzIwYh6d2ErDjEa0EnIEksGBHlo3/zsgTwuG4Fm1DugNA/uQbvaps3tFSzc55afFWPuTtzVEVYqAvP2hJglklmmz0oZNWK8GYPg4iEeXlFWGSuWRT04zYVgJFj0LbJkGWQIDAQAB";
        googlePay.setIsAutoConsume(true);
        googlePay = new GooglePay(this, base64EncodedPublicKey, new OnGooglePayStatusListener() {

            /**
             * 初始化失败是，调用该方法。在调用购买时，需要对该参数进行判断
             * https://www.jianshu.com/p/87ffdb7bc439
             * 该文章有提到解决方法
             *
             * @param boo
             */
            @Override
            public void initFailed(boolean boo) {

            }

            @Override
            public void onGgStatus(int code) {

            }

            @Override
            public void onBuySuccess(OrderParam data) {
                //说明用户购买成功
                //TODO 如果设置setIsAutoConsume未false，不自动消耗。将购买订单上传到服务器，服务器到Google 服务器校验订单成功时，手动调用消耗方法
                if (googlePay != null) {
                    googlePay.consumeAsync(data.currBuyType, data.purchaseData, data.dataSignature);
                }
            }

            @Override
            public void onConsumeSuccess() {
                //说明用户消耗商品成功
            }

            @Override
            public void unConsumeAsync(Purchase purchase) {
                //如果设置setIsAutoConsume未false，用户上次有未消耗的商品，会自动回调该方法，此时上传服务器校验，如果校验成功，手动调用消耗方法
                if (googlePay != null) {
                    googlePay.consumeAsync(purchase.getItemType(), purchase.getOriginalJson(), purchase.getSignature());
                }
            }
        });

        findViewById(R.id.btn_inapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RandomString randomString = new RandomString(36);
                googlePay.buyGoods(InappActivity.this, "vip_1_month", randomString.nextString().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (googlePay == null) return;

        // Pass on the activity result to the helper for handling
        if (!googlePay.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (googlePay != null) {
            googlePay.DestoryQuote();
        }
    }
}