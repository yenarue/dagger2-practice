package kyn.android.com.beginerdagger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    GitHubService githubService;

    Call<List<GitHubRepo>> reposCall;

    AdapterRepos adapterRepos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        adapterRepos = new AdapterRepos(this);
        listView.setAdapter(adapterRepos);

        githubService = GithubApplication.get(tthis).getGithubService();

        reposCall = githubService.getAllRepos();
        reposCall.enqueue(new Callback<List<GitHubRespo>>() {

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (reposCall != null) {
            reposCall.cancel();
        }
    }
}
