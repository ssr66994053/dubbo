/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.remoting.transport.dispatcher;


import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.Dispatcher;
import com.alibaba.dubbo.remoting.exchange.support.header.HeartbeatHandler;
import com.alibaba.dubbo.remoting.transport.AbstractChannelHandlerDelegate;
import com.alibaba.dubbo.remoting.transport.MultiMessageHandler;

import java.util.concurrent.ExecutorService;

/**
 * @author chao.liuc
 *
 */
public class ChannelHandlers {

    public static ChannelHandler wrap(ChannelHandler handler, URL url){
        return ChannelHandlers.getInstance().wrapInternal(handler, url);
    }

    protected ChannelHandlers() {}

    protected ChannelHandler wrapInternal(ChannelHandler handler, URL url) {
        ChannelHandler internalChannelHandler = ExtensionLoader
                .getExtensionLoader(Dispatcher.class).getAdaptiveExtension().dispatch(handler, url);
        ExecutorService executorService = null;
        if (internalChannelHandler instanceof WrappedChannelHandler) {
            executorService = ((WrappedChannelHandler) internalChannelHandler).getExecutor();
        }
        return new ChannelHandlerExecutorServiceWapper(new MultiMessageHandler(new HeartbeatHandler(internalChannelHandler)),executorService);
    }

    private static ChannelHandlers INSTANCE = new ChannelHandlers();

    protected static ChannelHandlers getInstance() {
        return INSTANCE;
    }

    static void setTestingChannelHandlers(ChannelHandlers instance) {
        INSTANCE = instance;
    }

    public static class ChannelHandlerExecutorServiceWapper extends AbstractChannelHandlerDelegate {
        private ExecutorService executorService;

        public ChannelHandlerExecutorServiceWapper(ChannelHandler handler, ExecutorService executorService) {
            super(handler);
            this.executorService=executorService;
        }

        public ExecutorService getExecutorService() {
            return executorService;
        }

        public void setExecutorService(ExecutorService executorService) {
            this.executorService = executorService;
        }
    }
}