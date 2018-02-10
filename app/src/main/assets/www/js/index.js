/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var Toast = {
    show: function(message){
        cordova.exec(function(success){}, function(error){}, 'Toasts', 'showShort', [message]);
    }
}
var app = {
    // Application Constructor
    initialize: function () {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function () {
        document.addEventListener('deviceready', this.onDeviceReady, false);
        document.addEventListener('pause', this.onPause, false);
        document.addEventListener('resume', this.onResume, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function () {
        app.receivedEvent('deviceready');
        document.getElementById("donwload").addEventListener("click", function() {
            // 初始化文件夹
            cordova.exec(function(success){}, function(error){}, 'FileSystem', 'mkdirs', ["cordova"]);
            cordova.exec(function(success){}, function(error){}, 'FileSystem', 'mkdirs', ["cordova/download"]);

            // 下载地址
            var doenloadUrl = "https://b-ssl.duitang.com/uploads/item/201311/28/20131128155314_yQyMj.jpeg";
            // 保存文件所在的文件夹(sdcard中的目录)
            var dir = "cordova/download";
            // 保存文件名
            var name = "20131128155314_yQyMj.jpeg";

            // 下载中
            var onSuccess = function(progress){
                app.showText("下载" + progress);
                 if(progress == 100){
                    Toast.show("下载完成:/sdcard/" + dir + "/" + name);
                    app.showText("");
                }
            }

            // 下载失败
            var onError = function(message){
                Toast.show("下载失败" + message);
            }

             cordova.exec(onSuccess,Toast, 'Download', 'download', [doenloadUrl, dir, name]);
         });

        document.getElementById("network").addEventListener("click", function() {
            Toast.show("network");
         });

        document.getElementById("memory").addEventListener("click", function() {
            Toast.show("memory");
         });

        document.getElementById("socket").addEventListener("click", function() {
            Toast.show("socket");
         });

        document.getElementById("tts").addEventListener("click", function() {
            Toast.show("tts");
         });
    },
    showText: function (message){
        var div = document.getElementById("text");
        div.innerHTML= message;
    },
    // Update DOM on a Received Event
    receivedEvent: function (id) {
        console.log('Received Event: ' + id);
    },

    onPause: function() {
        console.log('onPause');
    },

    onResume: function(event) {
        console.log('onResume');
    }
};

app.initialize();
