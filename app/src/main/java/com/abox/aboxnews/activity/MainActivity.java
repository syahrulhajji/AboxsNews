package com.abox.aboxnews.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abox.aboxnews.database.SharePreference;
import com.abox.aboxnews.fragment.AboxsNewsFragment;
import com.abox.aboxnews.utility.AboxsNewsActivity;
import com.abox.aboxnews.utility.Utility;
import com.abox.news.aboxnews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AboxsNewsActivity implements AboxsNewsFragment.FragmentInteractionListener {

    private LayoutInflater inflater;
    private SharePreference sharePreference;
    private Utility utility;
    final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

    @BindView(R.id.navigation_menu) NavigationView navigationMenu;
    @BindView(R.id.drawer_page) DrawerLayout drawerPage;
    @BindView(R.id.toolbar_title) Toolbar toolbarTitle;
    @BindView(R.id.tv_title) TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharePreference = new SharePreference(getApplicationContext());
        utility = new Utility();

        setSupportActionBar(toolbarTitle);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerPage, toolbarTitle, R.string.open, R.string.close);
        drawerPage.setDrawerListener(toggle);
        toggle.syncState();
        Utility utility = new Utility();

        navigationMenu.inflateMenu(R.menu.item_menu);
        navigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.isChecked()){item.setChecked(false);}
                else{item.setChecked(true);}
                drawerPage.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_logout:
                        inflater = getLayoutInflater();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        View view = inflater.inflate(R.layout.dialog_exit,null);
                        builder.setView(view);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        Button btnYes = (Button) view.findViewById(R.id.btn_yes);
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                cannotReplaceFragment(new LoginFragment());
                                alertDialog.dismiss();
                            }
                        });
                        Button btnNo = (Button)view.findViewById(R.id.btn_no);
                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Wrong Choice ",Toast.LENGTH_SHORT).show();
                        return true;
                }

            }
        });
    }

    @Override
    public void onFragmentInteraction(String title, boolean isTabSolid, boolean isTabVisible) {
        title = getString(R.string.app_name);
        if(title!=null)
            tvTitle.setText(title);
        if(isTabSolid) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                toolbarTitle.setBackgroundColor(getResources().getColor(R.color.colorGray, getTheme()));
            } else {
                toolbarTitle.setBackgroundColor(getResources().getColor(R.color.colorGray));
            }
            DrawerLayout.LayoutParams layoutParam = (DrawerLayout.LayoutParams) navigationMenu.getLayoutParams();
            layoutParam.setMargins(layoutParam.leftMargin,0,layoutParam.rightMargin,layoutParam.bottomMargin);
        }else {
            DrawerLayout.LayoutParams layoutParam = (DrawerLayout.LayoutParams) navigationMenu.getLayoutParams();
            layoutParam.setMargins(layoutParam.leftMargin,layoutParam.topMargin+getToolbarHeight(),layoutParam.rightMargin,layoutParam.bottomMargin);
        }
    }

    @Override
    public int getToolbarHeight() {
        return toolbarTitle.getHeight();
    }

    public void hideMenuSlide(){
        drawerPage.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigationMenu.setVisibility(View.GONE);
        navigationMenu.setEnabled(false);
        toolbarTitle.setVisibility(View.GONE);
    }

    public void showMenuSlide(){
        drawerPage.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        navigationMenu.setEnabled(true);
        toolbarTitle.setVisibility(View.VISIBLE);
        navigationMenu.setVisibility(View.VISIBLE);
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.page_fragment, fragment).addToBackStack(null).commit();
    }
    public void cannotReplaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.page_fragment, fragment).commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
