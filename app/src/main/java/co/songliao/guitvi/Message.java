package co.songliao.guitvi;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Song on 1/16/15.
 */
public class Message {

    public static void message(Context context,String message){

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}