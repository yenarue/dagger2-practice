package com.twistedeqations.dagger2tutorial.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twistedeqations.dagger2tutorial.GithubApplication;
import com.twistedeqations.dagger2tutorial.R;
import com.twistedeqations.dagger2tutorial.models.GithubRepo;
import com.twistedeqations.dagger2tutorial.network.GithubService;
import com.twistedeqations.dagger2tutorial.screens.home.AdapterRepos;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.repo_home_list)
    ListView listView;

    // HomeActivity don't need to have this field anymore.
    // it can have in HomeActivityComponent. HomeActivity don't care about inside Component. 진정한 위임ㅋ_
//    Picasso picasso;

    Call<List<GithubRepo>> reposCall;

    // 실제로 인젝트되는 타이밍이 언젤까? =>  Line61) component.injectHomeActivity(this); 에서 주입됨!
    @Inject
    GithubService githubService;

    @Inject
    AdapterRepos adapterRepos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        HomeActivityComponent component = DaggerHomeActivityComponent.builder()
                .homeActivityModule(new HomeActivityModule(this))
                .githubApplicationComponent(GithubApplication.get(this).component()) // Multi component
                .build();

        // Legacy
////        githubService = GithubApplication.get(this).getGithubService();
////        picasso = GithubApplication.get(this).getPicasso();
////        adapterRepos = new AdapterRepos(this, picasso);

        // Step1. component에서 가져오기
//        githubService = component.githubService();
//        adapterRepos = component.adapterRepos();

        // Step2. component를 통해 HomeActivity로 의존성 주입하
        component.injectHomeActivity(this); // component의 의존관계그래프를 HomeActivity에 주입(inject)한다.

        listView.setAdapter(adapterRepos);

        reposCall = githubService.getAllRepos();
        reposCall.enqueue(new Callback<List<GithubRepo>>() {
            @Override
            public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {
                adapterRepos.swapData(response.body());
            }

            @Override
            public void onFailure(Call<List<GithubRepo>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error getting repos " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reposCall != null) {
            reposCall.cancel();
        }
    }

//    // 이 메소드의 파라미터를 인젝션 받겠다! -> 어디로부터? 이 파라미터와 의존관계를 가지는 애로부터! -> Component!
//    // HomeActivity는 이걸 누구한테 받느냐보다는 이걸 받는것 자체가 더 중요하기 때문에
//    // 어느 모듈로부터 이걸 주입받아야할지는 굳이 알 필요없음. 걍 위임하는거임 컴포넌트에ㅋ
//    @Inject
//    public void setGithubService(GithubService githubService) {
//        this.githubService = githubService;
//    }
    // 하지만 이 케이스에서는 멤버변수에 저장하는거니까...
    // 굳이 파라미터inject를 할필요 없고, 걍 this.githubService선언시에 변수Inject를 해도 된다.
}
