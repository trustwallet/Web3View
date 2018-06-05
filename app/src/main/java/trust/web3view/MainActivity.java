package trust.web3view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import trust.core.entity.Address;
import trust.web3.OnSignMessageListener;
import trust.web3.OnSignPersonalMessageListener;
import trust.web3.OnSignTransactionListener;
import trust.web3.Web3View;
import trust.core.entity.Message;
import trust.core.entity.Transaction;

public class MainActivity extends AppCompatActivity implements
        OnSignTransactionListener, OnSignPersonalMessageListener, OnSignMessageListener {

    private TextView url;
    private Web3View web3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = findViewById(R.id.url);
        web3 = findViewById(R.id.web3view);
        findViewById(R.id.go).setOnClickListener(v -> web3.loadUrl(url.getText().toString()));

        setupWeb3();

    }

    private void setupWeb3() {
        web3.setChainId(1);
        web3.setRpcUrl("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk");
        web3.setWalletAddress(new Address("0xaa3cc54d7f10fa3a1737e4997ba27c34f330ce16"));

        web3.setOnSignMessageListener(message -> {
            Toast.makeText(this, "Message: " + message.value, Toast.LENGTH_LONG).show();
            web3.onSignCancel(message);
        });
        web3.setOnSignPersonalMessageListener(message -> {
            Toast.makeText(this, "Personal message: " + message.value, Toast.LENGTH_LONG).show();
            web3.onSignCancel(message);
        });
        web3.setOnSignTransactionListener(transaction -> {
            Toast.makeText(this, "Transaction: " + transaction.value, Toast.LENGTH_LONG).show();
            web3.onSignCancel(transaction);
        });
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
}
