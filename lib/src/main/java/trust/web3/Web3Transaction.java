package trust.web3;

import java.math.BigInteger;

import trust.core.entity.Address;
import trust.core.entity.Transaction;

class Web3Transaction extends Transaction {
    final int callbackId;

    Web3Transaction(
            int callbackId,
            Address recipient,
            BigInteger value,
            BigInteger gasPrice,
            long gasLimit,
            long nonce,
            String payload) {
        super(recipient, null, value, gasPrice, gasLimit, nonce, payload);
        this.callbackId = callbackId;
    }
}
