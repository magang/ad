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

package com.chanlin.ad.manager;

import com.chanlin.ad.base.BaseFragment;
import com.chanlin.ad.model.QDItemDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cginechen
 * @date 2016-10-21
 */

public class QDDataManager {
    private static QDDataManager _sInstance;
    private QDWidgetContainer mWidgetContainer;

    public QDDataManager() {
        mWidgetContainer = QDWidgetContainer.getInstance();
    }

    public static QDDataManager getInstance() {
        if (_sInstance == null) {
            _sInstance = new QDDataManager();
        }
        return _sInstance;
    }

    public QDItemDescription getDescription(Class<? extends BaseFragment> cls) {
        return mWidgetContainer.get(cls);
    }

    public String getName(Class<? extends BaseFragment> cls) {
        QDItemDescription itemDescription = getDescription(cls);
        if (itemDescription == null) {
            return null;
        }
        return itemDescription.getName();
    }

    public String getDocUrl(Class<? extends BaseFragment> cls) {
        QDItemDescription itemDescription = getDescription(cls);
        if (itemDescription == null) {
            return null;
        }
        return itemDescription.getDocUrl();
    }

    public List<QDItemDescription> getComponentsDescriptions() {
        List<QDItemDescription> list = new ArrayList<>();
        list.add(new QDItemDescription(null, "item1", 2131492876, "url1"));
        list.add(new QDItemDescription(null, "item2", 2131492876, "url2"));
        return list;
    }
}
