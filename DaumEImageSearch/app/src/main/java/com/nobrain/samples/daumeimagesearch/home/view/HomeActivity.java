package com.nobrain.samples.daumeimagesearch.home.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.nobrain.samples.daumeimagesearch.R;
import com.nobrain.samples.daumeimagesearch.home.adapter.ImageAdapter;
import com.nobrain.samples.daumeimagesearch.home.adapter.ImageAdapterDataView;
import com.nobrain.samples.daumeimagesearch.home.dagger.DaggerHomeComponent;
import com.nobrain.samples.daumeimagesearch.home.dagger.HomeModule;
import com.nobrain.samples.daumeimagesearch.network.SearchApi;
import com.nobrain.samples.daumeimagesearch.views.YenaMemberInjectTest;
import com.nobrain.samples.daumeimagesearch.home.presenter.HomePresenter;
import com.nobrain.samples.daumeimagesearch.home.presenter.HomePresenterImpl;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class HomeActivity extends AppCompatActivity implements HomePresenter.View {

    @Inject
    YenaMemberInjectTest yenaMemberInjectTest;

    @Inject
    SearchApi searchApi;

    @Inject
    HomePresenter homePresenter;

    @Inject
    ImageAdapterDataView imageAdapterDataView;

    @Bind(R.id.et_home_search)
    EditText etSearch;

    @Bind(R.id.rv_home_search_result)
    RecyclerView rvSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ImageAdapter adapter = new ImageAdapter(HomeActivity.this);

        DaggerHomeComponent.builder()
                .homeModule(new HomeModule(this, adapter))
                .build()
                .inject(this);

        ButterKnife.bind(this);

        rvSearchResult.setAdapter(adapter);
        rvSearchResult.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

        imageAdapterDataView.setOnRecyclerItemClickListener((adapter1, position) -> {
            homePresenter.onItemClick(position);
        });

//        yenaMemberInjectTest.call();
    }

    @Override
    protected void onDestroy() {
        homePresenter.unSubscribeSearch();
        super.onDestroy();
    }

    @OnTextChanged(R.id.et_home_search)
    void onChangedSearchText(CharSequence text) {
        homePresenter.inputSearchText(text.toString());
    }

    @Override
    public void refresh() {
        imageAdapterDataView.refresh();
    }

    @Override
    public void onMoveLink(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
}
