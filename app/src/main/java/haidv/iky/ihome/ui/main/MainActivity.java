package haidv.iky.ihome.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import haidv.iky.ihome.R;
import haidv.iky.ihome.model.Board;
import haidv.iky.ihome.ui.addboard.AddBoardActivity;
import haidv.iky.ihome.ui.base.BaseActivity;
import haidv.iky.ihome.ui.editboard.EditBoardActivity;
import haidv.iky.ihome.ui.login.LoginActivity;
import haidv.iky.ihome.viewholder.BoardViewHolder;

public class MainActivity extends BaseActivity implements MainMvpView{

    private  static  final String TAG = "MainActivity";

    private DatabaseReference mDatabase;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private FirebaseRecyclerAdapter<Board, BoardViewHolder> mFirebaseAdapter;

    ImageView ivLogout;
    ImageView ivAddBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = this.findViewById(R.id.board_list);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        showProgressDialog();

        Log.d(TAG, "getUid: " + getUid());

        // Set up FirebaseRecyclerAdapter with the Query
        Query deviceListQuery = mDatabase.child(getUid()).child("info");

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Board>()
                .setQuery(deviceListQuery, Board.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Board, BoardViewHolder>(options) {


            @Override
            public void onError(@NonNull DatabaseError error) {
                Log.d(TAG, "DatabaseError: " + error.toString());
            }

            @Override
            public BoardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new BoardViewHolder(inflater.inflate(R.layout.item_board, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(BoardViewHolder viewHolder, int position, final Board model) {

                hideProgressDialog();

                final DatabaseReference deviceRef = getRef(position);
                final String deviceKey = deviceRef.getKey();
                Log.d(TAG, model.name + " " + model.out1+ " " + model.out2+ " " + model.out3);


                viewHolder.bindToDeviceInfo(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d(TAG, deviceKey + "onClick ");
                        DatabaseReference outControlRef = mDatabase.child(getUid()).child("status").child(deviceRef.getKey());
                        switch (view.getId()) {
                            case R.id.ivOut1ItemBoardControl:
                                onOuputClicked(outControlRef,deviceKey,1);
                                break;

                            case R.id.ivOut2ItemBoardControl:
                                onOuputClicked(outControlRef,deviceKey,2);
                                break;

                            case R.id.ivOut3ItemBoardControl:
                                onOuputClicked(outControlRef,deviceKey,3);
                                break;

                            case R.id.ivSettingItemBoardControl:
                                Bundle bundle = new Bundle();
                                bundle.putString("key", deviceRef.getKey());

                                Intent intent = new Intent(MainActivity.this, EditBoardActivity.class);
                                intent.putExtra(EditBoardActivity.EXTRA_BOARD_KEY, deviceKey);
                                startActivity(intent);
                                break;

                            case R.id.ivRemoveItemBoardControl:
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                builder.setTitle("Xóa thiết bị");
                                builder.setMessage("Bạn muốn xóa thiết bị khỏi hệ thống ?");

                                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do do my action here
                                        mDatabase.child(getUid()).child("status").child(deviceKey).removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                });
                                        mDatabase.child(getUid()).child("info").child(deviceKey).removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                });
                                        dialog.dismiss();
                                    }

                                });

                                builder.setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // I do not need any action here you might
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();
                                break;

                            default:
                                break;
                        }
                    }
                });
            }
        };
        mRecycler.setAdapter(mFirebaseAdapter);

        ivLogout = findViewById(R.id.ivLogoutActivityMain);
        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // Go to MainActivity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        ivAddBoard = findViewById(R.id.ivAddActivityMain);

        ivAddBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch PostDetailActivity
                startActivity(new Intent(MainActivity.this, AddBoardActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mFirebaseAdapter != null)
        {
            mFirebaseAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mFirebaseAdapter != null)
        {
            mFirebaseAdapter.stopListening();
        }
    }

    private void onOuputClicked(DatabaseReference outRef, final String key, final int position) {
        showProgressDialog();
        outRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "iStatus: " + snapshot.getValue());

                Integer iStatus = snapshot.getValue(Integer.class);
                byte bStatus;
                if(snapshot.getValue() != null)
                {
                    bStatus = iStatus.byteValue();
                }
                else
                {
                    bStatus = 0;
                }
                if (((bStatus) & (0x01 << (position - 1))) == (0x01 << (position - 1))) {
                    bStatus = (byte) ( bStatus & (~(0x01 << (position - 1))));
                } else {
                    bStatus = (byte) ( bStatus | ((0x01 << (position - 1))));
                }
                mDatabase.child(getUid()).child("status").child(key).setValue((int) bStatus)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            hideProgressDialog();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgressDialog();
                        }
                    });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
            }
        });
    }


}
