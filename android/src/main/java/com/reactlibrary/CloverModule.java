package com.reactlibrary;

import android.accounts.Account;

import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.clover.connector.sdk.v3.PaymentConnector;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.util.CloverAuth;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v1.merchant.MerchantConnector;
import com.clover.sdk.v1.merchant.Merchant;
import com.clover.sdk.v3.connector.IPaymentConnectorListener;
import com.clover.sdk.v3.remotepay.AuthResponse;
import com.clover.sdk.v3.remotepay.CapturePreAuthResponse;
import com.clover.sdk.v3.remotepay.CloseoutResponse;
import com.clover.sdk.v3.remotepay.ConfirmPaymentRequest;
import com.clover.sdk.v3.remotepay.ManualRefundResponse;
import com.clover.sdk.v3.remotepay.PreAuthResponse;
import com.clover.sdk.v3.remotepay.ReadCardDataResponse;
import com.clover.sdk.v3.remotepay.RefundPaymentRequest;
import com.clover.sdk.v3.remotepay.RefundPaymentResponse;
import com.clover.sdk.v3.remotepay.RetrievePaymentResponse;
import com.clover.sdk.v3.remotepay.RetrievePendingPaymentsResponse;
import com.clover.sdk.v3.remotepay.SaleResponse;
import com.clover.sdk.v3.remotepay.TipAdded;
import com.clover.sdk.v3.remotepay.TipAdjustAuthResponse;
import com.clover.sdk.v3.remotepay.VaultCardResponse;
import com.clover.sdk.v3.remotepay.VerifySignatureRequest;
import com.clover.sdk.v3.remotepay.VoidPaymentRefundResponse;
import com.clover.sdk.v3.remotepay.VoidPaymentRequest;
import com.clover.sdk.v3.remotepay.VoidPaymentResponse;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

