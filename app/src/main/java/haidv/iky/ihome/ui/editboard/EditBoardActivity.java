package haidv.iky.ihome.ui.editboard;

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

import haidv.iky.ihome.R;
import haidv.iky.ihome.model.Board;
import haidv.iky.ihome.util.ViewUtil;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;

public class EditBoardActivity extends AppCompatActivity {

    public static final String EXTRA_BOARD_KEY = "EXTRA_BOARD_KEY";
    public static final String TAG = "EditBoardActivity";

    private String mBoardKey;

    private Board board;
    private DatabaseReference mBoardReference;

    EditText edtName;
    EditText edtOut1;
    EditText edtOut2;
    EditText edtOut3;

    ImageButton imageButtonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        // Get post key from intent
        mBoardKey = getIntent().getStringExtra(EXTRA_BOARD_KEY);
        if (mBoardKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        edtName = findViewById(R.id.etNameActivityChangeBoardInfo);
        edtOut1 = findViewById(R.id.etOut1ActivityChangeBoardInfo);
        edtOut2 = findViewById(R.id.etOut2ActivityChangeBoardInfo);
        edtOut3 = findViewById(R.id.etOut3ActivityChangeBoardInfo);


        // Initialize Database
        mBoardReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getUid()).child("info").child(mBoardKey);

        mBoardReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                board = dataSnapshot.getValue(Board.class);
                Log.d(TAG, "deviceInfo: " + board);

                if(board != null)
                {
                    edtName.setText(board.getName());
                    edtOut1.setText(board.getOut1());
                    edtOut2.setText(board.getOut2());
                    edtOut3.setText(board.getOut3());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toast("Lỗi khi cập nhật thông tin");
            }
        });

        imageButtonSave = findViewById(R.id.ibSaveActivityChangeBoardInfo);
        imageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), HIDE_NOT_ALWAYS);

                if(ViewUtil.validateText(edtName.getText().toString()) == false){
                    toast("Tên thiết bị không hợp lệ");
                    return;
                }

                if(ViewUtil.validateText(edtOut1.getText().toString()) == false){
                    toast("Tên thiết bị không hợp lệ");
                    return;
                }

                if(ViewUtil.validateText(edtOut2.getText().toString()) == false){
                    toast("Tên thiết bị không hợp lệ");
                    return;
                }

                if(ViewUtil.validateText(edtOut3.getText().toString()) == false){
                    toast("Tên thiết bị không hợp lệ");
                    return;
                }

                board.setName(edtName.getText().toString());
                board.setOut1(edtOut1.getText().toString());
                board.setOut2(edtOut2.getText().toString());
                board.setOut3(edtOut3.getText().toString());

                mBoardReference.setValue(board)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                toast("Đổi thông tin thành công");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                toast("Lỗi khi đổi thông tin");
                            }
                        });
            }
        });
    }

    private void toast(final String ss){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EditBoardActivity.this, ss, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
