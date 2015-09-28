package practice.juliosanchez.com.asynctaskex;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AsynTaskActivity extends Activity {

    private final static String TAG = "ThreadingAsyncTask";

    private ImageView mImageView;
    private ProgressBar mProgressBar;
   // final Button loadButton, otherButton; whys is this wrong?

    private int mDelay = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asyn_task);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        //the final keyword that the variable can only be assigned once
        final Button loadButton = (Button) findViewById(R.id.loadButton);
        final Button otherButton = (Button) findViewById(R.id.otherButton);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new LoadIconTask().execute(R.drawable.ic_launcher);
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AsynTaskActivity.this, "Im Working", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asyn_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //creating an AsyncTask: provides structured way to manage work involving background
    //and UI threads <params, Progress, Result>

    class LoadIconTask extends AsyncTask<Integer, Integer, Bitmap>{

        //this methods runs on the UI thread before creating
        //a new thread for this task to run on
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

        //Interger is the resource ID of the bitmap that was passed down
        // to thhis class in its execute method
        //Run in AsyncThread, AsyncTask must be subclassed to be used
        @Override
        protected Bitmap doInBackground(Integer... params) {
            //params can be used for the position of the passed down resource
            Bitmap tmp = BitmapFactory.decodeResource(getResources(), params[0]);

            for(int i = 1; i < 11; i++){
                sleep();
                publishProgress(i*10);
            }
            return tmp;
        }

        //Runs in UI thread, receives the integer that was pass to publishProgress
        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
        }

        //Runs on UI thread and receives
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            mImageView.setImageBitmap(bitmap);
        }
    }

    private void sleep(){
        try{
            Thread.sleep(mDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
