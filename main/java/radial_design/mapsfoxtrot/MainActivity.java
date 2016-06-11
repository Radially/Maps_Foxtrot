package radial_design.mapsfoxtrot;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.amazonaws.services.dynamodbv2.model.*;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button loginB;
    EditText userNameET;
    EditText passwordET;
    DynamoDBMapper mapper;
    User connectUser;

    Button mlloginB;
    EditText mluserNameET;
    EditText mlpasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "eu-west-1:16195ef4-c4f3-41f6-8237-0bc0117f8260", // Identity Pool ID
                Regions.EU_WEST_1 // Region
        );
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        ddbClient.setRegion(Region.getRegion(Regions.EU_WEST_1));  //super important detail
        mapper = new DynamoDBMapper(ddbClient);

        loginB = (Button) findViewById(R.id.ROfficerLoginButton);
        userNameET = (EditText) findViewById(R.id.LoginPageUserNameET);
        passwordET = (EditText) findViewById(R.id.LoginPagePasswordET);

        mlloginB = (Button) findViewById(R.id.MLLoginButton);
        mluserNameET = (EditText) findViewById(R.id.MLLoginPageUserNameET);
        mlpasswordET = (EditText) findViewById(R.id.MLLoginPagePasswordET);

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] details = {userNameET.getText().toString(), passwordET.getText().toString()};
                if (details[0] == null || details[1] == null)
                    Toast.makeText(MainActivity.this, "Enter user name and password", Toast.LENGTH_LONG).show();
                else new LoginTask().execute(details);
            }
        });

        mlloginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] details = {mluserNameET.getText().toString(), mlpasswordET.getText().toString()};
                if (details[0] == null || details[1] == null)
                    Toast.makeText(MainActivity.this, "Enter user name and password", Toast.LENGTH_LONG).show();
                else new MLConnectionTask().execute(details);
            }
        });


    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        protected void onPreExecute() {
        }

        protected Boolean doInBackground(String... params) {
            connectUser = mapper.load(User.class, params[0]);
            if(connectUser!=null) return (connectUser.getPassword().equals(params[1]));
            return false;
        }

        protected void onProgressUpdate(Integer... progress) {
            Toast.makeText(MainActivity.this, "Connecting...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                openROactivity();
            } else {
                Toast.makeText(MainActivity.this, "Wrong user name or password \n try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class MLConnectionTask extends AsyncTask<String, Void, Boolean> {
        protected void onPreExecute() {
        }

        protected Boolean doInBackground(String... params) {
            connectUser = mapper.load(User.class, params[0]);  //params[0] == RO User name
            if(connectUser!=null)return (connectUser.getPoolPassword().equals(params[1]));  //params[0] == Pool password
            return false;
        }

        protected void onProgressUpdate(Integer... progress) {
            Toast.makeText(MainActivity.this, "Connecting...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(MainActivity.this, "Connected to " + connectUser.getUserName(), Toast.LENGTH_SHORT).show();
                openMLactivity();
            } else {
                Toast.makeText(MainActivity.this, "Wrong Race Officer username or pool password \n try again", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void openROactivity() {
        Intent intent = new Intent(this, ROActivity.class);
        intent.putExtra("USER_TO_CONNECT", connectUser);
        startActivity(intent);
    }

    public void openMLactivity() {
        Intent intent = new Intent(this, MLActivity.class);
        intent.putExtra("My Race Officer", connectUser);
        startActivity(intent);
    }
}
