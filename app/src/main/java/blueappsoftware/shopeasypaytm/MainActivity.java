package blueappsoftware.shopeasypaytm;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This is the sample app which will make use of the PG SDK. This activity will
 * show the usage of Paytm PG SDK API's.
 **/

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button btn = (Button) findViewById(R.id.start_transaction);
      //  final EditText editText = (EditText) findViewById(R.id.edittextit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, checksum.class);
                startActivity(intent);
            }
        });
    }

    //This is to refresh the order id: Only for the Sample App's purpose.
    @Override
    protected void onStart(){
        super.onStart();
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

}