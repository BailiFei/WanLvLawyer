package com.m7.imkfsdk.chat.chatrow;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.ChatActivity;
import com.m7.imkfsdk.chat.holder.BaseHolder;
import com.m7.imkfsdk.chat.holder.InvestigateViewHolder;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.SubmitInvestigateListener;
import com.moor.imkf.model.entity.FromToMessage;
import com.moor.imkf.model.entity.Investigate;
import com.moor.imkf.model.entity.MsgInves;

import java.util.Collection;

/**
 * Created by longwei on 2016/3/9.
 */
public class InvestigateChatRow extends BaseChatRow {

    private Context context;
    private SharedPreferences sp;

    public InvestigateChatRow(int type) {
        super(type);
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail) {
        return false;
    }

    @Override
    protected void buildChattingData(final Context context, BaseHolder baseHolder, FromToMessage detail, int position) {
        this.context = context;
        sp = context.getSharedPreferences("moordata", 0);
        InvestigateViewHolder holder = (InvestigateViewHolder) baseHolder;
        final FromToMessage message = detail;
        LinearLayout linearLayout = holder.getChat_investigate_ll();
        TextView investigate_tv = holder.getChat_investigate_tv();
        linearLayout.removeAllViews();
        final String satifyThank = sp.getString("satisfyThank","感谢您对此次服务做出评价，祝您生活愉快，再见！");
        if(message != null) {

            investigate_tv.setText( sp.getString("satisfyTitle","感谢您使用我们的服务，请为此次服务评价！"));
            final Collection<MsgInves> investigates = message.investigates;
            for (final MsgInves investigate : investigates) {
                LinearLayout investigateItem = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.kf_investigate_item, null);
                TextView tv = (TextView) investigateItem.findViewById(R.id.investigate_item_tv_name);
                tv.setText(investigate.name);
                investigateItem.setTag(investigate);
                investigateItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context, "点击了:"+investigate.name, Toast.LENGTH_SHORT).show();
                        Investigate inv = new Investigate();
                        inv.name = investigate.name;
                        inv.value = investigate.value;
                        IMChatManager.getInstance().submitInvestigate(inv, new SubmitInvestigateListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(context, satifyThank, Toast.LENGTH_SHORT).show();
                                //删除该条数据
                                IMChatManager.getInstance().deleteInvestigateMsg(message);
                                ((ChatActivity) context).updateMessage();
                            }

                            @Override
                            public void onFailed() {

                            }
                        });
                    }
                });
                linearLayout.addView(investigateItem);
            }
        }
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.kf_chat_row_investigate, null);
            InvestigateViewHolder holder = new InvestigateViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, false));
        }
        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.INVESTIGATE_ROW_TRANSMIT.ordinal();
    }
}
