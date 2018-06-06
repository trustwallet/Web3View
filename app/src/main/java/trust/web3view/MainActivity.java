package trust.web3view;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import trust.Call;
import trust.SignMessageRequest;
import trust.SignTransactionRequest;
import trust.Trust;
import trust.core.entity.Address;
import trust.core.entity.Message;
import trust.core.entity.Transaction;
import trust.web3.OnSignMessageListener;
import trust.web3.OnSignPersonalMessageListener;
import trust.web3.OnSignTransactionListener;
import trust.web3.Web3View;

public class MainActivity extends AppCompatActivity implements
        OnSignTransactionListener, OnSignPersonalMessageListener, OnSignMessageListener {

    private TextView url;
    private Web3View web3;
    private Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = findViewById(R.id.url);
        web3 = findViewById(R.id.web3view);
        findViewById(R.id.go).setOnClickListener(v -> web3.loadUrl(url.getText().toString()));

        setupWeb3();

        if (savedInstanceState != null && savedInstanceState.containsKey("sign_call")) {
            call = savedInstanceState.getParcelable("sign_call");
        }
    }

    private void setupWeb3() {
        web3.setChainId(1);
        web3.setRpcUrl("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk");
        web3.setWalletAddress(new Address("0xaa3cc54d7f10fa3a1737e4997ba27c34f330ce16"));

        web3.setOnSignMessageListener(message ->
                call = Trust.signMessage().message(message).call(this));
        web3.setOnSignPersonalMessageListener(message ->
                call = Trust.signMessage().message(message).call(this));
        web3.setOnSignTransactionListener(transaction ->
                call = Trust.signTransaction().transaction(transaction).call(this));
    }

    @Override
    public void onSignMessage(Message message) {
        Toast.makeText(this, message.value, Toast.LENGTH_LONG).show();
        web3.onSignCancel(message);
    }

    @Override
    public void onSignPersonalMessage(Message message) {
        Toast.makeText(this, message.value, Toast.LENGTH_LONG).show();
        web3.onSignCancel(message);
    }

    @Override
    public void onSignTransaction(Transaction transaction) {
        String str = new StringBuilder()
                .append(transaction.recipient == null ? "" : transaction.recipient.toString()).append(" : ")
                .append(transaction.contract == null ? "" : transaction.contract.toString()).append(" : ")
                .append(transaction.value.toString()).append(" : ")
                .append(transaction.gasPrice.toString()).append(" : ")
                .append(transaction.gasLimit).append(" : ")
                .append(transaction.nonce).append(" : ")
                .append(transaction.payload).append(" : ")
                .toString();
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        web3.onSignCancel(transaction);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (call != null) {
            call.onActivityResult(requestCode, resultCode, data).subscribe((request, signHex) -> {
                if (request instanceof SignMessageRequest) {
                    SignMessageRequest signMessageRequest = (SignMessageRequest) request;
                    Message message = signMessageRequest.body();
                    if (message.isPersonal) {
                        web3.onSignPersonalMessageSuccessful(message, signHex);
                    } else {
                        web3.onSignMessageSuccessful(message, signHex);
                    }
                } else if (request instanceof SignTransactionRequest) {
                    web3.onSignTransactionSuccessful(((SignTransactionRequest) request).body(), signHex);
                }
            }, (request, error) -> {
                switch (error) {
                    case Trust.ErrorCode.CANCELED: {
                        if (request instanceof SignMessageRequest) {
                            web3.onSignCancel((Message) request.body());
                        } else if (request instanceof SignTransactionRequest) {
                            web3.onSignCancel((Transaction) request.body());
                        }
                    } break;
                    default: {
                        if (request instanceof SignMessageRequest) {
                            web3.onSignError((Message) request.body(), "Some error");
                        } else if (request instanceof SignTransactionRequest) {
                            web3.onSignError((Transaction) request.body(), "Some error");
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("sign_call", call);
    }
}
