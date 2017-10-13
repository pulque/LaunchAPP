/*
 * Copyright 2016 Li Zhe <pulqueli@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lizheblogs.launchapp;

import android.app.Application;

/**
 * App Application
 * Created by Norman.Li on 10/5/2017.
 * 主要用于Volley全局使用
 */
public class SubApplication extends Application {

    private static final String REQUEST_QUEUE_TAG = "SubApplication";
    private static SubApplication application;

    /**
     * Gets Application object
     *
     * @return
     */
    public static synchronized SubApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        AppCrashHandler.getInstance().init();
    }

}
