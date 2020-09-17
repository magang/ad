/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chanlin.ad.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chanlin.ad.QDMainActivity;
import com.chanlin.ad.fragment.home.HomeFragment;
import com.chanlin.ad.manager.QDDataManager;
import com.chanlin.ad.model.QDItemDescription;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.SwipeBackLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

/**
 * Created by cgspine on 2018/1/7.
 */

public abstract class BaseFragment extends QMUIFragment {

    private int mBindId = -1;

    public BaseFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected int backViewInitOffset(Context context, int dragDirection, int moveEdge) {
        if (moveEdge == SwipeBackLayout.EDGE_TOP || moveEdge == SwipeBackLayout.EDGE_BOTTOM) {
            return 0;
        }
        return QMUIDisplayHelper.dp2px(context, 100);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Object onLastFragmentFinish() {
        return new HomeFragment();

    }

    protected void goToWebExplorer(@NonNull String url, @Nullable String title) {
        Intent intent = QDMainActivity.createWebExplorerIntent(getContext(), url, title);
        startActivity(intent);
    }

    protected void injectDocToTopBar(QMUITopBar topBar) {
        final QDItemDescription description = QDDataManager.getInstance().getDescription(this.getClass());
        if (description != null) {
            topBar.addRightTextButton("DOC", QMUIViewHelper.generateViewId())
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToWebExplorer(description.getDocUrl(), description.getName());
                        }
                    });
        }
    }

    protected void injectDocToTopBar(QMUITopBarLayout topBar) {
        final QDItemDescription description = QDDataManager.getInstance().getDescription(this.getClass());
        if (description != null) {
            topBar.addRightTextButton("DOC", QMUIViewHelper.generateViewId())
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToWebExplorer(description.getDocUrl(), description.getName());
                        }
                    });
        }
    }
}
