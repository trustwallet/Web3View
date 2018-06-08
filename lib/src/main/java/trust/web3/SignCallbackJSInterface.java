package trust.web3;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import java.math.BigInteger;

import trust.core.entity.Address;
import trust.core.entity.Message;
import trust.core.entity.Transaction;
import trust.core.util.Hex;

public class SignCallbackJSInterface {

    @NonNull
    private final OnSignTransactionListener onSignTransactionListener;
    @NonNull
    private final OnSignMessageListener onSignMessageListener;
    @NonNull
    private final OnSignPersonalMessageListener onSignPersonalMessageListener;

    public SignCallbackJSInterface(
            @NonNull OnSignTransactionListener onSignTransactionListener,
            @NonNull OnSignMessageListener onSignMessageListener,
            @NonNull OnSignPersonalMessageListener onSignPersonalMessageListener) {
        this.onSignTransactionListener = onSignTransactionListener;
        this.onSignMessageListener = onSignMessageListener;
        this.onSignPersonalMessageListener = onSignPersonalMessageListener;
    }

    @JavascriptInterface
    public void signTransaction(
            int callbackId,
            String recipient,
            String value,
            String nonce,
            String gasLimit,
            String gasPrice,
            String payload) {
        Transaction transaction = new Transaction(
                TextUtils.isEmpty(recipient) ? Address.EMPTY : new Address(recipient),
                null,
                Hex.hexToBigInteger(value),
                Hex.hexToBigInteger(gasPrice, BigInteger.ZERO),
                Hex.hexToLong(gasLimit, 0),
                Hex.hexToLong(nonce, -1),
                payload,
                callbackId);
        onSignTransactionListener.onSignTransaction(transaction);

    }

    @JavascriptInterface
    public void signMessage(int callbackId, String data) {
        onSignMessageListener.onSignMessage(new Message(data, false, callbackId));
    }

    @JavascriptInterface
    public void signPersonalMessage(int callbackId, String data) {
        onSignPersonalMessageListener.onSignPersonalMessage(new Message(data, true, callbackId));
    }
}
