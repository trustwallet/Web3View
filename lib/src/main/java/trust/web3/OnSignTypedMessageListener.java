package trust.web3;

import trust.core.entity.Message;
import trust.core.entity.TypedData;

public interface OnSignTypedMessageListener {
    void onSignTypedMessage(Message<TypedData[]> message);
}
