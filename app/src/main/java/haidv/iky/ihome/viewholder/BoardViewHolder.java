package haidv.iky.ihome.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import haidv.iky.ihome.R;
import haidv.iky.ihome.model.Board;

public class BoardViewHolder extends RecyclerView.ViewHolder{

    private  static  final String TAG = "BoardViewHolder";

    public TextView txtName;
    public TextView txtOut1;
    public TextView txtOut2;
    public TextView txtOut3;

    public ImageView ivOut1;
    public ImageView ivOut2;
    public ImageView ivOut3;

    public ImageView ivSetting;
    public ImageView ivRemove;

    public BoardViewHolder(View itemView) {
        super(itemView);
        txtName = itemView.findViewById(R.id.tvNameItemBoardControl);
        txtOut1 = itemView.findViewById(R.id.tvOut1ItemBoardControl);
        txtOut2 = itemView.findViewById(R.id.tvOut2ItemBoardControl);
        txtOut3 = itemView.findViewById(R.id.tvOut3ItemBoardControl);

        ivOut1 = itemView.findViewById(R.id.ivOut1ItemBoardControl);
        ivOut2 = itemView.findViewById(R.id.ivOut2ItemBoardControl);
        ivOut3 = itemView.findViewById(R.id.ivOut3ItemBoardControl);

        ivSetting = itemView.findViewById(R.id.ivSettingItemBoardControl);
        ivRemove = itemView.findViewById(R.id.ivRemoveItemBoardControl);
    }


    public void bindToDeviceInfo(Board board, View.OnClickListener controlClickListener) {
        txtName.setText(board.getName());
        txtOut1.setText(board.getOut1());
        txtOut2.setText(board.getOut2());
        txtOut3.setText(board.getOut3());

        if((board.status & 0x01) == 0x01)
        {
            ivOut1.setImageResource(R.drawable.ic_switch_on);
        }
        else
        {
            ivOut1.setImageResource(R.drawable.ic_switch_off);
        }

        if((board.status & 0x02) == 0x02)
        {
            ivOut2.setImageResource(R.drawable.ic_switch_on);
        }
        else
        {
            ivOut2.setImageResource(R.drawable.ic_switch_off);
        }

        if((board.status & 0x04) == 0x04)
        {
            ivOut3.setImageResource(R.drawable.ic_switch_on);
        }
        else
        {
            ivOut3.setImageResource(R.drawable.ic_switch_off);
        }

        ivOut1.setOnClickListener(controlClickListener);
        ivOut2.setOnClickListener(controlClickListener);
        ivOut3.setOnClickListener(controlClickListener);
        ivSetting.setOnClickListener(controlClickListener);
        ivRemove.setOnClickListener(controlClickListener);
    }
}
