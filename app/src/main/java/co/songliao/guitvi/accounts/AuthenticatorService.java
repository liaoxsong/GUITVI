package co.songliao.guitvi.accounts;

import android.content.Intent;
import android.os.IBinder;

import java.security.Provider;
import java.util.List;
import java.util.Map;

/**
 * Created by Song on 1/26/15.
 */
public class AuthenticatorService extends Provider.Service {

    private Authenticator mAuthenticator;


    public AuthenticatorService(Provider provider, String type, String algorithm, String className, List<String> aliases, Map<String, String> attributes) {
        super(provider, type, algorithm, className, aliases, attributes);
    }


    public IBinder onBind(Intent intent){
        return mAuthenticator.getIBinder();
    }


}
