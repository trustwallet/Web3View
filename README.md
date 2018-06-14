# Web3View

[![](https://jitpack.io/v/TrustWallet/Web3View.svg)](https://jitpack.io/#TrustWallet/Web3View)

### Usage ([sample](https://github.com/TrustWallet/Web3View/tree/master/app))
Add dependency:

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```gradle
dependencies {
    implementation 'com.github.TrustWallet:Web3View:0.02'
}
```
Add internet permission to AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
Define a view in your layout file:
```xml
<?xml version="1.0" encoding="utf-8"?>
...
    <trust.web3.Web3View
        android:id="@+id/web3view"
        android:layout_below="@+id/go"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />
...
```
And add following code to your activity or fragment for setup:
Java
```java
web3.setChainId(1);
web3.setRpcUrl("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk");
web3.setWalletAddress(new Address("0xaa3cc54d7f10fa3a1737e4997ba27c34f330ce16"));
```
Add listeners:

```java
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
```
Send results:

```java
web3.onSignCancel(Message|Tranasction)
web3.onSignMessageSuccessful(message, "0x....");
web3.onSignPersonalMessageSuccessful(message, "0x...");
web3.onSignTransactionSuccessful(transaction, "0x...");
web3.onSignError(Message|Transaction, "some_error");
```
