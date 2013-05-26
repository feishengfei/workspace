package com.cookbook.launch_activity.uievent;

import com.cookbook.launch_activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class PhysicalKeyPressActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode) {
			case KeyEvent.KEYCODE_CAMERA:
				Toast.makeText(this, "Press Camera Button", 
						Toast.LENGTH_LONG).show();
                return true;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				Toast.makeText(this, "Press DPAD L Button", 
						Toast.LENGTH_LONG).show();
                return true;
			case KeyEvent.KEYCODE_VOLUME_UP:
				Toast.makeText(this, "Press Volume Up Button", 
						Toast.LENGTH_LONG).show();
                return true;
			case KeyEvent.KEYCODE_SEARCH:
                if(event.getRepeatCount() == 0) {
				Toast.makeText(this, "repeatCount() == 0", 
						Toast.LENGTH_SHORT).show();
                }
                return true;
			case KeyEvent.KEYCODE_BACK:
                if (/*android.os.Build.VERSION.SDK_INT
                		< android.os.Build.VERSION_CODES.ECLAIR*/
                	event.getRepeatCount() == 0) {
                    //onBackPressed();	
                }
                break;                
			default:
				break;
		}    
    	
    	return super.onKeyDown(keyCode, event);
    }

    /*
	private void onBackPressed()
	{
        Toast.makeText(this, "Pressed Back Key", 
        		Toast.LENGTH_LONG).show();
	}
    */
    
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
        if (keyCode == KeyEvent.KEYCODE_SEARCH){
            Toast.makeText(this, "pressed search key", Toast.LENGTH_SHORT).show();
            return true;
        }
		return super.onKeyUp(keyCode, event);
	}
}