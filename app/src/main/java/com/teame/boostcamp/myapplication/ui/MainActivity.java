package com.teame.boostcamp.myapplication.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.stetho.Stetho;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.MainViewPagerAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityMainBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseActivity;
import com.teame.boostcamp.myapplication.ui.search.SearchPlaceFragment;
import com.teame.boostcamp.myapplication.util.TedPermissionUtil;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.WorkInfo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements FragmentCallback {

    private CompositeDisposable disposable = new CompositeDisposable();
    private BehaviorSubject<Long> subject = BehaviorSubject.createDefault(0L);

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        binding.bnvMainNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        disposable.add(TedPermissionUtil.requestPermission(getApplicationContext(),
                "권한설정",
                "권한설정이 필요합니다 권한을 설정하시겠습니까?",
                TedPermissionUtil.LOCATION, TedPermissionUtil.WRITE_STORAGE, TedPermissionUtil.READ_STORAGE, TedPermissionUtil.CAMERA)
                .subscribe(result -> {
                    if (result.isGranted())
                        setupViewPager();
                    else
                        finish();
                }, e -> {
                    finish();
                }));


        disposable.add(
                subject.buffer(2, 1)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(buffer -> {
                            boolean isExit = buffer.get(1) - buffer.get(0) <= 2000;
                            if (isExit) {
                                finish();
                            } else {
                                showToast("뒤로가기를 누르면 종료됩니다.");
                            }
                        })
        );

    }

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
        super.onDestroy();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_main:
                binding.vpFragment.setCurrentItem(0);
                return true;
            case R.id.navigation_my_list:
                binding.vpFragment.setCurrentItem(1);
                return true;
            case R.id.navigation_sns:
                binding.vpFragment.setCurrentItem(2);
                return true;
        }
        return false;
    };

    @SuppressLint("ClickableViewAccessibility")
    private void setupViewPager() {
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        Fragment fragmentHome = NewMainFragment.newInstance();
        Fragment fragmentWallet = MyListFragment.newInstance();
        Fragment fragmentNavigationDrawer = SNSFragment.newInstance();
        mainViewPagerAdapter.addFragment(fragmentHome);
        mainViewPagerAdapter.addFragment(fragmentWallet);
        mainViewPagerAdapter.addFragment(fragmentNavigationDrawer);
        binding.vpFragment.setAdapter(mainViewPagerAdapter);
        binding.vpFragment.setOffscreenPageLimit(mainViewPagerAdapter.getCount() - 1);
    }

    @Override
    public void startNewFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_change, SearchPlaceFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager=getSupportFragmentManager();
        boolean isPop=manager.popBackStackImmediate();
        if(!isPop){
            subject.onNext(System.currentTimeMillis());
        }
    }
}
