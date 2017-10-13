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

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * UncaughtException, save exception file.
 *
 * @author Norman.Li
 */
public class AppCrashHandler implements UncaughtExceptionHandler {

    //CrashHandler
    private static AppCrashHandler INSTANCE = new AppCrashHandler();
    //Handle UncaughtException class
    private UncaughtExceptionHandler mDefaultHandler;

    private AppCrashHandler() {
    }

    /**
     * CrashHandler Instance
     */
    public static AppCrashHandler getInstance() {
        return INSTANCE;
    }

    public void init() {
        //get UncaughtException
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //set CrashHandler
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * @param thread Thread
     * @param ex     Throwable
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //if we not catch Exception
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * handleException
     *
     * @param ex
     * @return true:Abnormality information has been processed.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        ex.printStackTrace();
        return false;
    }



}
