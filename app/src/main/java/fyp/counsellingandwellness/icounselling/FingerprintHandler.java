package fyp.counsellingandwellness.icounselling;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import fyp.counsellingandwellness.icounselling.activities.LoginActivity;

/*  Implement FingerprintActivity Authentication Callback to get access to FingerprintActivity methods  */
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;
    private TextView errorText;

    public FingerprintHandler(Context mContext,TextView errorText) {
        context = mContext;
        this.errorText=errorText;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    /*  Method will call on FingerprintActivity Auth Error  */
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint authentication error\n" + errString);
    }

    /*  Method will call on FingerprintActivity Auth have some help  */
    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Authentication help\n" + helpString);
    }

    /*  Method will call on FingerprintActivity Auth Failed  */
    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint authentication failed. Please try again.");
    }

    /*  Method will call on FingerprintActivity Auth Succeeded  */
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        Log.d("Authentication", "Fingerprint authentication successful.");
        onAuthSuccess();
    }

    /*  Trigger this method on FingerPrint get Success  */
    private void onAuthSuccess() {
        String fpAccess = "granted";
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("fpAccess",fpAccess);
        context.startActivity(intent);
        //context.startActivity(new Intent(context, LoginActivity.class));
        ((AppCompatActivity) context).finish();
    }

    /*  Method to update Error text message on Auth failed  */
    public void update(String e) {
        errorText.setText(e);
    }
}