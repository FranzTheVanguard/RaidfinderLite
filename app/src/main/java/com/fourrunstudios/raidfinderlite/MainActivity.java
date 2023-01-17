package com.fourrunstudios.raidfinderlite;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fourrunstudios.raidfinderlite.adapters.RecyclerViewAdapter;
import com.fourrunstudios.raidfinderlite.databinding.ActivityMainBinding;
import com.fourrunstudios.raidfinderlite.viewmodels.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity{
    private ActivityMainBinding binding;
    private FloatingActionButton addRaid;
    private final String TAG = "";
    private final int INTERVAL = 5;
    private final Observable<Long> loopRaidObservable = Observable
            .interval(INTERVAL, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    private final Context context = this;
    private CompositeDisposable disposables = new CompositeDisposable();
    private MainViewModel viewModel;
    private androidx.lifecycle.Observer<List<Tweet>> raidObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        init();
    }
    private void init(){
        addRaid = binding.addRaid;
        registerForContextMenu(addRaid);
        setOnClick();
        viewModelSetup();
        raidObserverSetup();
    }
    private void setOnClick(){
        addRaid.setOnClickListener(v ->{
            openContextMenu(addRaid);
        });
    }
    private void viewModelSetup() {
        if(viewModel == null){
            viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        }
        viewModel.init(this);
    }
    private void adapterSetup(List<Tweet> liveTweets){
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(context, liveTweets);
        binding.raidView.setAdapter(adapter);
        if(binding.raidView.getLayoutManager()==null) binding.raidView.setLayoutManager(new LinearLayoutManager(context));
    }
    public void raidObserverSetup(){
        raidObserver = tweets -> {
            adapterSetup(tweets);
            Log.d(TAG, "List Updated!");
        };
    }

    public void updateTitle(String name){
        binding.raidName.setText(name);
    }
    public void getRaid(){
        viewModel.getLiveRaid().observe(this, raidObserver);
        updateRaid();
    }
    private void updateRaid(){
        clearDisposables();
        loopRaidObservable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposables.add(d);
            }
            @Override
            public void onNext(@NonNull Long aLong) {
                viewModel.updateRaid();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void clearDisposables(){
        disposables.clear();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Raids");
        getMenuInflater().inflate(R.menu.raids_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        viewModel.idToRaidName(item, this);
        return super.onContextItemSelected(item);
    }



}

