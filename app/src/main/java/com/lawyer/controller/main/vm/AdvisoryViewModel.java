package com.lawyer.controller.main.vm;

import android.databinding.Bindable;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsViewModel;
import com.lawyer.controller.main.MainAdvisoryFm;
import com.lawyer.databinding.FmMainAdvisoryBinding;
import com.lawyer.databinding.ItemAdvisoryCaseBinding;
import com.lawyer.databinding.ItemAdvisoryCaseTypeBinding;
import com.lawyer.databinding.ItemAdvisoryNewsBinding;
import com.lawyer.databinding.ItemAdvisoryVideoBinding;
import com.lawyer.entity.CaseExampleBean;
import com.lawyer.entity.CaseKnowledgeBean;
import com.lawyer.entity.CaseTypeBean;
import com.lawyer.entity.CityBean;
import com.lawyer.entity.UserIndexBean;
import com.lawyer.entity.VideoBean;
import com.lawyer.mvvm.adapter.listview.ListViewAdapter;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.Constant;

import java.util.ArrayList;
import java.util.List;

import ink.itwo.common.img.IMGLoad;
import ink.itwo.common.util.CacheUtil;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;
import ink.itwo.net.transformer.ResultTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 @author wang
 on 2019/2/21 */

public class AdvisoryViewModel extends AbsViewModel<MainAdvisoryFm, FmMainAdvisoryBinding> {
    @Bindable
    public UserIndexBean indexBean;
    @Bindable
    public String cityName;
    private List<CaseExampleBean> caseExampleData = new ArrayList<>();
    public ListViewAdapter<CaseExampleBean, ItemAdvisoryCaseBinding> casesAdapter =
            new ListViewAdapter<CaseExampleBean, ItemAdvisoryCaseBinding>(caseExampleData, R.layout.item_advisory_case, BR.bean) {
                @Override
                public void convert(ItemAdvisoryCaseBinding bind, CaseExampleBean item) {
                    bind.layout.setOnClickListener(v -> onSkip.put(11, item));
                }
            };
    private List<CaseKnowledgeBean> knowledgeData = new ArrayList<>();
    public ListViewAdapter<CaseKnowledgeBean, ItemAdvisoryNewsBinding> knowledgeAdapter =
            new ListViewAdapter<CaseKnowledgeBean, ItemAdvisoryNewsBinding>(knowledgeData, R.layout.item_advisory_news, BR.bean) {
                @Override
                public void convert(ItemAdvisoryNewsBinding bind, CaseKnowledgeBean item) {
                    bind.layout.setOnClickListener(v -> onSkip.put(12, item));
                }
            };
    private List<CaseTypeBean> caseTypeBeanList = new ArrayList<>();
    public ListViewAdapter<CaseTypeBean, ItemAdvisoryCaseTypeBinding> caseTypeAdapter =
            new ListViewAdapter<CaseTypeBean, ItemAdvisoryCaseTypeBinding>(caseTypeBeanList, R.layout.item_advisory_case_type, BR.bean) {
                @Override
                public void convert(ItemAdvisoryCaseTypeBinding bind, CaseTypeBean item) {
                    if (item.getId() == -1) {
//                        bind.image.setImageResource(R.mipmap.ic_advisory_more);
                        IMGLoad.with(AdvisoryViewModel.this.getView())
                                .asBitmap()
                                .load(R.mipmap.ic_advisory_more)
                                .into(bind.image);
                        bind.getRoot().setOnClickListener(v -> onSkip.put(13, true));
                    } else {
                        bind.getRoot().setOnClickListener(v -> onSkip.put(15, true));
                    }
                }
            };
    private List<VideoBean> videoBeanList = new ArrayList<>();
    public ListViewAdapter<VideoBean, ItemAdvisoryVideoBinding> videoViewAdapter =
            new ListViewAdapter<VideoBean, ItemAdvisoryVideoBinding>(videoBeanList, R.layout.item_advisory_video, BR.bean) {
                @Override
                public void convert(ItemAdvisoryVideoBinding bind, VideoBean item) {
                    super.convert(bind, item);
                    bind.layout.setOnClickListener(v -> onSkip.put(14, item));
                }
            };
    public View.OnClickListener onlineClick = v -> onSkip.put(3, true);
    public View.OnClickListener callClick = v -> onSkip.put(4, true);
    public View.OnClickListener knowMoreClick = v -> onSkip.put(1, true);
    public View.OnClickListener casesClick = v -> onSkip.put(2, true);

    public AdvisoryViewModel(MainAdvisoryFm mainAdvisoryFm, FmMainAdvisoryBinding fmMainAdvisoryBinding) {
        super(mainAdvisoryFm, fmMainAdvisoryBinding);
    }

    public void getInfo() {
        NetManager.http().create(API.class)
                .userIndex(/*UserCache.getAccessToken()*/)
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<UserIndexBean>>() {
                    @Override
                    public void onNext(Result<UserIndexBean> result) {
                        indexBean = result.getData();
                        if (indexBean == null) return;
                        CacheUtil.put(Constant.cache_key_telNum, indexBean.getTelNum());
                        caseExampleData.clear();
                        if (indexBean.getCaseExampleList() != null) {
                            caseExampleData.addAll(indexBean.getCaseExampleList());
                        }
                        if (indexBean.getCaseTypeList() == null) {
                            List<CaseTypeBean> caseTypeList = new ArrayList<>();
                            indexBean.setCaseTypeList(caseTypeList);
                        }
                        List<CaseTypeBean> caseTypeList = indexBean.getCaseTypeList();
                        CaseTypeBean caseTypeBean = new CaseTypeBean();
                        caseTypeBean.setId(-1);
                        caseTypeBean.setName("更多");
                        caseTypeList.add(caseTypeBean);
                        caseTypeBeanList.clear();
                        caseTypeBeanList.addAll(caseTypeList);
                        caseTypeAdapter.notifyDataSetChanged();
                        casesAdapter.notifyDataSetChanged();

                        videoBeanList.clear();
                        if (indexBean.getCaseVideoList() != null) {
                            videoBeanList.addAll(indexBean.getCaseVideoList());
                        }
                        videoViewAdapter.notifyDataSetChanged();

                        knowledgeData.clear();
                        if (indexBean.getCaseKnowledgeList() != null) {
                            knowledgeData.addAll(indexBean.getCaseKnowledgeList());
                        }
                        knowledgeAdapter.notifyDataSetChanged();
                        notifyPropertyChanged(BR.indexBean);
                    }
                });
        userFetchCity();
    }


    private void userFetchCity() {
        NetManager.http().create(API.class)
                .userFetchCity()
                .compose(ResultTransformer.handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver<Result<CityBean>>() {
                    @Override
                    public void onNext(Result<CityBean> result) {
                        CityBean data = result.getData();
                        if (data != null) {
                            cityName = data.getCity();
                            notifyPropertyChanged(BR.cityName);
                        }
                    }

                });
    }


}
