package com.m7.imkfsdk.chat;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.m7.imkfsdk.R;
import com.m7.imkfsdk.constant.Constants;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.OnSessionBeginListener;
import com.moor.imkf.model.entity.CardInfo;
import com.moor.imkf.model.entity.Peer;
import com.moor.imkf.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 技能组列表界面
 */
public class PeerDialog extends DialogFragment {

    private ListView investigateListView;

    private List<Peer> peers = new ArrayList<Peer>();
    private CardInfo mCardInfo;
    private PeerAdapter adapter;
    private String type;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("选择技能组");
        getDialog().setCanceledOnTouchOutside(false);

        // Get the layout inflater
        View view = inflater.inflate(R.layout.kf_dialog_common, null);
        investigateListView = (ListView) view.findViewById(R.id.investigate_list);

        Bundle bundle = getArguments();
        peers = (List<Peer>) bundle.getSerializable("Peers");

        if(bundle.getSerializable("cardInfo")!=null){
            mCardInfo = (CardInfo) bundle.getSerializable("cardInfo");
        }

        type = bundle.getString("type");

        adapter = new PeerAdapter(getActivity(), peers);

        investigateListView.setAdapter(adapter);

        if("init".equals(type)) {
            investigateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dismiss();
                    Peer peer = (Peer) parent.getAdapter().getItem(position);
                    ChatActivity.startActivity(getActivity(), Constants.TYPE_PEER,peer.getId(),mCardInfo);
                }
            });
        }else if("chat".equals(type)) {
            investigateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dismiss();
                    Peer peer = (Peer) parent.getAdapter().getItem(position);
                    LogUtils.aTag("beginSession","PeerDialog75行代码");
                    beginSession(peer.getId());
                }
            });
        }

        return view;
    }

    private void beginSession(String peerId) {
        IMChatManager.getInstance().beginSession(peerId, new OnSessionBeginListener() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailed() {
            }
        });
    }


    @Override
    public void show(android.app.FragmentManager manager, String tag) {
        if(!this.isAdded()) {
            try {
                super.show(manager, tag);
            }catch (Exception e) {}
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        }catch (Exception e) {}

    }
}
