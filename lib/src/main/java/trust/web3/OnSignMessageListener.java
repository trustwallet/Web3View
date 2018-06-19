package trust.web3;

import trust.core.entity.Message;

public interface OnSignMessageListener {
    void onSignMessage(Message<String> message);
}
