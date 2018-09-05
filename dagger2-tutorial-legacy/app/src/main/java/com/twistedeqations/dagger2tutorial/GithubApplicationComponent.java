package com.twistedeqations.dagger2tutorial;

import com.squareup.picasso.Picasso;
import com.twistedeqations.dagger2tutorial.network.GithubService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yena on 17. 1. 24.
 */

// Do not use singleton annotation under andy circumstances
//@Singleton // is the worst and the most mis-reading and the biggest mistake of dagger tutorials
// it can occur mis-reading. it seems like modern java singletone. but it is not.
// this has a equal mean with GithubApplicationScope.
// you will get different instance each time you call.

// 마치 일반적인 자바에서의 싱글톤처럼 보여지지만 그렇지 않아!!
// 이건 걍 이 밑에 있는 GithubApplicationScope와 동일한 의미를 가진다궁
// build()후 주입한 인스턴스들이 동일 인스턴스를 가지는 거 일 뿐이지
// build()시마다 GithubApplicationComponent의 인스턴스는 새로 생긴다구!


//@Singleton
@GithubApplicationScope
@Component(modules = { GithubServiceModule.class, PicassoModule.class, ActivityModule.class})
public interface GithubApplicationComponent {

    Picasso getPicasso();

    GithubService getGithubService();
}
