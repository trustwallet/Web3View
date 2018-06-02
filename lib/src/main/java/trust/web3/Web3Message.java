package trust.web3;

import trust.core.entity.Message;

class Web3Message extends Message {
    public final int callbackId;

    public Web3Message(int callbackId, String data) {
        super(data);
        this.callbackId = callbackId;
    }
}
