package com.twistedeqations.dagger2tutorial.screens;

import com.twistedeqations.dagger2tutorial.GithubApplicationComponent;
import com.twistedeqations.dagger2tutorial.network.GithubService;
import com.twistedeqations.dagger2tutorial.screens.home.AdapterRepos;

import dagger.Component;

/**
 * Created by yena on 17. 1. 25.
 */

@HomeActivityScope
@Component(modules = HomeActivityModule.class, dependencies = GithubApplicationComponent.class)
// 명칭이 Component이긴하지만 사실상 Container라고 보면 될 것 같다.................
// 사실 아웃풋파일이 컨테이너 같긴한데.....어쨌든 개발단에선 얘가!
public interface HomeActivityComponent {
//    AdapterRepos adapterRepos();
//    GithubService githubService();

//    ***** 의존성을 뒤집는게 목적이고 DI는 걍 방법일 뿐임
//    - Component를 이용해 HomeActivity에 의존성주입을 한다.
//      즉, HomeActivity의 의존관계를 Component에 위임하게 되는 것이다!!
    void injectHomeActivity(HomeActivity homeActivity);

    // 이렇게 재귀적으로 써도 ㅇㅋ! 유튜브 왈 : nice chaining system.
//    HomeActivity injectHomeActivity(HomeActivity homeActivity);

    // 이건 안됨. AdapterRepos에서 Context를 못찾음
    // -> AdapterRepos생성자를 Context로 재변경해봄
    // -> 그러면 Activity쪽에 주입이 안됨.
    // -> 헐? 어떡하지?
    // -> 정말 주입할 딱 그 Destination 객체를 파라미터로 넣어줘야 한다.
//    void injectActivity(Context context);
}