public class CloverModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private Promise promise;
    private MerchantConnector merchantConnector;
    private Account account;

    public CloverModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "Clover";
    }

    @ReactMethod
    public void getAuthToken(final Promise promise) {
        this.promise = promise;
        account = CloverAccount.getAccount(reactContext);
        new CloverAuthTask().execute();
    }

    @ReactMethod
    public void getMerchant(final Promise promise) {
        this.promise = promise;
        account = CloverAccount.getAccount(reactContext);
        new CloverMerchantTask().execute();
    }

    @ReactMethod
    public void getDeviceId(final Promise promise) {
        this.promise = promise;
        account = CloverAccount.getAccount(reactContext);
        new CloverDeviceTask().execute();
    }

    @ReactMethod
    public void voidTransaction(ReadableMap options, final Promise promise) {
        try{
            String paymentId = options.getString("paymentId");
            String orderId = options.getString("orderId");
            String remoteApplicationId =  options.getString("remoteApplicationId");
            VoidPaymentRequest vpr = new VoidPaymentRequest();
            vpr.setPaymentId(paymentId);
            vpr.setOrderId(orderId);
            vpr.setVoidReason("USER_CANCEL");
            Log.d("Tag", "VoidPaymentRequest: " + vpr.toString());
            PaymentConnector connector = initializePaymentConnector(remoteApplicationId);
            connector.voidPayment(vpr);
        }catch (Exception e){
            promise.reject(e);
        }

    }
    @ReactMethod
    public void refundTransaction(ReadableMap options, final Promise promise) {
        //;"59F41AY81A3FT.XMA490395AY96"
        try{
            String paymentId = options.getString("paymentId");
            String orderId = options.getString("orderId");
            String remoteApplicationId =  options.getString("remoteApplicationId");
            RefundPaymentRequest refund = new RefundPaymentRequest();
            refund.setPaymentId(paymentId);
            refund.setOrderId(orderId);
            refund.setFullRefund(true);
            Log.d("Tag", "RefundPaymentRequest - Full: " + refund.toString());
            PaymentConnector connector = initializePaymentConnector(remoteApplicationId);
            connector.refundPayment(refund);
            promise.resolve(null);
        }catch (Exception e){
            promise.reject(e);
        }


    }
    private PaymentConnector initializePaymentConnector(String remoteApplicationId) {
        // Get the Clover account that will be used with the service; uses the GET_ACCOUNTS permission
        Account cloverAccount = CloverAccount.getAccount(reactContext);
        // Set your RAID as the remoteApplicationId

        //Implement the interface
        IPaymentConnectorListener paymentConnectorListener = new IPaymentConnectorListener() {
            @Override
            public void onSaleResponse(SaleResponse response) {
                String result;
                if (response.getSuccess()) {
                    result = "Sale was successful";
                } else {
                    result = "Sale was unsuccessful" + response.getReason() + ":" + response.getMessage();
                }
                Toast.makeText(reactContext, result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPreAuthResponse(PreAuthResponse response) {

            }

            @Override
            public void onAuthResponse(AuthResponse response) {

            }

            @Override
            public void onTipAdjustAuthResponse(TipAdjustAuthResponse response) {

            }

            @Override
            public void onCapturePreAuthResponse(CapturePreAuthResponse response) {

            }

            @Override
            public void onVerifySignatureRequest(VerifySignatureRequest request) {

            }

            @Override
            public void onConfirmPaymentRequest(ConfirmPaymentRequest request) {

            }

            @Override
            public void onManualRefundResponse(ManualRefundResponse response) {

            }

            @Override
            public void onRefundPaymentResponse(RefundPaymentResponse response) {

            }

            @Override
            public void onTipAdded(TipAdded tipAdded) {

            }

            @Override
            public void onVoidPaymentResponse(VoidPaymentResponse response) {

            }

            @Override
            public void onVaultCardResponse(VaultCardResponse response) {

            }

            @Override
            public void onRetrievePendingPaymentsResponse(RetrievePendingPaymentsResponse retrievePendingPaymentResponse) {

            }

            @Override
            public void onReadCardDataResponse(ReadCardDataResponse response) {

            }

            @Override
            public void onCloseoutResponse(CloseoutResponse response) {

            }

            @Override
            public void onRetrievePaymentResponse(RetrievePaymentResponse response) {

            }

            @Override
            public void onVoidPaymentRefundResponse(VoidPaymentRefundResponse response) {

            }

            @Override
            public void onDeviceDisconnected() {
                Toast.makeText(reactContext, "Device Disconnected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDeviceConnected() {
                Toast.makeText(reactContext, "Device connected", Toast.LENGTH_LONG).show();
            }
        };
        return new PaymentConnector(reactContext, cloverAccount, paymentConnectorListener, remoteApplicationId);
    }

    private class CloverAuthTask extends AsyncTask<Void, Void, CloverAuth.AuthResult> {

        @Override
        protected final CloverAuth.AuthResult doInBackground(Void... params) {
            try {
                return CloverAuth.authenticate(reactContext);
            } catch (Exception e) {
                promise.reject(e);

            }
            return null;
        }

        @Override
        protected final void onPostExecute(CloverAuth.AuthResult item) {
            if (item != null) {
                WritableMap resultMap = Arguments.createMap();
                resultMap.putString("token", item.authToken);
                promise.resolve(resultMap);
            }
        }
    }

    private void connect() {
        disconnect();
        if (account != null) {
            merchantConnector = new MerchantConnector(reactContext, account, null);
            merchantConnector.connect();
        }
    }

    private void disconnect() {
        if (merchantConnector != null) {
            merchantConnector.disconnect();
            merchantConnector = null;
        }
    }

    public class CloverMerchantTask extends AsyncTask<Void, Void, Merchant> {
        @Override
        protected Merchant doInBackground(Void... voids) {
            if (account == null) return null;
            connect();
            Merchant merchant = null;
            try {
                merchant = merchantConnector.getMerchant();
            } catch (RemoteException e) {
                e.printStackTrace();
                promise.reject(e);
            } catch (ClientException e) {
                e.printStackTrace();
                promise.reject(e);
            } catch (ServiceException e) {
                e.printStackTrace();
                promise.reject(e);
            } catch (BindingException e) {
                e.printStackTrace();
                promise.reject(e);
            }
            return merchant;

        }

        @Override
        protected void onPostExecute(Merchant merchant) {
            super.onPostExecute(merchant);
            if (merchant != null) {
                WritableMap resultMap = Arguments.createMap();
                resultMap.putString("Mid", merchant.getMid());
                resultMap.putString("cloverId", merchant.getId());
                promise.resolve(resultMap);
            }
        }
    }

    public class CloverDeviceTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            if (account == null) return null;
            connect();
            String deviceId = null;
            try {
                deviceId = merchantConnector.getMerchant().getDeviceId();
            } catch (Exception e) {
                promise.reject(e);
            }
            return deviceId;

        }

        @Override
        protected void onPostExecute(String device) {
            super.onPostExecute(device);
            if (device != null) {
                WritableMap resultMap = Arguments.createMap();
                resultMap.putString("deviceId", device);
                promise.resolve(resultMap);
            } else {
                promise.reject(new Exception("Device is null"));
            }
        }
    }
}
