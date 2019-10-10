package com.lerex.tr;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
class user {
    public String u_name,phone,email,password;
    public Boolean isActive = false;
    public user(){

    }
    public user(String u_name,String phone,String email){
        this.u_name=u_name;
        this.phone=phone;
        this.email=email;
    }
}

public class SignUp extends AppCompatActivity {

    private DatabaseReference mydb = FirebaseDatabase.getInstance().getReference("Users");//RealTime Database Connection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.signuplayout);

        //final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.replace(R.id.ls_placeholder,new Login() );
        //ft.commit();

        //final Button signup=findViewById(R.id.sign_up);
        //final EditText eU,ePh,eEa;
        //eU=findViewById(R.id.username);
        //ePh=findViewById(R.id.phone_number);
        //eEa=findViewById(R.id.e_add);


        /*signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                u_name=eU.getText().toString();
                ph_no=ePh.getText().toString();
                email=eEa.getText().toString();
                user1 u1 = new user1(u_name,ph_no,email);
                mydb.child(ph_no).setValue(u1);//Db entry
                Intent intent=new Intent(MainActivity.this,sellActivity.class);
                startActivity(intent);
            }
        });*/

    }
}
