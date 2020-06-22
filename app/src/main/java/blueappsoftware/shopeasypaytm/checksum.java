package blueappsoftware.shopeasypaytm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by kamal_bunkar on 02-02-2018.
 */

public class checksum extends AppCompatActivity  implements PaytmPaymentTransactionCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
        dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(checksum.this);

        private String orderId , mid, custid, amt;
        String url ="http://paytmhyperlocal.unaux.com/paytm/generateChecksum.php";
        String varifyurl = // "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=<ORDER_ID>"; //
                             "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";//
        String CHECKSUMHASH ="";

                @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();

           // initOrderId();
            orderId ="KK100349";  // NOTE :  order id must be unique
            mid = "nwbmTX04755943663258";  // CREATI42545355156573
            custid = "KKCUST0345";

        }

        protected String doInBackground(ArrayList<String>... alldata) {

             String  url ="http://paytmhyperlocal.unaux.com/paytm/generateChecksum.php";

            JSONParser jsonParser = new JSONParser(checksum.this);
            String  param=
                    "MID="+mid+
                    "&ORDER_ID=" + orderId+
                    "&CUST_ID="+custid+
                    "&CHANNEL_ID=WAP&TXN_AMOUNT=100&WEBSITE=APPSTAGING"+"&CALLBACK_URL="+ varifyurl+"&INDUSTRY_TYPE_ID=Retail";

            Log.e("checksum"," param string "+param );



            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);
            // yaha per checksum ke saht order id or status receive hoga..
            Log.e("CheckSum result >>",jsonObject.toString());
            if(jsonObject != null){
                Log.e("CheckSum result >>",jsonObject.toString());
                try {

                    CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                    Log.e("CheckSum result >>",CHECKSUMHASH);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            // jab run kroge to yaha checksum dekhega
            ///ab service ko call krna hai
            Log.e(" setup acc ","  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            PaytmPGService Service =PaytmPGService.getStagingService();
           // when app is ready to publish use production service
            // PaytmPGService  Service = PaytmPGService.getProductionService();

            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            Map<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            // ye sari valeu same hon achaiye

            //MID provided by paytm
            paramMap.put("MID", mid);
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", "100");
            paramMap.put("WEBSITE", "APPSTAGING");
            paramMap.put("CALLBACK_URL" ,varifyurl);
            //paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
           // paramMap.put( "MOBILE_NO" , "9144040888");  // no need
            paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");

            PaytmOrder Order = new PaytmOrder(paramMap);

            Log.e("checksum ", paramMap.toString());


            Service.initialize(Order,null);
            // start payment service call here
            Service.startPaymentTransaction(checksum.this, true, true, checksum.this  );


        }

    }


    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum ", " respon true " + bundle.toString());
    }

    @Override
    public void networkNotAvailable() {

    }

    @Override
    public void clientAuthenticationFailed(String s) {

    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  "+ s );
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true "+ s + "  s1 " + s1);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back respon  " );
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel " );
    }


}
