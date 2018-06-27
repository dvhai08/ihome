package haidv.iky.ihome.ui.addboard;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import haidv.iky.ihome.R;
import haidv.iky.ihome.model.Board;
import haidv.iky.ihome.ui.editboard.EditBoardActivity;
import haidv.iky.ihome.util.ViewUtil;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;

public class AddBoardActivity extends AppCompatActivity {
    private static final String TAG = "AddBoardActivity";
    private DatabaseReference mBoardInfoReference;
    private DatabaseReference mBoardStatusReference;
    String mBoardKey;
    ImageButton imageButtonSave;
    EditText edtMAC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_board);

        edtMAC = findViewById(R.id.edtMACAddBoard);
        imageButtonSave = findViewById(R.id.ibSaveActivityAddBoard);
        imageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), HIDE_NOT_ALWAYS);

                if(ViewUtil.validateText(edtMAC.getText().toString()) == false){
                    toast("Địa chỉ MAC không hợp lệ");
                    return;
                }
                mBoardKey = edtMAC.getText().toString();
                mBoardInfoReference = FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseAuth.getInstance().getUid()).child("info").child(mBoardKey);

                mBoardStatusReference = FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseAuth.getInstance().getUid()).child("status").child(mBoardKey);



                mBoardInfoReference.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                Board board = dataSnapshot.getValue(Board.class);

                                // [START_EXCLUDE]
                                if (board == null) {
                                    writeNewBoard(mBoardKey,"name","out1","out2","out3",0);
                                } else {
                                    // Write new post
                                    Toast.makeText(AddBoardActivity.this,
                                            "Thiết bị đã tồn tại trên hệ thống",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        });
            }
        });
    }

    private void toast(final String ss){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AddBoardActivity.this, ss, Toast.LENGTH_SHORT).show();

            }
        });
    }

    // [START write_fan_out]
    private void writeNewBoard(String key, String name, String out1, String out2, String out3, int status)  {

        Board board = new Board(name,out1,out2,out3,status);
        Map<String, Object> boardValues = board.toMap();

        mBoardInfoReference.setValue(boardValues)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    toast("onSuccess");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toast("onFailure");
                }
            });
        mBoardStatusReference.setValue(board.status)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    toast("onSuccess");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toast("onFailure");
                }
            });
    }
}
